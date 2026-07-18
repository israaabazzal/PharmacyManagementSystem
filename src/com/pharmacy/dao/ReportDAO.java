package com.pharmacy.dao;

import com.pharmacy.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public int getUserCount() throws SQLException {
        return getSingleInt("SELECT COUNT(*) FROM users");
    }

    public int getMedicineCount() throws SQLException {
        return getSingleInt("SELECT COUNT(*) FROM medicines");
    }

    public int getPrescriptionCount() throws SQLException {
        return getSingleInt("SELECT COUNT(*) FROM prescriptions");
    }

    public double getTotalSales() throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM sales";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0;
    }

    private int getSingleInt(String sql) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    
  
  public int getLowStockCount(int threshold) throws SQLException {
    String sql = "SELECT COUNT(*) FROM medicines WHERE quantity < ?";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, threshold);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }
}

public int getExpiringSoonCount(int days) throws SQLException {
    String sql = "SELECT COUNT(*) FROM medicines WHERE expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY)";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, days);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }
}




public List<String[]> getRecentActivities(int limit) throws SQLException {
    List<String[]> activities = new ArrayList<>();
String sql =
    "SELECT a.created_at, u.username, a.action, " +
    "CONCAT(a.entity_type, ' #', a.entity_id) " +
    "FROM audit_log a " +
    "JOIN users u ON a.user_id = u.user_id " +
    "ORDER BY a.created_at DESC " +
    "LIMIT ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, limit);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String[] row = new String[4];
            row[0] = rs.getString("created_at");
            row[1] = rs.getString("username");
            row[2] = rs.getString("action");
            row[3] = rs.getString(4); // the "Details" column: entity_type + id
            activities.add(row);
        }
    }

    return activities;
}

}



