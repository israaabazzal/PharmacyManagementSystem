package com.pharmacy.views;

import com.formdev.flatlaf.FlatLightLaf;
import com.pharmacy.dao.*;
import com.pharmacy.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class SalesFrame extends JFrame {
    private User currentUser;

    private JComboBox<Patient> patientCombo;
    private JComboBox<Prescription> prescriptionCombo;
    private JTable itemTable;
    private DefaultTableModel itemTableModel;
    private JTable prescriptionTable;


    private JLabel totalLabel, discountLabel, taxLabel, finalTotalLabel;
    private JComboBox<String> paymentMethodCombo;

    private JButton addMedicineBtn, finalizeSaleBtn, clearBtn;

    private List<SaleItem> saleItems = new ArrayList<>();
    private Map<Integer, Medicine> medicineMap;

    public SalesFrame(User currentUser) {
        
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        
        this.currentUser = currentUser;

        setTitle("Sales");
        setSize(900, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        loadData();
    }

    private void initComponents() {
 
   
    setTitle("Finalize Sale");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    setSize(800, 600);

    // Create a main panel with padding
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

    // Top Panel (Patient and Prescription)
    JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    topPanel.setBorder(BorderFactory.createTitledBorder("Sale Info"));

    patientCombo = new JComboBox<>();
    prescriptionCombo = new JComboBox<>();

    topPanel.add(new JLabel("Select Patient:"));
    topPanel.add(patientCombo);
    topPanel.add(new JLabel("Prescription :"));
    topPanel.add(prescriptionCombo);

    // Table Panel
    itemTableModel = new DefaultTableModel(new String[]{"Medicine", "Qty", "Unit Price", "Total"}, 0);
    itemTable = new JTable(itemTableModel);
    JScrollPane tableScrollPane = new JScrollPane(itemTable);
    tableScrollPane.setBorder(BorderFactory.createTitledBorder("Sale Items"));
    
    // prescription item  table
String[] columnNames = {"Medicine", "Quantity", "Dosage", "Instructions", "Notes"};
DefaultTableModel prescriptionTableModel = new DefaultTableModel(columnNames, 0);
prescriptionTable = new JTable(prescriptionTableModel); // ✅ Create the JTable instance
JScrollPane scrollPane = new JScrollPane(prescriptionTable);
scrollPane.setBorder(BorderFactory.createTitledBorder("Prescription Items")); // Optional styling




    // Bottom Panel
    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

    // Summary
    JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 8, 8));
    summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

    totalLabel = new JLabel("0.00");
    discountLabel = new JLabel("0.00");
    taxLabel = new JLabel("0.00");
    finalTotalLabel = new JLabel("0.00");

    summaryPanel.add(new JLabel("Total:"));
    summaryPanel.add(totalLabel);
    summaryPanel.add(new JLabel("Discount:"));
    summaryPanel.add(discountLabel);
    summaryPanel.add(new JLabel("Tax:"));
    summaryPanel.add(taxLabel);
    summaryPanel.add(new JLabel("Final Total:"));
    summaryPanel.add(finalTotalLabel);

    // Controls (Payment and Buttons)
    JPanel controlsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    controlsPanel.setBorder(BorderFactory.createTitledBorder("Payment"));

    paymentMethodCombo = new JComboBox<>(new String[]{"cash", "credit_card", "insurance", "other"});
    finalizeSaleBtn = new JButton("Finalize Sale");
    clearBtn = new JButton("Clear");

    controlsPanel.add(new JLabel("Payment Method:"));
    controlsPanel.add(paymentMethodCombo);
    controlsPanel.add(clearBtn);
    controlsPanel.add(finalizeSaleBtn);

    // Add Medicine Button
    addMedicineBtn = new JButton("➕ Add Medicine");
    addMedicineBtn.addActionListener(e -> showAddMedicineDialog());

    bottomPanel.add(addMedicineBtn, BorderLayout.NORTH);
    bottomPanel.add(summaryPanel, BorderLayout.CENTER);
    bottomPanel.add(controlsPanel, BorderLayout.SOUTH);

    // Add everything to the main panel
    mainPanel.add(topPanel, BorderLayout.NORTH);
    
   JPanel centerPanel = new JPanel();
   centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

// Prescription Table comes first
   centerPanel.add(scrollPane);         // ⬅️ prescriptionTable's scrollPane on top
   centerPanel.add(Box.createVerticalStrut(10)); // spacing
   centerPanel.add(tableScrollPane);    // ⬅️ sale item table below

    mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER); // ✅ Add center panel to main panel


    // Add main panel to the frame
    add(mainPanel);

    // Event Listeners
    finalizeSaleBtn.addActionListener(e -> finalizeSale());
    clearBtn.addActionListener(e -> clearForm());


    
    
    setLocationRelativeTo(null); // center on screen
    setVisible(true);
}


    private void loadData() {
        try {
            // Load patients
            for (Patient p : new PatientDAO().getAllPatients()) {
                patientCombo.addItem(p);
            }

            // Load prescriptions only for selected patient 
       patientCombo.addActionListener(e -> {
       Patient selectedPatient = (Patient) patientCombo.getSelectedItem();
       prescriptionCombo.removeAllItems();

    if (selectedPatient != null) {
        try {
            List<Prescription> prescriptions = new PrescriptionDAO()
                .getActivePrescriptionsByPatientId(selectedPatient.getPatientId());

            for (Prescription p : prescriptions) {
                prescriptionCombo.addItem(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load prescriptions: " + ex.getMessage());
        }
    }
});
       prescriptionCombo.addActionListener(e -> {
    Prescription selectedPrescription = (Prescription) prescriptionCombo.getSelectedItem();
    if (selectedPrescription != null) {
        loadPrescriptionItemsToTable(selectedPrescription.getPrescriptionId());
    }
});
       
       
       



            // Load medicines into map
            medicineMap = new MedicineDAO().getMedicineMap(); // e.g., <id, Medicine>

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }


    private void loadPrescriptionItemsToTable(int prescriptionId) {
    try {
        Prescription prescription = new PrescriptionDAO().getPrescriptionById(prescriptionId);
        List<PrescriptionItem> items = prescription.getItems();

        DefaultTableModel model = (DefaultTableModel) prescriptionTable.getModel();
        model.setRowCount(0); // clear existing rows

        for (PrescriptionItem item : items) {
            String medicineName = medicineMap.get(item.getMedicineId()).getName();
            model.addRow(new Object[]{
                medicineName,
                item.getQuantity(),
                item.getDosage(),
                item.getInstructions(),
                item.getNotes()
            });
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load prescription items.");
    }
}

    
    
    
 
    
private void showAddMedicineDialog() {
    JDialog dialog = new JDialog(this, "Add Medicine to Sale", true);
    dialog.setSize(600, 500);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // Medicine ComboBox (show medicine names)
    JComboBox<Medicine> medicineCombo = new JComboBox<>();
    for (Medicine m : medicineMap.values()) {
        medicineCombo.addItem(m);
    }
    medicineCombo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Medicine) {
                setText(((Medicine) value).getName());
            }
            return this;
        }
    });
    medicineCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    medicineCombo.setPreferredSize(new Dimension(250, 30));

    // Labels for available stock and unit price
    JLabel stockLabel = new JLabel("Available Stock:");
    stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JLabel stockValueLabel = new JLabel();
    stockValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    JLabel priceLabel = new JLabel("Unit Price:");
    priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JLabel priceValueLabel = new JLabel();
    priceValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    // Quantity field
    JTextField qtyField = new JTextField();
    qtyField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    qtyField.setPreferredSize(new Dimension(250, 30));

    // Build form panel vertically using your helper method for labeled fields
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    // Medicine dropdown panel
    JPanel medicinePanel = new JPanel(new BorderLayout(5, 5));
    medicinePanel.setOpaque(false);
    JLabel medicineLabel = new JLabel("Select Medicine:");
    medicineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    medicinePanel.add(medicineLabel, BorderLayout.NORTH);
    medicinePanel.add(medicineCombo, BorderLayout.CENTER);
    medicinePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    formPanel.add(medicinePanel);

    // Stock panel
    JPanel stockPanel = new JPanel(new BorderLayout(5, 5));
    stockPanel.setOpaque(false);
    stockPanel.add(stockLabel, BorderLayout.NORTH);
    stockPanel.add(stockValueLabel, BorderLayout.CENTER);
    stockPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    formPanel.add(stockPanel);

    // Price panel
    JPanel pricePanel = new JPanel(new BorderLayout(5, 5));
    pricePanel.setOpaque(false);
    pricePanel.add(priceLabel, BorderLayout.NORTH);
    pricePanel.add(priceValueLabel, BorderLayout.CENTER);
    pricePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    formPanel.add(pricePanel);

    // Quantity panel using your helper
    formPanel.add(createLabeledField("Quantity:", qtyField));

    // Buttons
    JButton addBtn = new JButton("Add");
    JButton cancelBtn = new JButton("Cancel");
    addBtn.setBackground(Color.WHITE);
    cancelBtn.setBackground(Color.WHITE);
    addBtn.setFocusPainted(false);
    cancelBtn.setFocusPainted(false);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
    buttonPanel.add(addBtn);
    buttonPanel.add(cancelBtn);

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Update stock and price when selected medicine changes
    medicineCombo.addActionListener(e -> {
        Medicine selected = (Medicine) medicineCombo.getSelectedItem();
        if (selected != null) {
            stockValueLabel.setText(String.valueOf(selected.getQuantity()));
            priceValueLabel.setText(String.format("%.2f", selected.getSellingPrice()));
        } else {
            stockValueLabel.setText("");
            priceValueLabel.setText("");
        }
    });
    if (medicineCombo.getItemCount() > 0) {
        medicineCombo.setSelectedIndex(0);
    }

    // Add button logic
    addBtn.addActionListener(e -> {
        Medicine selected = (Medicine) medicineCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(dialog, "Please select a medicine.");
            return;
        }

        String qtyText = qtyField.getText().trim();
        int qty;
        try {
            qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid quantity.");
            return;
        }

        if (qty > selected.getQuantity()) {
            JOptionPane.showMessageDialog(dialog, "Quantity exceeds available stock.");
            return;
        }

        // Check if medicine already in the table, update qty if so
  boolean found = false;

for (int i = 0; i < itemTableModel.getRowCount(); i++) {
    String medName = itemTableModel.getValueAt(i, 0).toString();
    if (medName.equals(selected.getName())) {
        int existingQty = Integer.parseInt(itemTableModel.getValueAt(i, 1).toString());
        int newQty = existingQty + qty;

        if (newQty > selected.getQuantity()) {
            JOptionPane.showMessageDialog(dialog, "Total quantity exceeds available stock.");
            return;
        }

        itemTableModel.setValueAt(newQty, i, 1);

       BigDecimal unitPrice = BigDecimal.valueOf(selected.getSellingPrice()); // ✅ Correct

        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(newQty));
        itemTableModel.setValueAt(String.format("%.2f", total), i, 3);

        // Update saleItems list
        for (SaleItem si : saleItems) {
            if (si.getMedicineId() == selected.getId()) {
                si.setQuantity(newQty);
                si.setUnitPrice(unitPrice);
                si.setTotalPrice(total);
                break;
            }
        }

        found = true;
        break;
    }
}

if (!found) {
    BigDecimal unitPrice = BigDecimal.valueOf(selected.getSellingPrice()); // ✅ Correct

    BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(qty));

    itemTableModel.addRow(new Object[]{
        selected.getName(),
        qty,
        String.format("%.2f", unitPrice),
        String.format("%.2f", total)
    });

    SaleItem si = new SaleItem();
    si.setMedicineId(selected.getId());
    si.setQuantity(qty);
    si.setUnitPrice(unitPrice);
    si.setTotalPrice(total);

    saleItems.add(si);
}


        updateTotals();
        dialog.dispose();
    });

    cancelBtn.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}


