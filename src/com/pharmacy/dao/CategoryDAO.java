package com.pharmacy.dao;

import com.pharmacy.models.Category;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class CategoryDAO {

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM medicine_categories";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category cat = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                categories.add(cat);
            }
        }
        return categories;
    }

    public Map<String, Integer> getCategoryNameIdMap() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String query = "SELECT category_id, name FROM medicine_categories";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("category_id"));
            }
        }
        return map;
    }

    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO medicine_categories (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM medicine_categories WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        }
    }
}
