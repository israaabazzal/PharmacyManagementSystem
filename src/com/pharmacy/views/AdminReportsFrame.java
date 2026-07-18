package com.pharmacy.views;

import com.pharmacy.dao.ReportDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class AdminReportsFrame extends JFrame {

    private ReportDAO dao;
    private JPanel mainPanel;
    private JTable activityTable;
    private DefaultTableModel tableModel;

    
    // Modern color scheme matching login form
    private final Color[] GRADIENT_COLORS = {
        new Color(235, 245, 255),//light blue
        new Color(230, 230, 250), // lavender
        new Color(230, 230, 250)// sky blue
    };

    public AdminReportsFrame() {
        setTitle("Pharmacy System Reports");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        try {
            dao = new ReportDAO();
            initComponents();
            loadReportData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing reports: " + e.getMessage());
        }
    }

    private void initComponents() {
        // Main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(0, getHeight());
                GradientPaint gp = new GradientPaint(
                    start, GRADIENT_COLORS[0], 
                    end, GRADIENT_COLORS[2]
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("System Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadReportData());
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Stats cards panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create stat cards (will be populated in loadReportData())
        for (int i = 0; i < 6; i++) {
            statsPanel.add(createStatCardPlaceholder());
        }
        
        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Recent activity panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setOpaque(false);
        activityPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Recent Activity"
        ));
        
        // activity table
String[] columns = {"Timestamp", "User", "Action", "Details"};
tableModel = new DefaultTableModel(columns, 0); // initially empty
activityTable = new JTable(tableModel);

        
    
        activityTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activityTable.setRowHeight(25);
        activityTable.setShowGrid(false);
        activityTable.setIntercellSpacing(new Dimension(0, 5));
        
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(activityPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createStatCardPlaceholder() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Loading...");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(80, 80, 80));
        
        JLabel value = new JLabel("--");
        value.setFont(new Font("Segoe UI", Font.BOLD, 24));
        value.setForeground(new Color(50, 50, 50));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        
        return card;
    }

    private void loadReportData() {
        try {
            // Get data from DAO
            int userCount = dao.getUserCount();
            int medicineCount = dao.getMedicineCount();
            int prescriptionCount = dao.getPrescriptionCount();
            double totalSales = dao.getTotalSales();
            int lowStockCount = dao.getLowStockCount(20);
            int expiringSoonCount = dao.getExpiringSoonCount(30);
            
            // Update stat cards
            updateStatCard(0, "Total Users", String.valueOf(userCount));
            updateStatCard(1, "Medicines", String.valueOf(medicineCount));
            updateStatCard(2, "Prescriptions", String.valueOf(prescriptionCount));
            updateStatCard(3, "Total Sales", NumberFormat.getCurrencyInstance().format(totalSales));
            updateStatCard(4, "Low Stock Items", String.valueOf(lowStockCount));
            updateStatCard(5, "Expiring Soon", String.valueOf(expiringSoonCount));
            
            
            tableModel.setRowCount(0); // clear old data
     for (String[] row : dao.getRecentActivities(20)) {
    tableModel.addRow(row);
}

            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading report data: " + e.getMessage());
        }
    }
    
    private void updateStatCard(int index, String title, String value) {
        JPanel statsPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        JPanel card = (JPanel) statsPanel.getComponent(index);
        
        ((JLabel) card.getComponent(0)).setText(title);
        ((JLabel) card.getComponent(1)).setText(value);
        
        // Set appropriate color based on data
        if (title.contains("Low Stock") && Integer.parseInt(value) > 0) {
            card.setBackground(new Color(255, 230, 230, 200)); // Light red
        } else if (title.contains("Expiring") && Integer.parseInt(value) > 0) {
            card.setBackground(new Color(255, 255, 200, 200)); // Light yellow
        } else {
            card.setBackground(new Color(255, 255, 255, 200)); // Default white
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminReportsFrame().setVisible(true));
    }
}