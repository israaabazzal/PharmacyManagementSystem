package com.pharmacy.views;

import com.formdev.flatlaf.FlatLightLaf;
import com.pharmacy.models.User;
import com.pharmacy.views.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

  public class PharmacistDashboard extends JFrame {
    private User currentUser;
    

    public PharmacistDashboard(User currentUser) {
        
        
                      try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        
        
        
        this.currentUser = currentUser;

        setTitle("Pharmacist Dashboard");
        setSize(700, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        initComponents();
    }

    private void initComponents() {
        // Gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color top = new Color(240, 240, 255);
                Color bottom = new Color(220, 240, 250);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new GridBagLayout());

        // === Card container ===
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(700, 800));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        ));
        
        
        //title
// Load and scale the icon
ImageIcon rawIcon = new ImageIcon(getClass().getResource("/assets/motivation.png"));
Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
ImageIcon userIcon = new ImageIcon(scaledImage);

// Create the label with icon and welcome text
JLabel titleLabel = new JLabel(" Welcome " + currentUser.getUsername(), userIcon, JLabel.CENTER);
titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
titleLabel.setIconTextGap(10); // spacing between icon and text


        card.add(titleLabel);
        card.add(Box.createVerticalStrut(40));
          


        // Action buttons in cards
card.add(createDashboardCard("Inventory", "Manage medicine stock", "/assets/box.png", 
    () -> new InventoryFrame(currentUser).setVisible(true)));

card.add(createDashboardCard("Prescription", "Manage Prescriptions", "/assets/prescription.png", 
    () -> new PrescriptionManagementFrame(currentUser).setVisible(true)));

card.add(createDashboardCard("Patients", "Manage patient records", "/assets/patient.png", 
    () -> new PatientManagementFrame(currentUser).setVisible(true)));

card.add(createDashboardCard("Doctors", "Manage doctors directory", "/assets/doctor.png", 
        
    () -> new DoctorManagementFrame(currentUser).setVisible(true)));

 card.add(createDashboardCard("Process Sale", "Sell medicines to customers", "/assets/sales.png",
                () -> new SalesFrame(currentUser).setVisible(true)));

card.add(createDashboardCard("Reports", "Inventory & sales reports", "/assets/report.png", 
    () -> new ReportsFrame().setVisible(true)
));




        // Logout button with icon
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
       logoutBtn.setMaximumSize(new Dimension(280, 30));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(Color.BLACK);
       logoutBtn.setFont(new Font("Arial", Font.PLAIN, 16)); // 👈 Increase font size here

        //  Add icon
        try {
            ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/assets/logout.png"));
            Image scaled = logoutIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            logoutBtn.setIcon(new ImageIcon(scaled));
            logoutBtn.setIconTextGap(10);
        } catch (Exception e) {
            System.err.println("Logout icon not found.");
        }

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });

        card.add(Box.createVerticalStrut(45));
        card.add(logoutBtn);

        gradientPanel.add(card);
        getContentPane().add(gradientPanel);
        
        
        
         // date and time label
        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setForeground(Color.GRAY);
        
        
        card.add(Box.createVerticalStrut(25));
        card.add(dateLabel);
        
        
    } 

    // date function  outside initComponent inside constructor 
    
    private String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return now.format(formatter);
    } 
    
  
    
    
private JPanel createDashboardCard(String title, String description, String iconPath, Runnable action) {
    JPanel panel = new RoundedPanel(20);
    panel.setLayout(new BorderLayout());
    panel.setBackground(new Color(248, 248, 248));

    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));

    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
    panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Load and scale icon
    JLabel titleLabel;
    if (iconPath != null) {
        try {
            ImageIcon rawIcon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);
            titleLabel = new JLabel(title, icon, JLabel.LEFT);
            titleLabel.setIconTextGap(15);
        } catch (Exception e) {
            System.err.println("Icon not found for " + title + ": " + iconPath);
            titleLabel = new JLabel(title);
        }
    } else {
        titleLabel = new JLabel(title);
    }

    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

    JLabel descLabel = new JLabel("<html><div style='font-size:12px;'>" + description + "</div></html>");
    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.setOpaque(false);
    textPanel.add(titleLabel);
    textPanel.add(Box.createVerticalStrut(6));
    textPanel.add(descLabel);

    panel.add(textPanel, BorderLayout.CENTER);

    // Optional: visual shadow effect
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 4, new Color(240, 240, 255)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));

    // Mouse click
    panel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            action.run();
        }
    });

    return panel;
}



    public static void main(String[] args) { 
        // For testing : to be removed later 
        User dummy = new User();
        dummy.setUsername("PharmaUser");
        dummy.setRoleId(2);
        SwingUtilities.invokeLater(() -> new PharmacistDashboard(dummy).setVisible(true));
    }
    
    

    
    
    
}
