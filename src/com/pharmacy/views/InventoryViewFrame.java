package com.pharmacy.views;

import com.formdev.flatlaf.FlatLightLaf;
import com.pharmacy.dao.MedicineDAO;
import com.pharmacy.models.Medicine;
import com.pharmacy.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.List;

public class InventoryViewFrame extends JFrame {
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private User currentUser;
    private JPanel mainPanel;

    private final Color[] GRADIENT_COLORS = {
        new Color(230, 245, 255),
        new Color(220, 230, 250),
        new Color(240, 230, 250)
    };

    public InventoryViewFrame(User currentUser) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.currentUser = currentUser;
        setTitle("Inventory Viewer");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadMedicineData();
    }

    private void initComponents() {
        // Gradient background panel
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(0, getHeight());
                GradientPaint gp = new GradientPaint(start, GRADIENT_COLORS[0], end, GRADIENT_COLORS[2]);

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel with Refresh Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        JButton refreshBtn = new JButton("Refresh");
        topPanel.add(refreshBtn);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel();
        medicineTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(medicineTable);
        tableModel.setColumnIdentifiers(new String[]{
            "ID", "Name", "Description", "Category", "Batch", "Expiry", "Qty", "Unit Price", "Selling Price"
        });

        mainPanel.add(tableScroll, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Refresh Button Action
        refreshBtn.addActionListener(e -> loadMedicineData());
    }

    private void loadMedicineData() {
        tableModel.setRowCount(0);  // Clear old data
        MedicineDAO dao = new MedicineDAO();
        List<Medicine> medicines = dao.getAllMedicines();
        for (Medicine m : medicines) {
            tableModel.addRow(new Object[]{
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getCategoryName(),
                m.getBatchNumber(),
                m.getExpiryDate(),
                m.getQuantity(),
                m.getUnitPrice(),
                m.getSellingPrice()
            });
        }
    }
}

