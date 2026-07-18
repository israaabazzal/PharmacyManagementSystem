package com.pharmacy.views;

import com.pharmacy.dao.*;
import com.pharmacy.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionManagementFrame extends JFrame {
    private User currentPharmacist;

    private JTable prescriptionTable;
    private JTable itemTable;
    private DefaultTableModel prescriptionTableModel;
    private DefaultTableModel itemTableModel;

    private PrescriptionDAO prescriptionDAO;
    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;
    private MedicineDAO medicineDAO;
    private JPanel mainPanel;


    public PrescriptionManagementFrame(User currentPharmacist) {
        this.currentPharmacist = currentPharmacist;
        this.prescriptionDAO = new PrescriptionDAO();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
        this.medicineDAO = new MedicineDAO();

        setTitle("Prescription Management");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadPrescriptions();
    }

    private void initComponents() {
        
    mainPanel = new JPanel(new BorderLayout(10, 10)) {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(0, getHeight());
        GradientPaint gp = new GradientPaint(start, new Color(230, 245, 255), end, new Color(240, 230, 250));

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
};
      mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // ========== TOP BUTTON PANEL ==========
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnSearch = new JButton("Search");

        topButtonPanel.add(btnAdd);
        topButtonPanel.add(btnEdit);
        topButtonPanel.add(btnDelete);
        topButtonPanel.add(btnRefresh);
        topButtonPanel.add(btnSearch);

        // ========== PRESCRIPTION TABLE ==========
        prescriptionTableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date", "Status", "Notes"}, 0);
        prescriptionTable = new JTable(prescriptionTableModel);
        JScrollPane prescriptionScroll = new JScrollPane(prescriptionTable);

        // ========== ITEM TABLE ==========
        itemTableModel = new DefaultTableModel(new Object[]{"Medicine", "Quantity", "Dosage"}, 0);
        itemTable = new JTable(itemTableModel);
        JScrollPane itemScroll = new JScrollPane(itemTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, prescriptionScroll, itemScroll);
        splitPane.setResizeWeight(0.7);

        // ========== MAIN PANEL LAYOUT ==========
        mainPanel.add(topButtonPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);

        // ========== EVENT LISTENERS ==========
        btnRefresh.addActionListener(e -> loadPrescriptions());
        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelectedPrescription());
        btnSearch.addActionListener(e -> searchPrescriptions());

        prescriptionTable.getSelectionModel().addListSelectionListener(e -> loadSelectedPrescriptionItems());
    }

   private void loadPrescriptions() {
    try {
        prescriptionTableModel.setRowCount(0);

        // Use searchPrescriptions("") to get all prescriptions
        List<Prescription> prescriptions = prescriptionDAO.searchPrescriptions("");

        for (Prescription p : prescriptions) {
            Patient patient = patientDAO.getPatientById(p.getPatientId());
            Doctor doctor = doctorDAO.getDoctorById(p.getDoctorId());

            prescriptionTableModel.addRow(new Object[]{
                p.getPrescriptionId(),
                patient != null ? patient.getFullName() : "Unknown",
                doctor != null ? doctor.getFullName() : "Unknown",
                p.getPrescriptionDate(),  // changed to getPrescriptionDate() as per model
                p.getStatus(),
                p.getNotes()
            });
        }

        itemTableModel.setRowCount(0);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Failed to load prescriptions: " + ex.getMessage());
    }
}


    private void loadSelectedPrescriptionItems() {
        int selectedRow = prescriptionTable.getSelectedRow();
        if (selectedRow == -1) return;

        int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);
        try {
            Prescription prescription = prescriptionDAO.getPrescriptionById(prescriptionId);
            itemTableModel.setRowCount(0);

            for (PrescriptionItem item : prescription.getItems()) {
                Medicine med = medicineDAO.getMedicineById(item.getMedicineId());
                itemTableModel.addRow(new Object[]{
                    med != null ? med.getName() : "Unknown",
                    item.getQuantity(),
                    item.getDosage()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + ex.getMessage());
        }
    }

    private void deleteSelectedPrescription() {
        int selectedRow = prescriptionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription to delete.");
            return;
        }

        int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this prescription?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                prescriptionDAO.deletePrescription(prescriptionId);
                AuditLogger.log(currentPharmacist.getUserId(), "Delete", "Prescription", prescriptionId, null, null);
                loadPrescriptions();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
            }
        }
    }

