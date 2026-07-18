package com.pharmacy.dao;

import com.pharmacy.models.Patient;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public List<Patient> findPatients(String idOrKeyword) throws SQLException {
        List<Patient> patients = new ArrayList<>();

        if (idOrKeyword == null || idOrKeyword.isEmpty()) {
            return getAllPatients();
        }

        try {
            int patientId = Integer.parseInt(idOrKeyword);
            String sql = "SELECT * FROM patients WHERE patient_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, patientId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patients.add(mapRowToPatient(rs));
                    }
                }
            }
        } catch (NumberFormatException ex) {
            String sql = "SELECT * FROM patients WHERE full_name LIKE ? OR phone LIKE ? OR email LIKE ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                String likeKeyword = "%" + idOrKeyword + "%";
                stmt.setString(1, likeKeyword);
                stmt.setString(2, likeKeyword);
                stmt.setString(3, likeKeyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        patients.add(mapRowToPatient(rs));
                    }
                }
            }
        }

        return patients;
    }

    public boolean addPatient(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (full_name, date_of_birth, gender, address, phone, email, medical_history, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getFullName());
            stmt.setDate(2, p.getDateOfBirth() == null ? null : Date.valueOf(p.getDateOfBirth()));
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getAddress());
            stmt.setString(5, p.getPhone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getMedicalHistory());
            stmt.setInt(8, p.getCreatedBy());

            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setPatientId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patients SET full_name=?, date_of_birth=?, gender=?, address=?, phone=?, email=?, medical_history=?, updated_at=NOW() " +
                     "WHERE patient_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getFullName());
            stmt.setDate(2, p.getDateOfBirth() == null ? null : Date.valueOf(p.getDateOfBirth()));
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getAddress());
            stmt.setString(5, p.getPhone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getMedicalHistory());
            stmt.setInt(8, p.getPatientId());

            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    }

    public boolean deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapRowToPatient(rs));
            }
        }
        return patients;
    }

    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPatient(rs);
                }
            }
        }
        return null;
    }

    private Patient mapRowToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setFullName(rs.getString("full_name"));

        Date dob = rs.getDate("date_of_birth");
        p.setDateOfBirth(dob == null ? null : dob.toLocalDate());

        p.setGender(rs.getString("gender"));
        p.setAddress(rs.getString("address"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setMedicalHistory(rs.getString("medical_history"));
        p.setCreatedBy(rs.getInt("created_by"));

        return p;
    }
}