private JPanel createLabeledField(String labelText, JTextField textField) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(5, 5));
    panel.setOpaque(false);

    JLabel label = new JLabel(labelText);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textField.setPreferredSize(new Dimension(250, 30));

    panel.add(label, BorderLayout.NORTH);
    panel.add(textField, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    return panel;
}





    private void updateTotals() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < itemTableModel.getRowCount(); i++) {
            total = total.add(new BigDecimal(itemTableModel.getValueAt(i, 3).toString()));
        }

        BigDecimal discount = total.multiply(new BigDecimal("0.05"));
        BigDecimal tax = total.multiply(new BigDecimal("0.11"));
        BigDecimal finalTotal = total.subtract(discount).add(tax);

        totalLabel.setText(total.toString());
        discountLabel.setText(discount.toString());
        taxLabel.setText(tax.toString());
        finalTotalLabel.setText(finalTotal.toString());
    }

private void finalizeSale() {
    if (saleItems.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No items in the sale. Please add medicines.");
        return;
    }

    Patient selectedPatient = (Patient) patientCombo.getSelectedItem();
    if (selectedPatient == null) {
        JOptionPane.showMessageDialog(this, "Please select a patient.");
        return;
    }

    Prescription selectedPrescription = (Prescription) prescriptionCombo.getSelectedItem();
    String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

    try {
        // Build the Sale object
        Sale sale = new Sale();
        sale.setPatientId(selectedPatient.getPatientId());
        sale.setPrescriptionId(selectedPrescription != null ? selectedPrescription.getPrescriptionId() : null);
        sale.setTotalAmount(new BigDecimal(totalLabel.getText()));
        sale.setDiscount(new BigDecimal(discountLabel.getText()));
        sale.setTaxAmount(new BigDecimal(taxLabel.getText()));
        sale.setPaymentMethod(paymentMethod);
        sale.setPaymentStatus("paid"); // or allow a dropdown if you want manual selection
        sale.setProcessedBy(currentUser.getUserId());

        // Call DAO to insert into DB
        SaleDAO dao = new SaleDAO();
        int saleId = dao.addSale(sale, saleItems);

        if (saleId > 0) {
            JOptionPane.showMessageDialog(this, "Sale finalized successfully. Sale ID: " + saleId);

            // Optional: clear form for next transaction
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to finalize sale. Please try again.");
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
    }
}


    private void clearForm() {
        itemTableModel.setRowCount(0);
        saleItems.clear();
        updateTotals();
    }

    public static void main(String[] args) {
        User dummy = new User();
        dummy.setUserId(3);
        dummy.setUsername("staff");
        SwingUtilities.invokeLater(() -> new SalesFrame(dummy).setVisible(true));
    }
}