package com.pharmacy.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database configuration  
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "israa098"; // Your MySQL password
    
    /**
     * Gets a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // 1. Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Establish and return connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException ex) {
            // Convert driver loading error to SQLException
            throw new SQLException("MySQL JDBC Driver not found!", ex);
        }
    }
    
    /**
     * Test the database connection (run this to verify setup)
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connection successful!");
            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException ex) {
            System.err.println("❌ Connection failed!");
            ex.printStackTrace();
        }
    }
}