private void searchPrescriptions() {
    String keyword = JOptionPane.showInputDialog(this, "Enter patient or doctor name:");
    if (keyword == null || keyword.trim().isEmpty()) return;

    try {
        List<Prescription> prescriptions = prescriptionDAO.searchPrescriptions(keyword.trim());
        prescriptionTableModel.setRowCount(0);

        for (Prescription p : prescriptions) {
            Patient patient = patientDAO.getPatientById(p.getPatientId());
            Doctor doctor = doctorDAO.getDoctorById(p.getDoctorId());

            prescriptionTableModel.addRow(new Object[]{
                p.getPrescriptionId(),
                patient != null ? patient.getFullName() : "Unknown",
                doctor != null ? doctor.getFullName() : "Unknown",
                p.getPrescriptionDate(),
                p.getStatus(),
                p.getNotes()
            });
        }

        itemTableModel.setRowCount(0);  // clear item table

        // ✅ Reattach row click listener after refreshing table data
        prescriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPrescriptionItems();
            }
        });

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
    }
}

    // ==== DIALOG METHODS (to be implemented next) ====
    private void openAddDialog() {
        
     
    JDialog dialog = new JDialog(this, "Add New Prescription", true);
    dialog.setSize(600, 700);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // === Form fields ===
    JComboBox<Patient> patientCombo = new JComboBox<>();
    JComboBox<Doctor> doctorCombo = new JComboBox<>();
    JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    JTextField statusField = new JTextField();
    JTextArea notesArea = new JTextArea(4, 20);

    // Load patients and doctors into combos
    try {
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            patientCombo.addItem(p);
        }
        List<Doctor> doctors = doctorDAO.searchDoctors(""); // empty to get all doctors
        for (Doctor d : doctors) {
            doctorCombo.addItem(d);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to load patients or doctors: " + ex.getMessage());
    }

    // Date spinner config
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
    dateSpinner.setEditor(dateEditor);
    dateSpinner.setValue(new java.util.Date());

    // === Prescription Items Table ===
    String[] itemColumns = {"Medicine ID", "Quantity", "Dosage", "Instructions", "Notes"};
    DefaultTableModel itemsTableModel = new DefaultTableModel(itemColumns, 0);
    JTable itemsTable = new JTable(itemsTableModel);
    JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
    itemsScrollPane.setPreferredSize(new Dimension(550, 200));

    // Panel for adding new prescription items (fields + Add button)
    JTextField medicineIdField = new JTextField();
    JTextField quantityField = new JTextField();
    JTextField dosageField = new JTextField();
    JTextField instructionsField = new JTextField();
    JTextField itemNotesField = new JTextField();
    JButton addItemBtn = new JButton("Add Item");

    addItemBtn.addActionListener(e -> {
        try {
            int medId = Integer.parseInt(medicineIdField.getText().trim());
            int qty = Integer.parseInt(quantityField.getText().trim());
            String dosage = dosageField.getText().trim();
            String instructions = instructionsField.getText().trim();
            String iNotes = itemNotesField.getText().trim();

            itemsTableModel.addRow(new Object[]{medId, qty, dosage, instructions, iNotes});

            // Clear fields after adding
            medicineIdField.setText("");
            quantityField.setText("");
            dosageField.setText("");
            instructionsField.setText("");
            itemNotesField.setText("");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for Medicine ID and Quantity.");
        }
    });

    JPanel addItemPanel = new JPanel(new GridLayout(2, 5, 5, 5));
    addItemPanel.add(new JLabel("Medicine ID"));
    addItemPanel.add(new JLabel("Quantity"));
    addItemPanel.add(new JLabel("Dosage"));
    addItemPanel.add(new JLabel("Instructions"));
    addItemPanel.add(new JLabel("Notes"));

    addItemPanel.add(medicineIdField);
    addItemPanel.add(quantityField);
    addItemPanel.add(dosageField);
    addItemPanel.add(instructionsField);
    addItemPanel.add(itemNotesField);

    JPanel addItemButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    addItemButtonPanel.add(addItemBtn);

    // === Main form panel ===
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    formPanel.add(createLabeledField("Patient:", patientCombo));
    formPanel.add(createLabeledField("Doctor:", doctorCombo));
    formPanel.add(createLabeledField("Date:", dateSpinner));
    formPanel.add(createLabeledField("Status:", statusField));

    JPanel notesPanel = new JPanel(new BorderLayout());
    notesPanel.setBackground(Color.WHITE);
    JLabel notesLabel = new JLabel("Notes:");
    notesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    notesArea.setLineWrap(true);
    notesArea.setWrapStyleWord(true);
    JScrollPane notesScroll = new JScrollPane(notesArea);
    notesScroll.setPreferredSize(new Dimension(250, 80));
    notesPanel.add(notesLabel, BorderLayout.NORTH);
    notesPanel.add(notesScroll, BorderLayout.CENTER);
    notesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    formPanel.add(notesPanel);

    // === Items section panel ===
    JPanel itemsPanel = new JPanel(new BorderLayout(10, 10));
    itemsPanel.setBackground(Color.WHITE);
    itemsPanel.setBorder(BorderFactory.createTitledBorder("Prescription Items"));
    itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);
    itemsPanel.add(addItemPanel, BorderLayout.NORTH);
    itemsPanel.add(addItemButtonPanel, BorderLayout.SOUTH);

    // === Buttons panel ===
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setBackground(Color.WHITE);
    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");
    buttonsPanel.add(saveBtn);
    buttonsPanel.add(cancelBtn);

    // Add to dialog
    dialog.add(formPanel, BorderLayout.NORTH);
    dialog.add(itemsPanel, BorderLayout.CENTER);
    dialog.add(buttonsPanel, BorderLayout.SOUTH);

    // === Save button action ===
    saveBtn.addActionListener(e -> {
        try {
            Patient selectedPatient = (Patient) patientCombo.getSelectedItem();
            Doctor selectedDoctor = (Doctor) doctorCombo.getSelectedItem();
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            String status = statusField.getText().trim();
            String notes = notesArea.getText().trim();

            if (selectedPatient == null || selectedDoctor == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a patient and a doctor.");
                return;
            }
            if (status.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Status cannot be empty.");
                return;
            }

            // Build prescription object
            Prescription p = new Prescription();
            p.setPatientId(selectedPatient.getPatientId());
            p.setDoctorId(selectedDoctor.getDoctorId());
            p.setPrescriptionDate(new java.sql.Date(selectedDate.getTime()));
            p.setStatus(status);
            p.setNotes(notes);
            p.setCreatedBy(currentPharmacist.getUserId());

            // Build items list
            List<PrescriptionItem> items = new ArrayList<>();
            for (int i = 0; i < itemsTableModel.getRowCount(); i++) {
                PrescriptionItem item = new PrescriptionItem();
                item.setMedicineId((int) itemsTableModel.getValueAt(i, 0));
                item.setQuantity((int) itemsTableModel.getValueAt(i, 1));
                item.setDosage((String) itemsTableModel.getValueAt(i, 2));
                item.setInstructions((String) itemsTableModel.getValueAt(i, 3));
                item.setNotes((String) itemsTableModel.getValueAt(i, 4));
                items.add(item);
            }
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please add at least one prescription item.");
                return;
            }
            p.setItems(items);

            // Add to database
            boolean success = prescriptionDAO.addPrescription(p);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Prescription added successfully.");
                loadPrescriptions();
                AuditLogger.log(currentPharmacist.getUserId(), "Add", "Prescription", p.getPrescriptionId(), null, null);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add prescription.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    cancelBtn.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}

    //helper method 
    private JPanel createLabeledField(String labelText, JComponent field) {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    panel.setBackground(Color.WHITE); // keep consistent background
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(100, 25)); // fixed label width for alignment
    panel.add(label, BorderLayout.WEST);
    panel.add(field, BorderLayout.CENTER);
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // make panel stretch horizontally
    return panel;
}


