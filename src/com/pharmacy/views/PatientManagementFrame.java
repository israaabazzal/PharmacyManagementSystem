package com.pharmacy.views;

import com.pharmacy.dao.AuditLogger;
import com.pharmacy.dao.PatientDAO;
import com.pharmacy.models.Patient;
import com.pharmacy.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientManagementFrame extends JFrame {
    private PatientDAO patientDAO;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private User currentPharmacist; // or your current user object with getUserId()
    private JPanel mainPanel;
    
    // Constructor: pass current user for audit logging
    public PatientManagementFrame(User currentPharmacist) {
        this.currentPharmacist = currentPharmacist;
        patientDAO = new PatientDAO();

        setTitle("Patient Management");
         setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadPatients();
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
        getContentPane().add(mainPanel);

        // Table columns
        String[] columns = {"ID", "Full Name", "DOB", "Gender", "Address", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Add Patient");
        JButton btnEdit = new JButton("Edit Patient");
        JButton btnDelete = new JButton("Delete Patient");
        JButton btnSearch = new JButton("Search");

        btnAdd.addActionListener(e -> openAddPatientDialog());
        btnEdit.addActionListener(e -> openEditPatientDialog());
        btnDelete.addActionListener(e -> deletePatient());
        btnSearch.addActionListener(e -> searchPatients());

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);

        mainPanel.add(btnPanel, BorderLayout.NORTH);
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            refreshTable(patients);
        } catch (Exception e) {
            showError("Error loading patients: " + e.getMessage());
        }
    }

    private void refreshTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Patient p : patients) {
            String dob = p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "";
            tableModel.addRow(new Object[]{
                    p.getPatientId(),
                    p.getFullName(),
                    dob,
                    p.getGender(),
                    p.getAddress(),
                    p.getPhone()
            });
        }
    }

  private void openAddPatientDialog() {
    JDialog dialog = new JDialog(this, "Add Patient", true);
    dialog.setSize(500, 550);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // Create form fields
    JTextField nameField = new JTextField();
    JTextField dobField = new JTextField();  // Format: YYYY-MM-DD
    JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
    JTextArea addressArea = new JTextArea(3, 20);
    JTextField phoneField = new JTextField();

    // Form panel using BoxLayout
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    formPanel.add(createLabeledField("Full Name:", nameField));
    formPanel.add(createLabeledField("Date of Birth (YYYY-MM-DD):", dobField));

    // Gender
    JPanel genderPanel = new JPanel(new BorderLayout(5, 5));
    genderPanel.setBackground(Color.WHITE);
    JLabel genderLabel = new JLabel("Gender:");
    genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    genderCombo.setPreferredSize(new Dimension(250, 30));
    genderPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    genderPanel.add(genderLabel, BorderLayout.NORTH);
    genderPanel.add(genderCombo, BorderLayout.CENTER);
    formPanel.add(genderPanel);

    // Address
    JPanel addressPanel = new JPanel(new BorderLayout(5, 5));
    addressPanel.setBackground(Color.WHITE);
    JLabel addressLabel = new JLabel("Address:");
    addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addressArea.setLineWrap(true);
    addressArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(addressArea);
    scrollPane.setPreferredSize(new Dimension(250, 60));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    addressPanel.add(addressLabel, BorderLayout.NORTH);
    addressPanel.add(scrollPane, BorderLayout.CENTER);
    formPanel.add(addressPanel);

    formPanel.add(createLabeledField("Phone:", phoneField));

    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);

    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");
    saveBtn.setBackground(Color.WHITE);
    cancelBtn.setBackground(Color.WHITE);
    buttonPanel.add(saveBtn);
    buttonPanel.add(cancelBtn);

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Action listeners
    saveBtn.addActionListener(e -> {
        String name = nameField.getText().trim();
        String dobStr = dobField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Full Name is required.");
            return;
        }

        Patient newPatient = new Patient();
        newPatient.setFullName(name);

        if (!dobStr.isEmpty()) {
            try {
                newPatient.setDateOfBirth(java.sql.Date.valueOf(dobStr).toLocalDate());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.");
                return;
            }
        }

        newPatient.setGender(gender);
        newPatient.setAddress(address);
        newPatient.setPhone(phone);
        newPatient.setCreatedBy(currentPharmacist.getUserId());

        try {
            patientDAO.addPatient(newPatient);

            // Log and refresh
            String newVals = "fullName=" + name + "; dob=" + dobStr + "; gender=" + gender +
                    "; address=" + address + "; phone=" + phone;
            AuditLogger.log(currentPharmacist.getUserId(), "Add", "Patient", newPatient.getPatientId(), null, newVals);

            JOptionPane.showMessageDialog(dialog, "Patient added successfully.");
            dialog.dispose();
            loadPatients();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Failed to add patient: " + ex.getMessage());
            ex.printStackTrace();
        }
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


  private void openEditPatientDialog() {
    int selectedRow = patientTable.getSelectedRow();
    if (selectedRow < 0) {
        showError("Please select a patient to edit.");
        return;
    }

    int patientId = (int) tableModel.getValueAt(selectedRow, 0);

    try {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient == null) {
            showError("Selected patient not found.");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Patient", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Fields
        JTextField nameField = new JTextField(patient.getFullName());
        JTextField dobField = new JTextField(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "");
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setSelectedItem(patient.getGender());
        JTextArea addressArea = new JTextArea(patient.getAddress(), 3, 20);
        JTextField phoneField = new JTextField(patient.getPhone());

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(createLabeledField("Full Name:", nameField));
        formPanel.add(createLabeledField("Date of Birth (YYYY-MM-DD):", dobField));

        // Gender Panel
        JPanel genderPanel = new JPanel(new BorderLayout(5, 5));
        genderPanel.setBackground(Color.WHITE);
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCombo.setPreferredSize(new Dimension(250, 30));
        genderPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        genderPanel.add(genderLabel, BorderLayout.NORTH);
        genderPanel.add(genderCombo, BorderLayout.CENTER);
        formPanel.add(genderPanel);

        // Address Panel
        JPanel addressPanel = new JPanel(new BorderLayout(5, 5));
        addressPanel.setBackground(Color.WHITE);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(addressArea);
        scrollPane.setPreferredSize(new Dimension(250, 60));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(scrollPane, BorderLayout.CENTER);
        formPanel.add(addressPanel);

        formPanel.add(createLabeledField("Phone:", phoneField));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.setBackground(Color.WHITE);
        cancelBtn.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Save action
        saveBtn.addActionListener(ev -> {
            String name = nameField.getText().trim();
            String dobStr = dobField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String address = addressArea.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty()) {
                showError("Full Name is required.");
                return;
            }

            Patient updated = new Patient();
            updated.setPatientId(patient.getPatientId());
            updated.setFullName(name);
            updated.setCreatedBy(patient.getCreatedBy());

            if (!dobStr.isEmpty()) {
                try {
                    updated.setDateOfBirth(java.sql.Date.valueOf(dobStr).toLocalDate());
                } catch (Exception ex) {
                    showError("Invalid date format. Use YYYY-MM-DD.");
                    return;
                }
            }

            updated.setGender(gender);
            updated.setAddress(address);
            updated.setPhone(phone);

            try {
                String oldVals = "fullName=" + patient.getFullName() +
                        "; dob=" + (patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "") +
                        "; gender=" + patient.getGender() +
                        "; address=" + patient.getAddress() +
                        "; phone=" + patient.getPhone();

                String newVals = "fullName=" + name +
                        "; dob=" + dobStr +
                        "; gender=" + gender +
                        "; address=" + address +
                        "; phone=" + phone;

                patientDAO.updatePatient(updated);
                AuditLogger.log(currentPharmacist.getUserId(), "Updated patient", "Patient", updated.getPatientId(), oldVals, newVals);

                showInfo("Patient updated.");
                dialog.dispose();
                loadPatients();
            } catch (Exception ex) {
                showError("Error updating patient: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    } catch (Exception ex) {
        showError("Error loading patient: " + ex.getMessage());
    }
}

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select a patient to delete.");
            return;
        }

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected patient?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Get patient before deletion for audit
            Patient patient = patientDAO.getPatientById(patientId);
            if (patient == null) {
                showError("Selected patient not found.");
                return;
            }

            // Compose old values for audit
            String oldVals = "fullName=" + patient.getFullName() + "; dob=" + (patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "") +
                    "; gender=" + patient.getGender() + "; address=" + patient.getAddress() + "; phone=" + patient.getPhone();

            patientDAO.deletePatient(patientId);

            AuditLogger.log(currentPharmacist.getUserId(), "Deleted patient", "Patient", patientId, oldVals, null);

            showInfo("Patient deleted.");
            loadPatients();
        } catch (Exception ex) {
            showError("Error deleting patient: " + ex.getMessage());
        }
    }

    private void searchPatients() {
        String name = JOptionPane.showInputDialog(this, "Enter name or ID to search:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                List<Patient> results = patientDAO.findPatients(name.trim());
                refreshTable(results);
            } catch (Exception e) {
                showError("Error searching: " + e.getMessage());
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
