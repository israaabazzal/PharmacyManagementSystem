package com.pharmacy.views;

import com.pharmacy.dao.*;
import com.pharmacy.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.List;

public class PrescriptionViewFrame extends JFrame {
    private User currentUser;

    private JTable prescriptionTable;
    private JTable itemTable;
    private DefaultTableModel prescriptionTableModel;
    private DefaultTableModel itemTableModel;

    private PrescriptionDAO prescriptionDAO;
    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;
    private MedicineDAO medicineDAO;
    private JPanel mainPanel;

    public PrescriptionViewFrame(User currentUser) {
        this.currentUser = currentUser;
        this.prescriptionDAO = new PrescriptionDAO();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
        this.medicineDAO = new MedicineDAO();

        setTitle("View Prescriptions");
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

        // Top Panel (Only Refresh & Search)
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnSearch = new JButton("Search");
        topButtonPanel.add(btnRefresh);
        topButtonPanel.add(btnSearch);

        // Prescription table
        prescriptionTableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date", "Status", "Notes"}, 0);
        prescriptionTable = new JTable(prescriptionTableModel);
        JScrollPane prescriptionScroll = new JScrollPane(prescriptionTable);

        // Item table
        itemTableModel = new DefaultTableModel(new Object[]{"Medicine", "Quantity", "Dosage"}, 0);
        itemTable = new JTable(itemTableModel);
        JScrollPane itemScroll = new JScrollPane(itemTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, prescriptionScroll, itemScroll);
        splitPane.setResizeWeight(0.7);

        mainPanel.add(topButtonPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);

        // Listeners
        btnRefresh.addActionListener(e -> loadPrescriptions());
        btnSearch.addActionListener(e -> searchPrescriptions());
        prescriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPrescriptionItems();
        });
    }

    private void loadPrescriptions() {
        try {
            prescriptionTableModel.setRowCount(0);
            List<Prescription> prescriptions = prescriptionDAO.searchPrescriptions("");
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
            itemTableModel.setRowCount(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }
}
