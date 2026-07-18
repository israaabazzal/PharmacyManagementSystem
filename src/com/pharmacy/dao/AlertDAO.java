package com.pharmacy.dao;

import com.pharmacy.models.Alert;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    // Get unresolved alerts
    public List<Alert> getUnresolvedAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts WHERE is_resolved = FALSE ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlertId(rs.getInt("alert_id"));
                alert.setMedicineId(rs.getInt("medicine_id"));
                alert.setAlertType(rs.getString("alert_type"));
                alert.setMessage(rs.getString("message"));
                alert.setSeverity(rs.getString("severity"));
                alert.setResolved(rs.getBoolean("is_resolved"));
                alert.setResolvedBy(rs.getInt("resolved_by"));
                alert.setResolvedAt(rs.getTimestamp("resolved_at"));
                alert.setCreatedAt(rs.getTimestamp("created_at"));
                alerts.add(alert);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alerts;
    }

    // Check and insert alerts for low stock and expiry
    // Also auto-resolve stale alerts
    public void checkAndGenerateAlerts() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 0️⃣ Auto-resolve stale low stock alerts
            String resolveLowStockSql =
                    "UPDATE alerts a " +
                    "JOIN medicines m ON a.medicine_id = m.medicine_id " +
                    "SET a.is_resolved = TRUE, a.resolved_at = CURRENT_TIMESTAMP " +
                    "WHERE a.alert_type = 'low_stock' AND a.is_resolved = FALSE AND m.quantity >= 20";
            try (PreparedStatement stmt = conn.prepareStatement(resolveLowStockSql)) {
                stmt.executeUpdate();
            }

            // 0️⃣ Auto-resolve stale expiry alerts
            String resolveExpirySql =
                    "UPDATE alerts a " +
                    "JOIN medicines m ON a.medicine_id = m.medicine_id " +
                    "SET a.is_resolved = TRUE, a.resolved_at = CURRENT_TIMESTAMP " +
                    "WHERE a.alert_type = 'expiry' AND a.is_resolved = FALSE AND (m.expiry_date > DATE_ADD(CURDATE(), INTERVAL 30 DAY))";
            try (PreparedStatement stmt = conn.prepareStatement(resolveExpirySql)) {
                stmt.executeUpdate();
            }

            // 1️⃣ Insert new low stock alerts
            String lowStockSql =
                    "INSERT INTO alerts (medicine_id, alert_type, message, severity) " +
                    "SELECT medicine_id, 'low_stock', CONCAT(name, ' is low in stock (', quantity, ')'), 'high' " +
                    "FROM medicines " +
                    "WHERE quantity < 20 " +
                    "AND medicine_id NOT IN ( " +
                    "   SELECT medicine_id FROM alerts WHERE alert_type = 'low_stock' AND is_resolved = FALSE " +
                    ");";

            // 2️⃣ Insert new expiry alerts (7, 15, 30 days)
            String expirySql =
                    "INSERT INTO alerts (medicine_id, alert_type, message, severity) " +
                    "SELECT medicine_id, 'expiry', " +
                    "CONCAT(name, ' will expire on ', expiry_date), " +
                    "CASE " +
                    "   WHEN DATEDIFF(expiry_date, CURDATE()) <= 7 THEN 'high' " +
                    "   WHEN DATEDIFF(expiry_date, CURDATE()) <= 15 THEN 'medium' " +
                    "   ELSE 'low' " +
                    "END " +
                    "FROM medicines " +
                    "WHERE expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                    "AND medicine_id NOT IN ( " +
                    "   SELECT medicine_id FROM alerts WHERE alert_type = 'expiry' AND is_resolved = FALSE " +
                    ");";

            try (PreparedStatement stmt1 = conn.prepareStatement(lowStockSql);
                 PreparedStatement stmt2 = conn.prepareStatement(expirySql)) {
                stmt1.executeUpdate();
                stmt2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mark an alert as resolved manually
    public void resolveAlert(int alertId, int userId) {
        String sql = "UPDATE alerts SET is_resolved = TRUE, resolved_by = ?, resolved_at = CURRENT_TIMESTAMP WHERE alert_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, alertId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}