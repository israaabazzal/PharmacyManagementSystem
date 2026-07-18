package com.pharmacy.dao;

import com.pharmacy.models.Prescription;
import com.pharmacy.models.PrescriptionItem;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptionDAO {

    // Add prescription + items in one transaction
    public boolean addPrescription(Prescription prescription) throws SQLException {
        String insertPrescriptionSQL = "INSERT INTO prescriptions (patient_id, doctor_id, prescription_date, notes, status, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO prescription_items (prescription_id, medicine_id, quantity, dosage, instructions, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(insertPrescriptionSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, prescription.getPatientId());
                ps.setInt(2, prescription.getDoctorId());
                ps.setDate(3, new java.sql.Date(prescription.getPrescriptionDate().getTime()));
                ps.setString(4, prescription.getNotes());
                ps.setString(5, prescription.getStatus());
                ps.setInt(6, prescription.getCreatedBy());

                int affected = ps.executeUpdate();
                if (affected == 0) {
                    con.rollback();
                    return false;
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int prescriptionId = keys.getInt(1);
                        prescription.setPrescriptionId(prescriptionId);

                        try (PreparedStatement psItem = con.prepareStatement(insertItemSQL)) {
                            for (PrescriptionItem item : prescription.getItems()) {
                                psItem.setInt(1, prescriptionId);
                                psItem.setInt(2, item.getMedicineId());
                                psItem.setInt(3, item.getQuantity());
                                psItem.setString(4, item.getDosage());
                                psItem.setString(5, item.getInstructions());
                                psItem.setString(6, item.getNotes());
                                psItem.addBatch();
                            }
                            psItem.executeBatch();
                        }
                    } else {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // Update prescription + items transactionally
    public boolean updatePrescription(Prescription prescription) throws SQLException {
        String updatePrescriptionSQL = "UPDATE prescriptions SET patient_id=?, doctor_id=?, prescription_date=?, notes=?, status=? WHERE prescription_id=?";
        String deleteItemsSQL = "DELETE FROM prescription_items WHERE prescription_id=?";
        String insertItemSQL = "INSERT INTO prescription_items (prescription_id, medicine_id, quantity, dosage, instructions, notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psUpdate = con.prepareStatement(updatePrescriptionSQL)) {
                psUpdate.setInt(1, prescription.getPatientId());
                psUpdate.setInt(2, prescription.getDoctorId());
                psUpdate.setDate(3, new java.sql.Date(prescription.getPrescriptionDate().getTime()));
                psUpdate.setString(4, prescription.getNotes());
                psUpdate.setString(5, prescription.getStatus());
                psUpdate.setInt(6, prescription.getPrescriptionId());

                int updated = psUpdate.executeUpdate();
                if (updated == 0) {
                    con.rollback();
                    return false;
                }

                try (PreparedStatement psDeleteItems = con.prepareStatement(deleteItemsSQL)) {
                    psDeleteItems.setInt(1, prescription.getPrescriptionId());
                    psDeleteItems.executeUpdate();
                }

                try (PreparedStatement psInsertItems = con.prepareStatement(insertItemSQL)) {
                    for (PrescriptionItem item : prescription.getItems()) {
                        psInsertItems.setInt(1, prescription.getPrescriptionId());
                        psInsertItems.setInt(2, item.getMedicineId());
                        psInsertItems.setInt(3, item.getQuantity());
                        psInsertItems.setString(4, item.getDosage());
                        psInsertItems.setString(5, item.getInstructions());
                        psInsertItems.setString(6, item.getNotes());
                        psInsertItems.addBatch();
                    }
                    psInsertItems.executeBatch();
                }

                con.commit();
                return true;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // Delete prescription + its items
    public boolean deletePrescription(int prescriptionId) throws SQLException {
        String deleteItemsSQL = "DELETE FROM prescription_items WHERE prescription_id=?";
        String deletePrescriptionSQL = "DELETE FROM prescriptions WHERE prescription_id=?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psDeleteItems = con.prepareStatement(deleteItemsSQL);
                 PreparedStatement psDeletePrescription = con.prepareStatement(deletePrescriptionSQL)) {
                psDeleteItems.setInt(1, prescriptionId);
                psDeleteItems.executeUpdate();

                psDeletePrescription.setInt(1, prescriptionId);
                int deleted = psDeletePrescription.executeUpdate();

                con.commit();
                return deleted > 0;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // Get prescription by ID (with items)
    public Prescription getPrescriptionById(int id) throws SQLException {
        String sqlPrescription = "SELECT * FROM prescriptions WHERE prescription_id=?";
        String sqlItems = "SELECT * FROM prescription_items WHERE prescription_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement psPres = con.prepareStatement(sqlPrescription);
             PreparedStatement psItems = con.prepareStatement(sqlItems)) {

            psPres.setInt(1, id);
            try (ResultSet rsPres = psPres.executeQuery()) {
                if (!rsPres.next()) return null;

                Prescription p = new Prescription();
                p.setPrescriptionId(rsPres.getInt("prescription_id"));
                p.setPatientId(rsPres.getInt("patient_id"));
                p.setDoctorId(rsPres.getInt("doctor_id"));
                p.setPrescriptionDate(rsPres.getDate("prescription_date"));
                p.setNotes(rsPres.getString("notes"));
                p.setStatus(rsPres.getString("status"));
                p.setCreatedBy(rsPres.getInt("created_by"));
                int approvedBy = rsPres.getInt("approved_by");
                if (!rsPres.wasNull()) p.setApprovedBy(approvedBy);

                // Load items
                psItems.setInt(1, id);
                try (ResultSet rsItems = psItems.executeQuery()) {
                    List<PrescriptionItem> items = new ArrayList<>();
                    while (rsItems.next()) {
                        PrescriptionItem item = new PrescriptionItem();
                        item.setItemId(rsItems.getInt("item_id"));
                        item.setPrescriptionId(id);
                        item.setMedicineId(rsItems.getInt("medicine_id"));
                        item.setQuantity(rsItems.getInt("quantity"));
                        item.setDosage(rsItems.getString("dosage"));
                        item.setInstructions(rsItems.getString("instructions"));
                        item.setNotes(rsItems.getString("notes"));
                        items.add(item);
                    }
                    p.setItems(items);
                }
                return p;
            }
        }
    }

    // Search prescriptions by keyword on patient name, doctor name, or status
    public List<Prescription> searchPrescriptions(String keyword) throws SQLException {
String sql = "SELECT pr.* FROM prescriptions pr "
           + "JOIN patients pt ON pr.patient_id = pt.patient_id "
           + "JOIN doctors dc ON pr.doctor_id = dc.doctor_id "
           + "WHERE pt.full_name LIKE ? OR dc.full_name LIKE ? OR pr.status LIKE ? "
           + "ORDER BY pr.prescription_date DESC";

        List<Prescription> results = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);
            ps.setString(3, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prescription p = new Prescription();
                    p.setPrescriptionId(rs.getInt("prescription_id"));
                    p.setPatientId(rs.getInt("patient_id"));
                    p.setDoctorId(rs.getInt("doctor_id"));
                    p.setPrescriptionDate(rs.getDate("prescription_date"));
                    p.setNotes(rs.getString("notes"));
                    p.setStatus(rs.getString("status"));
                    p.setCreatedBy(rs.getInt("created_by"));
                    int approvedBy = rs.getInt("approved_by");
                    if (!rs.wasNull()) p.setApprovedBy(approvedBy);
                    // We can optionally load items separately if needed
                    results.add(p);
                }
            }
        }
        return results;
    }
    
    
    public boolean updateStatus(int prescriptionId, String newStatus) throws SQLException {
    String sql = "UPDATE prescriptions SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE prescription_id = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, newStatus.toLowerCase());
        stmt.setInt(2, prescriptionId);

        return stmt.executeUpdate() > 0;
    }
}
   public List<Prescription> getActivePrescriptionsByPatientId(int patientId) throws SQLException {
    String sql = "SELECT * FROM prescriptions WHERE status = ? AND patient_id = ? ORDER BY prescription_date DESC";

    List<Prescription> activePrescriptions = new ArrayList<>();
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "active");
        ps.setInt(2, patientId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setPrescriptionDate(rs.getDate("prescription_date"));
                p.setNotes(rs.getString("notes"));
                p.setStatus(rs.getString("status"));
                p.setCreatedBy(rs.getInt("created_by"));
                int approvedBy = rs.getInt("approved_by");
                if (!rs.wasNull()) p.setApprovedBy(approvedBy);
                
                activePrescriptions.add(p);
            }
        }
    }
    return activePrescriptions;
}


public List<Prescription> getAllPrescriptions() throws SQLException {
    List<Prescription> prescriptions = new ArrayList<>();

    String sql = "SELECT * FROM prescriptions";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Prescription p = new Prescription();
            p.setPrescriptionId(rs.getInt("prescription_id"));
            p.setPatientId(rs.getInt("patient_id"));
            p.setDoctorId(rs.getInt("doctor_id"));
            p.setPrescriptionDate(rs.getDate("prescription_date"));
            p.setNotes(rs.getString("notes"));
            p.setStatus(rs.getString("status"));
            p.setCreatedBy(rs.getInt("created_by"));
            int approvedBy = rs.getInt("approved_by");
            if (!rs.wasNull()) p.setApprovedBy(approvedBy);

            prescriptions.add(p);
        }
    }

    return prescriptions;
}

}
