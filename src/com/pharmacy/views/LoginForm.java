package com.pharmacy.views;

import com.formdev.flatlaf.FlatLightLaf;
import com.pharmacy.dao.UserDAO;
import com.pharmacy.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginForm() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // ✅ Set window icon (top-left icon)
        setIconImage(new ImageIcon(getClass().getResource("/assets/pharmacy.png")).getImage());

        initComponents();
        setTitle("Pharmacy Management System - Login");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
    }

    private void initComponents() {
        
JPanel gradientPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw gradient background
        Color color1 = new Color(240, 240, 255);
        Color color2 = new Color(220, 240, 250);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Load the image
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/back.png"));
        Image image = icon.getImage();

        // Set 80% transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

        // Draw the image stretched to fill the entire panel
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        // Reset opacity
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
};
        gradientPanel.setLayout(new GridBagLayout());


        // Card panel
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(500, 600));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        ));

        // ✅ Add icon inside card
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/pharmacy.png"));
        Image scaled = icon.getImage().getScaledInstance(90,90, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Pharmacy Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Username Label
JLabel usernameLabel = new JLabel("Username:");
usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Username Panel (icon + field)
ImageIcon userIcon = new ImageIcon(getClass().getResource("/assets/login-avatar.png"));
JLabel userIconLabel = new JLabel(new ImageIcon(userIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));

usernameField = new JTextField();
usernameField.setPreferredSize(new Dimension(350, 35));
usernameField.setHorizontalAlignment(JTextField.LEFT);

JPanel usernameInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
usernameInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
usernameInputPanel.setBackground(Color.WHITE);
usernameInputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
usernameInputPanel.add(userIconLabel);
usernameInputPanel.add(usernameField);

// Password Label
JLabel passwordLabel = new JLabel("Password:");
passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Password Panel (icon + field)
ImageIcon lockIcon = new ImageIcon(getClass().getResource("/assets/password.png"));
JLabel lockIconLabel = new JLabel(new ImageIcon(lockIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));

passwordField = new JPasswordField();
passwordField.setPreferredSize(new Dimension(350, 35));
passwordField.setHorizontalAlignment(JTextField.LEFT);

JPanel passwordInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
passwordInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
passwordInputPanel.setBackground(Color.WHITE);
passwordInputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
passwordInputPanel.add(lockIconLabel);
passwordInputPanel.add(passwordField);
        // Error message
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login Button
        JButton loginBtn = new JButton("Login");
    
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(280, 30));
        loginBtn.addActionListener(e -> loginBtnActionPerformed());
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/assets/enter.png"));
        loginBtn.setIcon(new ImageIcon(loginIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        getRootPane().setDefaultButton(loginBtn);

        // Assemble card layout
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(27));// for space below the icon
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(25));
        
        card.add(iconLabel);
card.add(Box.createVerticalStrut(15));
card.add(titleLabel);
card.add(Box.createVerticalStrut(30));

card.add(usernameLabel);
card.add(usernameInputPanel);
card.add(Box.createVerticalStrut(25));

card.add(passwordLabel);
card.add(passwordInputPanel);
card.add(Box.createVerticalStrut(18));

card.add(loginBtn);
card.add(Box.createVerticalStrut(10));
card.add(errorLabel);
card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(errorLabel);
        card.add(Box.createVerticalGlue());

        // Center card on gradient background
        gradientPanel.add(card, new GridBagConstraints());

        getContentPane().add(gradientPanel);

    }

private void loginBtnActionPerformed() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword());

    if (username.isEmpty() || password.isEmpty()) {
        showError("Please enter both username and password.");
        return;
    }

    try {
        User user = new UserDAO().authenticate(username, password);

        if (user != null) {
            // Check if the user is active
            if (!user.isActive()) {
                showError("Your account is inactive. Please contact the administrator.");
                return; // Stop login
            }

            // Active user → redirect to dashboard
            redirectToDashboard(user);
            dispose();
        } else {
            // This includes invalid credentials or inactive users (since authenticate() filters by active)
            showError("Invalid username or password");
        }
    } catch (SQLException ex) {
        showError("Database error: " + ex.getMessage());
    }
}

    private void redirectToDashboard(User user) {
        switch (user.getRoleId()) {
            case 1:
                new AdminDashboard(user).setVisible(true);
                break;
            case 2:
                new PharmacistDashboard(user).setVisible(true);
                break;
            case 3:
                new StaffDashboard(user).setVisible(true);
                break;
            default:
                showError("Unknown user role");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}