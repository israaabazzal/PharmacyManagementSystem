package com.pharmacy.dao;

import com.pharmacy.models.Sale;
import com.pharmacy.models.SaleItem;
import com.pharmacy.utils.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class SaleDAO {

    /**
     * Adds a sale and its items in one transaction.
     * Also deducts stock from medicines,
     * and inserts inventory transaction records.
     *
     * @param sale  Sale object (without saleId, which will be generated)
     * @param items List of SaleItem linked to the sale
     * @return generated sale_id on success, -1 on failure
     * @throws SQLException if DB error occurs
     */
public int addSale(Sale sale, List<SaleItem> items) throws SQLException {
    String insertSaleSQL = "INSERT INTO sales (prescription_id, patient_id, total_amount, discount, tax_amount, payment_method, payment_status, processed_by) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String insertItemSQL = "INSERT INTO sale_items (sale_id, medicine_id, quantity, unit_price, total_price) " +
                           "VALUES (?, ?, ?, ?, ?)";
    String insertInventoryTransactionSQL = "INSERT INTO inventory_transactions " +
            "(medicine_id, transaction_type, quantity, unit_price, total_value, reference_id, reference_type, notes, performed_by) " +
            "VALUES (?, 'sale', ?, ?, ?, ?, 'sale', ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        int saleId;
        try (PreparedStatement insertSaleStmt = conn.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS)) {
            if (sale.getPrescriptionId() != null)
                insertSaleStmt.setInt(1, sale.getPrescriptionId());
            else
                insertSaleStmt.setNull(1, Types.INTEGER);

            insertSaleStmt.setInt(2, sale.getPatientId());
            insertSaleStmt.setBigDecimal(3, sale.getTotalAmount());
            insertSaleStmt.setBigDecimal(4, sale.getDiscount());
            insertSaleStmt.setBigDecimal(5, sale.getTaxAmount());
            insertSaleStmt.setString(6, sale.getPaymentMethod());
            insertSaleStmt.setString(7, sale.getPaymentStatus());
            insertSaleStmt.setInt(8, sale.getProcessedBy());

            int affectedRows = insertSaleStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return -1;
            }

            try (ResultSet generatedKeys = insertSaleStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                    sale.setSaleId(saleId);
                } else {
                    conn.rollback();
                    return -1;
                }
            }
        }

        // Insert sale items (trigger handles stock)
        try (PreparedStatement insertItemStmt = conn.prepareStatement(insertItemSQL);
             PreparedStatement insertInventoryStmt = conn.prepareStatement(insertInventoryTransactionSQL)) {

            for (SaleItem item : items) {
                insertItemStmt.setInt(1, saleId);
                insertItemStmt.setInt(2, item.getMedicineId());
                insertItemStmt.setInt(3, item.getQuantity());
                insertItemStmt.setBigDecimal(4, item.getUnitPrice());
                insertItemStmt.setBigDecimal(5, item.getTotalPrice());
                insertItemStmt.executeUpdate();

                // Optional: log inventory transaction
                insertInventoryStmt.setInt(1, item.getMedicineId());
                insertInventoryStmt.setInt(2, item.getQuantity());
                insertInventoryStmt.setBigDecimal(3, item.getUnitPrice());
                insertInventoryStmt.setBigDecimal(4, item.getTotalPrice());
                insertInventoryStmt.setInt(5, saleId);
                insertInventoryStmt.setString(6, "Sale processed");
                insertInventoryStmt.setInt(7, sale.getProcessedBy());
                insertInventoryStmt.executeUpdate();
            }
        }

        // Update prescription status
        if (sale.getPrescriptionId() != null) {
            String updatePrescriptionSQL = "UPDATE prescriptions SET status = 'completed' WHERE prescription_id = ?";
            try (PreparedStatement updatePrescriptionStmt = conn.prepareStatement(updatePrescriptionSQL)) {
                updatePrescriptionStmt.setInt(1, sale.getPrescriptionId());
                updatePrescriptionStmt.executeUpdate();
            }
        }

        conn.commit();
        return saleId;
    } catch (SQLException e) {
        throw e;
    }
}
}
