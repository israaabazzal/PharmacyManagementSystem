package com.pharmacy.views;

import com.pharmacy.dao.AuditLogger;
import com.pharmacy.dao.UserDAO;
import com.pharmacy.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.List;

public class UserManagementFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private User currentAdmin;
    private JPanel mainPanel;

    // Gradient colors: light blue to light lavender
    private final Color[] GRADIENT_COLORS = {
        new Color(230, 245, 255),  // Light blue
        new Color(220, 230, 250),  // Light lavender mid
        new Color(240, 230, 250)   // Light lavender bottom
    };

    public UserManagementFrame(User currentAdmin) {
        this.currentAdmin = currentAdmin;
        setTitle("User Management");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadUserData();
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
                GradientPaint gp = new GradientPaint(start, GRADIENT_COLORS[0], end, GRADIENT_COLORS[2]);

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Role","Active"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

// Button panel
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
buttonPanel.setOpaque(false);

JButton refreshBtn = new JButton("Refresh");
JButton addUserBtn = new JButton("Add User");
JButton deleteUserBtn = new JButton("Delete User");
JButton editUserBtn = new JButton("Edit User");

refreshBtn.addActionListener(e -> loadUserData());
addUserBtn.addActionListener(e -> openAddUserDialog());
deleteUserBtn.addActionListener(e -> deleteSelectedUser());
editUserBtn.addActionListener(e -> openEditUserDialog());

buttonPanel.add(refreshBtn);
buttonPanel.add(addUserBtn);
buttonPanel.add(deleteUserBtn);
buttonPanel.add(editUserBtn);

// Add to top of main panel
mainPanel.add(buttonPanel, BorderLayout.NORTH);


        add(mainPanel);
    }

    private void loadUserData() {
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();

            tableModel.setRowCount(0); // Clear table
            for (User user : users) {
                tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getRoleName(),
                    user.isActive() ? "yes" : "no"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load users: " + e.getMessage());
        }
    }

