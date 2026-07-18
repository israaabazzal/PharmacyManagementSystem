package com.pharmacy.views;

import com.pharmacy.dao.ReportDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;



public class ReportsFrame extends JFrame {
    private final ReportDAO reportDAO = new ReportDAO();
    private JPanel mainPanel;

    // Optional: Gradient color scheme
    private static final Color[] GRADIENT_COLORS = {
        new Color(245, 249, 255),
        new Color(220, 235, 250),
        new Color(200, 220, 240)
    };

    public ReportsFrame() {
        setTitle("Pharmacy Reports");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gradient background main panel
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
        setContentPane(mainPanel);

        // Title
        JLabel titleLabel = new JLabel("Pharmacy Summary Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Report cards grid
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setOpaque(false); // Allow gradient background to show through
        
        JPanel chartPanel = createLowStockPieChart();
        chartPanel.setPreferredSize(new Dimension(300, 300));
        mainPanel.add(chartPanel, BorderLayout.SOUTH);

        

        try {
            cardsPanel.add(createReportCard("Total Users", String.valueOf(reportDAO.getUserCount())));
            cardsPanel.add(createReportCard("Total Medicines", String.valueOf(reportDAO.getMedicineCount())));
            cardsPanel.add(createReportCard("Total Prescriptions", String.valueOf(reportDAO.getPrescriptionCount())));
            cardsPanel.add(createReportCard("Total Sales ($)", String.format("%.2f", reportDAO.getTotalSales())));
            cardsPanel.add(createReportCard("Low Stock (< 10)", String.valueOf(reportDAO.getLowStockCount(10))));
            cardsPanel.add(createReportCard("Expiring Soon (30d)", String.valueOf(reportDAO.getExpiringSoonCount(30))));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load report data:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createReportCard(String title, String value) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        valueLabel.setForeground(new Color(44, 135, 230));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
    
    


private JPanel createLowStockPieChart() {
    DefaultPieDataset dataset = new DefaultPieDataset();

    try {
        int lowStockCount = reportDAO.getLowStockCount(10);
        int totalMedicines = reportDAO.getMedicineCount();
        int normalStock = totalMedicines - lowStockCount;

        dataset.setValue("Low Stock", lowStockCount);
        dataset.setValue("Normal Stock", normalStock);

    } catch (SQLException e) {
        e.printStackTrace();
        dataset.setValue("Error", 1);
    }

    JFreeChart chart = ChartFactory.createPieChart(
        "Medicine Stock Status",
        dataset,
        true,  // legend
        true,  // tooltips
        false  // URLs
    );
    // Customize section colors
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setSectionPaint("Low Stock", new Color(220, 240, 250));     // blue  
    plot.setSectionPaint("Normal Stock", new Color(200, 200, 230)); // lavender 

    // Optional: Improve look
    plot.setBackgroundPaint(Color.WHITE);
    plot.setOutlineVisible(false);
    plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
    plot.setLabelPaint(new Color(50, 50, 50));
    plot.setShadowPaint(null);
    return new ChartPanel(chart);
}

    
}
