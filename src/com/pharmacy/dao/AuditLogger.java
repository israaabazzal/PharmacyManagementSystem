package com.pharmacy.dao;

import com.pharmacy.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogger {

    public static void log(int userId, String action, String entityType, int entityId,
                           String oldValues, String newValues) {
        String sql = "INSERT INTO audit_log (user_id, action, entity_type, entity_id, old_values, new_values) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, entityType);
            stmt.setInt(4, entityId);
            stmt.setString(5, oldValues);
            stmt.setString(6, newValues);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }
}