private void openEditDialog() {
    int selectedRow = prescriptionTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a prescription to edit.");
        return;
    }

    int prescriptionId = (int) prescriptionTableModel.getValueAt(selectedRow, 0);

    try {
        Prescription prescription = prescriptionDAO.getPrescriptionById(prescriptionId);
        if (prescription == null) {
            JOptionPane.showMessageDialog(this, "Prescription not found.");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Prescription", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Main form panel - vertical BoxLayout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Patient combo
        JComboBox<String> patientCombo = new JComboBox<>();
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            patientCombo.addItem(p.getPatientId() + " - " + p.getFullName());
        }
        // Select current patient
        for (int i = 0; i < patientCombo.getItemCount(); i++) {
            if (patientCombo.getItemAt(i).startsWith(prescription.getPatientId() + " -")) {
                patientCombo.setSelectedIndex(i);
                break;
            }
        }
        formPanel.add(createLabeledField("Patient:", patientCombo));

        // Doctor combo
        JComboBox<String> doctorCombo = new JComboBox<>();
        List<Doctor> doctors = doctorDAO.searchDoctors(""); // Load all doctors
        for (Doctor d : doctors) {
            doctorCombo.addItem(d.getDoctorId() + " - " + d.getFullName());
        }
        // Select current doctor
        for (int i = 0; i < doctorCombo.getItemCount(); i++) {
            if (doctorCombo.getItemAt(i).startsWith(prescription.getDoctorId() + " -")) {
                doctorCombo.setSelectedIndex(i);
                break;
            }
        }
        formPanel.add(createLabeledField("Doctor:", doctorCombo));

        // Date picker (using JTextField for simplicity)
        JTextField dateField = new JTextField(new java.text.SimpleDateFormat("yyyy-MM-dd").format(prescription.getPrescriptionDate()));
        formPanel.add(createLabeledField("Date (yyyy-MM-dd):", dateField));

        // Status combo
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"draft", "active", "completed", "cancelled"});
        statusCombo.setSelectedItem(prescription.getStatus());
        formPanel.add(createLabeledField("Status:", statusCombo));

        // Notes text area
        JTextArea notesArea = new JTextArea(prescription.getNotes());
        notesArea.setRows(3);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setBackground(Color.WHITE);
        notesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesPanel.add(notesLabel, BorderLayout.NORTH);
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);
        formPanel.add(notesPanel);

        // Prescription Items Table (with add/edit/delete buttons)
        DefaultTableModel itemsTableModel = new DefaultTableModel(
            new Object[]{"Medicine ID", "Quantity", "Dosage", "Instructions", "Notes"}, 0
        );
        JTable itemsTable = new JTable(itemsTableModel);
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
        itemsScrollPane.setPreferredSize(new Dimension(700, 150));

        // Load existing items into table
    for (PrescriptionItem item : prescription.getItems()) {
    itemsTableModel.addRow(new Object[]{
        item.getMedicineId(),
        item.getQuantity(),
        item.getDosage(),
        item.getInstructions(),
        item.getNotes()
    });
}

        

        // Buttons panel for items
        JPanel itemsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemsButtonPanel.setBackground(Color.WHITE);

        JButton addItemBtn = new JButton("Add Item");
        JButton editItemBtn = new JButton("Edit Item");
        JButton deleteItemBtn = new JButton("Delete Item");

        itemsButtonPanel.add(addItemBtn);
        itemsButtonPanel.add(editItemBtn);
        itemsButtonPanel.add(deleteItemBtn);

        // Add action listeners for items buttons

        addItemBtn.addActionListener(e -> {
            // Simple dialog to add item (medicineId, quantity, dosage, instructions, notes)
            JTextField medIdField = new JTextField();
            JTextField quantityField = new JTextField();
            JTextField dosageField = new JTextField();
            JTextField instructionsField = new JTextField();
            JTextField notesField = new JTextField();

            JPanel addItemPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            addItemPanel.add(new JLabel("Medicine ID:"));
            addItemPanel.add(medIdField);
            addItemPanel.add(new JLabel("Quantity:"));
            addItemPanel.add(quantityField);
            addItemPanel.add(new JLabel("Dosage:"));
            addItemPanel.add(dosageField);
            addItemPanel.add(new JLabel("Instructions:"));
            addItemPanel.add(instructionsField);
            addItemPanel.add(new JLabel("Notes:"));
            addItemPanel.add(notesField);

            int result = JOptionPane.showConfirmDialog(dialog, addItemPanel, "Add Prescription Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int medId = Integer.parseInt(medIdField.getText().trim());
                    int qty = Integer.parseInt(quantityField.getText().trim());
                    String dosage = dosageField.getText().trim();
                    String instr = instructionsField.getText().trim();
                    String notes = notesField.getText().trim();

                    itemsTableModel.addRow(new Object[]{medId, qty, dosage, instr, notes});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Medicine ID and Quantity must be integers.");
                }
            }
        });

        editItemBtn.addActionListener(e -> {
            int selectedItemRow = itemsTable.getSelectedRow();
            if (selectedItemRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an item to edit.");
                return;
            }

            JTextField medIdField = new JTextField(itemsTableModel.getValueAt(selectedItemRow, 0).toString());
            JTextField quantityField = new JTextField(itemsTableModel.getValueAt(selectedItemRow, 1).toString());
            JTextField dosageField = new JTextField(itemsTableModel.getValueAt(selectedItemRow, 2).toString());
            JTextField instructionsField = new JTextField(itemsTableModel.getValueAt(selectedItemRow, 3).toString());
            JTextField notesField = new JTextField(itemsTableModel.getValueAt(selectedItemRow, 4).toString());

            JPanel editItemPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            editItemPanel.add(new JLabel("Medicine ID:"));
            editItemPanel.add(medIdField);
            editItemPanel.add(new JLabel("Quantity:"));
            editItemPanel.add(quantityField);
            editItemPanel.add(new JLabel("Dosage:"));
            editItemPanel.add(dosageField);
            editItemPanel.add(new JLabel("Instructions:"));
            editItemPanel.add(instructionsField);
            editItemPanel.add(new JLabel("Notes:"));
            editItemPanel.add(notesField);

            int result = JOptionPane.showConfirmDialog(dialog, editItemPanel, "Edit Prescription Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int medId = Integer.parseInt(medIdField.getText().trim());
                    int qty = Integer.parseInt(quantityField.getText().trim());
                    String dosage = dosageField.getText().trim();
                    String instr = instructionsField.getText().trim();
                    String notes = notesField.getText().trim();

                    itemsTableModel.setValueAt(medId, selectedItemRow, 0);
                    itemsTableModel.setValueAt(qty, selectedItemRow, 1);
                    itemsTableModel.setValueAt(dosage, selectedItemRow, 2);
                    itemsTableModel.setValueAt(instr, selectedItemRow, 3);
                    itemsTableModel.setValueAt(notes, selectedItemRow, 4);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Medicine ID and Quantity must be integers.");
                }
            }
        });

        deleteItemBtn.addActionListener(e -> {
            int selectedItemRow = itemsTable.getSelectedRow();
            if (selectedItemRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an item to delete.");
                return;
            }
            itemsTableModel.removeRow(selectedItemRow);
        });

        // Items panel combining table and buttons
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Prescription Items"));
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);
        itemsPanel.add(itemsButtonPanel, BorderLayout.SOUTH);

        formPanel.add(itemsPanel);

        // Buttons panel at bottom
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        // Save action
        saveBtn.addActionListener(e -> {
            try {
                // Validate and update prescription fields
                String patientStr = (String) patientCombo.getSelectedItem();
                int patientId = Integer.parseInt(patientStr.split(" - ")[0]);

                String doctorStr = (String) doctorCombo.getSelectedItem();
                int doctorId = Integer.parseInt(doctorStr.split(" - ")[0]);

                java.util.Date presDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText().trim());

                String status = (String) statusCombo.getSelectedItem();
                String notes = notesArea.getText().trim();

                // Update prescription object
                prescription.setPatientId(patientId);
                prescription.setDoctorId(doctorId);
                prescription.setPrescriptionDate(new java.sql.Date(presDate.getTime()));
                prescription.setStatus(status);
                prescription.setNotes(notes);

                // Build prescription items list from table
                List<PrescriptionItem> items = new ArrayList<>();
                for (int i = 0; i < itemsTableModel.getRowCount(); i++) {
                    PrescriptionItem item = new PrescriptionItem();
                    item.setMedicineId(Integer.parseInt(itemsTableModel.getValueAt(i, 0).toString()));
                    item.setQuantity(Integer.parseInt(itemsTableModel.getValueAt(i, 1).toString()));
                    item.setDosage(itemsTableModel.getValueAt(i, 2).toString());
                    item.setInstructions(itemsTableModel.getValueAt(i, 3).toString());
                    item.setNotes(itemsTableModel.getValueAt(i, 4).toString());
                    items.add(item);
                }
                prescription.setItems(items);

                // Save to DB
                boolean updated = prescriptionDAO.updatePrescription(prescription);
                if (updated) {
                    JOptionPane.showMessageDialog(dialog, "Prescription updated successfully.");
                    loadPrescriptions();
                    AuditLogger.log(currentPharmacist.getUserId(), "Edit", "Prescription", prescription.getPrescriptionId(), null, null);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update prescription.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Failed to load prescription for editing: " + e.getMessage());
        e.printStackTrace();
    }
}

  
  public static void main(String[] args) {
    // Optional: Set FlatLaf look and feel if you're using it
    try {
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    // Sample dummy user to pass (replace with real one if needed)
    User dummyPharmacist = new User();
    dummyPharmacist.setUserId(1);
    dummyPharmacist.setUsername("Test Pharmacist");

    // Launch the frame
    SwingUtilities.invokeLater(() -> {
        new PrescriptionManagementFrame(dummyPharmacist).setVisible(true);
    });
}
}