private void openAddUserDialog() {
    JDialog dialog = new JDialog(this, "Add New User", true);
    dialog.setSize(600, 600);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // Create form fields
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField emailField = new JTextField();
    JTextField phoneField = new JTextField();
    String[] roles = {"admin", "pharmacist", "staff"};
    JComboBox<String> roleCombo = new JComboBox<>(roles);   
    JCheckBox activeCheck = new JCheckBox("Active");
   activeCheck.setSelected(true); // new users default to active
   
    

    // Form panel with vertical layout
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    formPanel.add(createLabeledField("Username:", usernameField));
    formPanel.add(createLabeledField("Password:", passwordField));
    formPanel.add(createLabeledField("Email:", emailField));
    formPanel.add(createLabeledField("Phone:", phoneField));
    
    // role panel

    JPanel rolePanel = new JPanel(new BorderLayout(5, 5));
    rolePanel.setBackground(Color.WHITE);
    JLabel roleLabel = new JLabel("Role:");
    roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    roleCombo.setPreferredSize(new Dimension(250, 30));
    rolePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    rolePanel.add(roleLabel, BorderLayout.NORTH);
    rolePanel.add(roleCombo, BorderLayout.CENTER);
    
     formPanel.add(rolePanel);
     
     // active panel 
     
    // Active panel
    JPanel activePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    activePanel.setBackground(Color.WHITE);
    activePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    activePanel.add(activeCheck);
    formPanel.add(activePanel);
     
     
     
    // Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);

    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");
    saveBtn.setBackground(Color.WHITE);
    cancelBtn.setBackground(Color.WHITE);

    buttonPanel.add(saveBtn);
    buttonPanel.add(cancelBtn);

    // Add panels to dialog
    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Button actions
    saveBtn.addActionListener(e -> {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String roleName = (String) roleCombo.getSelectedItem();

             int roleId;
            switch (roleName.toLowerCase()) {
                case "admin": roleId = 1; break;
                case "pharmacist": roleId = 2; break;
                case "staff": roleId = 3; break;
                default: roleId = 3;
            }
            
            boolean isActive = activeCheck.isSelected();
            
            User newUser = new User(0, username, password, email, phone, roleId, isActive);
            new UserDAO().addUser(newUser);

            JOptionPane.showMessageDialog(this, "User added successfully.");
            loadUserData();
            AuditLogger.log(currentAdmin.getUserId(), "Add", "User", newUser.getUserId(), null, null);

            dialog.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Failed to add user: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    cancelBtn.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}

    
    // design for the dialog : will be called inside the openAddUserDialog when addind to main form panel 

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

   

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user: " + username + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UserDAO dao = new UserDAO();
                dao.deleteUser(userId);
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                loadUserData();
                AuditLogger.log(currentAdmin.getUserId(), "Delete", "User", userId, username, null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
            }
        }
    }

    
    
    
    
    
    private void openEditUserDialog() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        return;
    }

    int userId = (int) tableModel.getValueAt(selectedRow, 0);

    try {
        UserDAO dao = new UserDAO();
        User user = dao.getUserById(userId);

        JDialog dialog = new JDialog(this, "Edit User", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Create form fields pre-filled
        JTextField usernameField = new JTextField(user.getUsername());
        JTextField emailField = new JTextField(user.getEmail());
        JTextField phoneField = new JTextField(user.getPhone());
        String[] roles = {"admin", "pharmacist", "staff"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        // Set selected role based on user role
        String userRoleName = user.getRoleName();
        if (userRoleName != null) {
            roleCombo.setSelectedItem(userRoleName.toLowerCase());
        } else {
            switch (user.getRoleId()) {
                case 1: roleCombo.setSelectedItem("admin"); break;
                case 2: roleCombo.setSelectedItem("pharmacist"); break;
                case 3: roleCombo.setSelectedItem("staff"); break;
                default: roleCombo.setSelectedItem("staff");
            }
        }

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(createLabeledField("Username:", usernameField));
        formPanel.add(createLabeledField("Email:", emailField));
        formPanel.add(createLabeledField("Phone:", phoneField));

        // Role panel
        JPanel rolePanel = new JPanel(new BorderLayout(5, 5));
        rolePanel.setBackground(Color.WHITE);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(250, 30));
        rolePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rolePanel.add(roleLabel, BorderLayout.NORTH);
        rolePanel.add(roleCombo, BorderLayout.CENTER);
        formPanel.add(rolePanel);

        // Active checkbox panel
        JCheckBox activeCheck = new JCheckBox("Active");
        activeCheck.setBackground(Color.WHITE);
        activeCheck.setSelected(user.isActive()); // pre-fill current status
        JPanel activePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        activePanel.setBackground(Color.WHITE);
        activePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        activePanel.add(activeCheck);
        formPanel.add(activePanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.setBackground(Color.WHITE);
        cancelBtn.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Save action
        saveBtn.addActionListener(e -> {
            try {
                user.setUsername(usernameField.getText().trim());
                user.setEmail(emailField.getText().trim());
                user.setPhone(phoneField.getText().trim());

                String newRole = (String) roleCombo.getSelectedItem();
                int newRoleId;
                switch (newRole.toLowerCase()) {
                    case "admin": newRoleId = 1; break;
                    case "pharmacist": newRoleId = 2; break;
                    case "staff":
                    default: newRoleId = 3;
                }
                user.setRoleId(newRoleId);

                // Set active status from checkbox
                user.setActive(activeCheck.isSelected());

                dao.updateUser(user);

                JOptionPane.showMessageDialog(dialog, "User updated successfully.");
                loadUserData();
                AuditLogger.log(currentAdmin.getUserId(), "Edit", "User", user.getUserId(), null, null);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error editing user: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        e.printStackTrace();
    }
}
}