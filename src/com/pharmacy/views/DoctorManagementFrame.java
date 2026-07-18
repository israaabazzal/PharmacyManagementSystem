package com.pharmacy.views;

import com.pharmacy.dao.AuditLogger;
import com.pharmacy.dao.DoctorDAO;
import com.pharmacy.models.Doctor;
import com.pharmacy.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class DoctorManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private DoctorDAO doctorDAO;
    private User currentPharmacist;
    private JPanel mainPanel;

    public DoctorManagementFrame(User currentPharmacist) {
        this.currentPharmacist = currentPharmacist;
        this.doctorDAO = new DoctorDAO();
        setTitle("Doctor Management");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        refreshTable();
    }

    private void initUI() {
        
        
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
        
        
        

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Specialization", "Phone", "Email", "License"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(refreshBtn);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Listeners
        addBtn.addActionListener(e -> showDoctorDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int doctorId = (int) tableModel.getValueAt(row, 0);
                Doctor doctor = getDoctorFromRow(row);
                showDoctorDialog(doctor);
            } else {
                showError("Select a doctor to edit.");
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int doctorId = (int) tableModel.getValueAt(row, 0);
                String doctorName = (String) tableModel.getValueAt(row, 1);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete " + doctorName + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        doctorDAO.deleteDoctor(doctorId);
                        AuditLogger.log(currentPharmacist.getUserId(), "Delete", "Doctor", doctorId, doctorName, null);
                        refreshTable();
                    } catch (Exception ex) {
                        showError("Delete failed: " + ex.getMessage());
                    }
                }
            } else {
                showError("Select a doctor to delete.");
            }
        });

        searchBtn.addActionListener(e -> searchDoctors());
        refreshBtn.addActionListener(e -> refreshTable());

        setContentPane(mainPanel);
    }
    
    
    
private void showDoctorDialog(Doctor doctor) {
    JDialog dialog = new JDialog(this, doctor == null ? "Add Doctor" : "Edit Doctor", true);
    dialog.setSize(500, 500);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // Fields
    JTextField nameField = new JTextField();
    JTextField specializationField = new JTextField();
    JTextField phoneField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField licenseField = new JTextField();

    if (doctor != null) {
        nameField.setText(doctor.getFullName());
        specializationField.setText(doctor.getSpecialization());
        phoneField.setText(doctor.getPhone());
        emailField.setText(doctor.getEmail());
        licenseField.setText(doctor.getLicenseNumber());
    }

    // Form Panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    formPanel.add(createLabeledField("Full Name:", nameField));
    formPanel.add(createLabeledField("Specialization:", specializationField));
    formPanel.add(createLabeledField("Phone:", phoneField));
    formPanel.add(createLabeledField("Email:", emailField));
    formPanel.add(createLabeledField("License #:", licenseField));

    // Button Panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);
    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");

    buttonPanel.add(saveBtn);
    buttonPanel.add(cancelBtn);

    // Action
    saveBtn.addActionListener(e -> {
        String name = nameField.getText().trim();
        String spec = specializationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String license = licenseField.getText().trim();

        if (name.isEmpty() || spec.isEmpty()) {
            showError("Name and specialization are required.");
            return;
        }

        try {
            if (doctor == null) {
                Doctor newDoc = new Doctor();
                newDoc.setFullName(name);
                newDoc.setSpecialization(spec);
                newDoc.setPhone(phone);
                newDoc.setEmail(email);
                newDoc.setLicenseNumber(license);

                if (doctorDAO.addDoctor(newDoc)) {
                    AuditLogger.log(currentPharmacist.getUserId(), "Add", "Doctor", 0, null, name);
                    refreshTable();
                    dialog.dispose();
                } else {
                    showError("Failed to add doctor.");
                }
            } else {
                String oldVal = doctor.getFullName();
                doctor.setFullName(name);
                doctor.setSpecialization(spec);
                doctor.setPhone(phone);
                doctor.setEmail(email);
                doctor.setLicenseNumber(license);

                if (doctorDAO.updateDoctor(doctor)) {
                    AuditLogger.log(currentPharmacist.getUserId(), "Update", "Doctor", doctor.getDoctorId(), oldVal, name);
                    refreshTable();
                    dialog.dispose();
                } else {
                    showError("Update failed.");
                }
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    });

    cancelBtn.addActionListener(e -> dialog.dispose());

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
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



    private Doctor getDoctorFromRow(int row) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId((int) tableModel.getValueAt(row, 0));
        doctor.setFullName((String) tableModel.getValueAt(row, 1));
        doctor.setSpecialization((String) tableModel.getValueAt(row, 2));
        doctor.setPhone((String) tableModel.getValueAt(row, 3));
        doctor.setEmail((String) tableModel.getValueAt(row, 4));
        doctor.setLicenseNumber((String) tableModel.getValueAt(row, 5));
        return doctor;
    }
    
    
    
    private void refreshTable() {
        try {
            List<Doctor> doctors = doctorDAO.searchDoctors("");
            tableModel.setRowCount(0);
            for (Doctor d : doctors) {
                tableModel.addRow(new Object[]{
                        d.getDoctorId(), d.getFullName(), d.getSpecialization(),
                        d.getPhone(), d.getEmail(), d.getLicenseNumber()
                });
            }
        } catch (Exception e) {
            showError("Failed to load doctors: " + e.getMessage());
        }
    }

    private void searchDoctors() {
        String keyword = JOptionPane.showInputDialog(this, "Enter name, license, or ID to search:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                List<Doctor> results = doctorDAO.searchDoctors(keyword.trim());
                tableModel.setRowCount(0);
                for (Doctor d : results) {
                    tableModel.addRow(new Object[]{
                            d.getDoctorId(), d.getFullName(), d.getSpecialization(),
                            d.getPhone(), d.getEmail(), d.getLicenseNumber()
                    });
                }
            } catch (Exception e) {
                showError("Search failed: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
