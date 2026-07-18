package com.pharmacy.dao;

import com.pharmacy.models.Medicine;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineDAO {

   public List<Medicine> getAllMedicines() {
    List<Medicine> medicines = new ArrayList<>();

String query = 
    "SELECT m.medicine_id, m.name, m.description, m.batch_number, m.expiry_date, " +
    "       m.quantity, m.unit_price, m.selling_price, " +
    "       mc.name AS category_name " +
    "FROM medicines m " +
    "LEFT JOIN medicine_categories mc ON m.category_id = mc.category_id";



    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Medicine m = new Medicine();
            m.setId(rs.getInt("medicine_id"));
            m.setName(rs.getString("name"));
            m.setDescription(rs.getString("description"));      // ✅
            m.setBatchNumber(rs.getString("batch_number"));
            m.setExpiryDate(rs.getDate("expiry_date"));
            m.setQuantity(rs.getInt("quantity"));
            m.setUnitPrice(rs.getDouble("unit_price"));
            m.setSellingPrice(rs.getDouble("selling_price"));
            m.setCategoryName(rs.getString("category_name"));   // ✅

            medicines.add(m);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return medicines;
}

   public int insertMedicine(Medicine m) throws SQLException {
    String query = "INSERT INTO medicines (name, description, category_id, batch_number, expiry_date, quantity, unit_price, selling_price, added_by) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, m.getName());
        stmt.setString(2, m.getDescription());
        stmt.setInt(3, m.getCategoryId());  // category_id must be resolved before insert
        stmt.setString(4, m.getBatchNumber());

        java.sql.Date sqlDate = new java.sql.Date(m.getExpiryDate().getTime());
        stmt.setDate(5, sqlDate);

        stmt.setInt(6, m.getQuantity());
        stmt.setDouble(7, m.getUnitPrice());
        stmt.setDouble(8, m.getSellingPrice());
        stmt.setInt(9, m.getAddedBy());

        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1); // return the new medicine_id
            }
        }
    }
    return -1; // in case of failure
}

public boolean updateMedicine(Medicine med) throws SQLException {
   String query = "UPDATE medicines SET " +
               "name=?, " +
               "description=?, " +
               "category_id=?, " +
               "batch_number=?, " +
               "expiry_date=?, " +
               "quantity=?, " +
               "unit_price=?, " +
               "selling_price=?, " +
               "added_by=? " +
               "WHERE medicine_id=?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, med.getName());
        stmt.setString(2, med.getDescription());
        stmt.setInt(3, med.getCategoryId());  // ✅ Use categoryId, not categoryName
        stmt.setString(4, med.getBatchNumber());
        java.sql.Date sqlDate = new java.sql.Date(med.getExpiryDate().getTime());
        stmt.setDate(5, sqlDate);
        stmt.setInt(6, med.getQuantity());
        stmt.setDouble(7, med.getUnitPrice());
        stmt.setDouble(8, med.getSellingPrice());
        stmt.setInt(9, med.getAddedBy());
        stmt.setInt(10, med.getId());

        return stmt.executeUpdate() > 0;
    }
}

public void deleteMedicine(int medicineId) throws SQLException {
    String sql = "DELETE FROM medicines WHERE medicine_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, medicineId);
        stmt.executeUpdate();
    }
}
public Medicine getMedicineById(int id) throws SQLException {
    String sql = "SELECT * FROM medicines WHERE medicine_id = ?";
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Medicine med = new Medicine();
                med.setId(rs.getInt("medicine_id"));
                med.setName(rs.getString("name"));
                // set other fields...
                return med;
            }
        }
    }
    return null;
}

 public Map<Integer, Medicine> getMedicineMap() throws SQLException {
    Map<Integer, Medicine> medicineMap = new HashMap<>();

    String query = "SELECT * FROM medicines";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Medicine m = new Medicine();
            m.setId(rs.getInt("medicine_id"));
            m.setName(rs.getString("name"));
            m.setDescription(rs.getString("description"));
            m.setBatchNumber(rs.getString("batch_number"));
            m.setExpiryDate(rs.getDate("expiry_date"));
            m.setQuantity(rs.getInt("quantity"));
            m.setUnitPrice(rs.getDouble("unit_price"));
            m.setSellingPrice(rs.getDouble("selling_price"));
            m.setCategoryId(rs.getInt("category_id"));

            medicineMap.put(m.getId(), m);
        }
    }

    return medicineMap;
}

   
   
   
   
   
}
