package com.pharmacy.dao;

import com.pharmacy.models.Doctor;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> searchDoctors(String keyword) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE full_name LIKE ? OR license_number LIKE ?";
        boolean isNumeric = keyword.matches("\\d+");

        if (isNumeric) {
            sql += " OR doctor_id = ?";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            if (isNumeric) {
                stmt.setInt(3, Integer.parseInt(keyword));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapRowToDoctor(rs));
                }
            }
        }

        return doctors;
    }

    public boolean addDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (full_name, specialization, phone, email, license_number) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhone());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, doctor.getLicenseNumber());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET full_name=?, specialization=?, phone=?, email=?, license_number=? " +
                     "WHERE doctor_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhone());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, doctor.getLicenseNumber());
            stmt.setInt(6, doctor.getDoctorId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteDoctor(int id) throws SQLException {
        String sql = "DELETE FROM doctors WHERE doctor_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Doctor mapRowToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setEmail(rs.getString("email"));
        doctor.setLicenseNumber(rs.getString("license_number"));
        return doctor;
    }
    
    public Doctor getDoctorById(int doctorId) throws SQLException {
    String sql = "SELECT * FROM doctors WHERE doctor_id = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setInt(1, doctorId);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapRowToDoctor(rs);
            }
        }
    }

    return null; // Return null if not found
}

    
    
    
}
