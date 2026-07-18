package com.pharmacy.views;

import com.pharmacy.models.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminDashboard extends JFrame {
    private User currentUser;

    public AdminDashboard(User currentUser) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.currentUser = currentUser;
        initComponents();
        setTitle("Admin Dashboard" );
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
    }

    private void initComponents() {
        // Gradient background panel
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(240, 240, 255); // light 
                Color color2 = new Color(220, 240, 250); // light blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new GridBagLayout());

        // Card panel with white background and padding
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(700, 800));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Title
// Load and scale the icon
ImageIcon rawIcon = new ImageIcon(getClass().getResource("/assets/motivation.png"));
Image scaledImage = rawIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
ImageIcon userIcon = new ImageIcon(scaledImage);

// Create the label with icon and welcome text
JLabel titleLabel = new JLabel(" Welcome " + currentUser.getUsername(), userIcon, JLabel.CENTER);
titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
titleLabel.setIconTextGap(10); // spacing between icon and text


        // Buttons
        JButton manageUsersBtn = createStyledButton("Manage Users");
        JButton backupBtn = createStyledButton("Backup Database");
        JButton reportsBtn = createStyledButton("View Reports");
        
        //logout button with icon 
        JButton logoutBtn = createStyledButton("Logout");
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/assets/logout.png"));
        logoutBtn.setIcon(logoutIcon);
        // resize if icon big 
        Image scaled = logoutIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
         logoutBtn.setIcon(new ImageIcon(scaled));

        // Date label
        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setForeground(Color.GRAY);

        // Actions
        manageUsersBtn.addActionListener(e -> new UserManagementFrame(currentUser).setVisible(true));
        backupBtn.addActionListener(e -> performDatabaseBackup());
        reportsBtn.addActionListener(e -> new AdminReportsFrame().setVisible(true));
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        
        
card.setLayout(new BorderLayout());

// ====== Top (Title with space) ======
JPanel topPanel = new JPanel(new BorderLayout());
topPanel.setOpaque(false); // transparent if using background/gradient
topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // Top padding (30px)

titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
topPanel.add(titleLabel, BorderLayout.CENTER);

card.add(topPanel, BorderLayout.NORTH);

// ====== Bottom (Date Label) ======
dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
card.add(dateLabel, BorderLayout.SOUTH);

// ====== Center (Button Group) ======
JPanel centerPanel = new JPanel(new GridBagLayout());
centerPanel.setOpaque(false);

GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.fill = GridBagConstraints.NONE;
gbc.anchor = GridBagConstraints.CENTER;
gbc.insets = new Insets(10, 20, 10, 20);

// Vertical stack of buttons
JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
buttonPanel.setOpaque(false);

Dimension buttonSize = new Dimension(230, 55);
manageUsersBtn.setPreferredSize(buttonSize);
backupBtn.setPreferredSize(buttonSize);
reportsBtn.setPreferredSize(buttonSize);
logoutBtn.setPreferredSize(buttonSize);

buttonPanel.add(manageUsersBtn);
buttonPanel.add(backupBtn);
buttonPanel.add(reportsBtn);

// Logout with extra top space
JPanel logoutWrapper = new JPanel(new BorderLayout());
logoutWrapper.setOpaque(false);
logoutWrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // Extra space before logout
logoutWrapper.add(logoutBtn, BorderLayout.CENTER);

buttonPanel.add(logoutWrapper);

// Add button panel centered
centerPanel.add(buttonPanel, gbc);
card.add(centerPanel, BorderLayout.CENTER);




        // Center card on gradient background
        gradientPanel.add(card, new GridBagConstraints());

        getContentPane().add(gradientPanel);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 55));
        btn.setPreferredSize(new Dimension(300, 55));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return btn;
    }

    private String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return now.format(formatter);
    }

    private void performDatabaseBackup() {
        try {
            String backupPath = "backup.sql"; // You can choose any path
            String user = "root";             // Use your MySQL username
            String password = "israa098";     // Your MySQL password
            String dbName = "pharmacy_management_system";

            String command = String.format(
                    "mysqldump -u%s -p%s --databases %s -r %s",
                    user, password, dbName, backupPath
            );

            Process process = Runtime.getRuntime().exec(command);
            int processComplete = process.waitFor();

            if (processComplete == 0) {
                JOptionPane.showMessageDialog(this, "Backup created successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Backup failed!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}