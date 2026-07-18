package com.pharmacy.dao;

import com.pharmacy.models.User;
import com.pharmacy.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, password, email, phone, role_id, is_active "
                   + "FROM users WHERE username = ? AND password = ? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            // Execute query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("role_id"),
                        rs.getBoolean("is_active")
                    );
                }
            }
        }
        return null; // No matching user found
    }
   
    public List<User> getAllUsers() throws SQLException {
    List<User> users = new ArrayList<>();
    String query = "SELECT u.user_id, u.username, r.role_name ,u.is_active " +
                   "FROM users u JOIN roles r ON u.role_id = r.role_id";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setRoleName(rs.getString("role_name"));
            user.setActive(rs.getBoolean("is_active"));
            users.add(user);
        }
    }

    return users;
}
public void addUser(User user) throws SQLException {
    String sql = "INSERT INTO users (username, password, email, phone, role_id, is_active) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword()); // Hash it in real app
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPhone());
        stmt.setInt(5, user.getRoleId());
        stmt.setBoolean(6, user.isActive());

        stmt.executeUpdate();
    }
}

  public void deleteUser(int userId) throws SQLException {
    String sql = "DELETE FROM users WHERE user_id = ?";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.executeUpdate();
    }
}
  
    
 public User getUserById(int userId) throws SQLException {
    String sql = "SELECT * FROM users WHERE user_id = ?";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getInt("role_id"),
                    rs.getBoolean("is_active")
                );
            }
        }
    }
    return null;
}

 
 
 public void updateUser(User user) throws SQLException {
    String sql = "UPDATE users SET username = ?, email = ?, phone = ?, role_id = ? , is_active = ? WHERE user_id = ?";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPhone());
        stmt.setInt(4, user.getRoleId());
        stmt.setInt(6, user.getUserId());
        stmt.setBoolean(5, user.isActive());
        

        stmt.executeUpdate();
    }
}

 
 // Update only active status (quick toggle)
    public void updateUserStatus(int userId, boolean isActive) throws SQLException {
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
 
 
}
