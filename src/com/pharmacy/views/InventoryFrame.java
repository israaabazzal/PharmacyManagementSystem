package com.pharmacy.views;

import com.formdev.flatlaf.FlatLightLaf;
import com.pharmacy.dao.AlertDAO;
import com.pharmacy.dao.AuditLogger;
import com.pharmacy.dao.CategoryDAO;
import com.pharmacy.dao.MedicineDAO;
import com.pharmacy.models.Alert;
import com.pharmacy.models.Medicine;
import com.pharmacy.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class InventoryFrame extends JFrame {
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private User currentpharmacist;
    private JPanel mainPanel;

 
    
    // Gradient colors: light blue to light lavender
    private final Color[] GRADIENT_COLORS = {
        new Color(230, 245, 255),  // Light blue
        new Color(220, 230, 250),  // Light lavender mid
        new Color(240, 230, 250)   // Light lavender bottom
    };
  
    
    
    
    
    public InventoryFrame(User currentpharmacist) {
        
                      try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        
        
        
        
        this.currentpharmacist = currentpharmacist;
        setTitle("Medicine Management");
        setSize(600, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadMedicineData();
        showAlertsIfAny();
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
        
        
        
        
           // Buttons
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    topPanel.setOpaque(false); // let gradient show through
    JButton addBtn = new JButton("Add");
    JButton editBtn = new JButton("Edit");
    JButton deleteBtn = new JButton("Delete");
    JButton refreshBtn = new JButton("Refresh");

    topPanel.add(addBtn);
    topPanel.add(editBtn);
    topPanel.add(deleteBtn);
    topPanel.add(refreshBtn);
    

    // Table
    tableModel = new DefaultTableModel();
    medicineTable = new JTable(tableModel);
    JScrollPane tableScroll = new JScrollPane(medicineTable);

    // Set table columns
    tableModel.setColumnIdentifiers(new String[]{
        "ID", "Name", "Description", "Category", "Batch", "Expiry", "Qty", "Unit Price", "Selling Price"
    });

    mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(tableScroll, BorderLayout.CENTER);
    setContentPane(mainPanel);


    
    
    // call the fuction to Load medicines initially 
    loadMedicineData();
    

   // Button actions
    refreshBtn.addActionListener(e -> loadMedicineData()); 
    addBtn.addActionListener(e -> showAddMedicineDialog());
    editBtn.addActionListener(e -> showEditMedicineDialog());
    deleteBtn.addActionListener(e -> deleteSelectedMedicine());
   
    
        
    }

  
       

  private void loadMedicineData( ) {
    try {
        tableModel.setRowCount(0);  // Clear old data
        MedicineDAO dao = new MedicineDAO();
        List<Medicine> medicines = dao.getAllMedicines();

        for (Medicine m : medicines) {
            tableModel.addRow(new Object[]{
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getCategoryName(), // from JOIN
                m.getBatchNumber(),
                m.getExpiryDate(),
                m.getQuantity(),
                m.getUnitPrice(),
                m.getSellingPrice()
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading medicines: " + e.getMessage());
        e.printStackTrace();
    }
}
  
  
  
  
private void showAddMedicineDialog() {
    
    
    JDialog dialog = new JDialog(this, "Add Medicine", true);
    dialog.setSize(600, 800);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(Color.WHITE);

    // Create input fields
    JTextField nameField = new JTextField();
    JTextField descField = new JTextField();
    JTextField batchField = new JTextField();
    JTextField expiryField = new JTextField();  // yyyy-mm-dd
    JTextField qtyField = new JTextField();
    JTextField unitPriceField = new JTextField();
    JTextField sellingPriceField = new JTextField();

    // Load categories
    final Map<String, Integer> categoryMap = new LinkedHashMap<>();
    try {
        categoryMap.putAll(new CategoryDAO().getCategoryNameIdMap());
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage());
        e.printStackTrace();
        return;
    }

    // Category dropdown
    JComboBox<String> categoryCombo = new JComboBox<>(categoryMap.keySet().toArray(new String[0]));
    categoryCombo.setEditable(false);
    categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    categoryCombo.setPreferredSize(new Dimension(250, 30));

    // Form panel layout
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    formPanel.add(createLabeledField("Name:", nameField));
    formPanel.add(createLabeledField("Description:", descField));

    JPanel categoryPanel = new JPanel(new BorderLayout(5, 5));
    categoryPanel.setOpaque(false);
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    categoryPanel.add(categoryLabel, BorderLayout.NORTH);
    categoryPanel.add(categoryCombo, BorderLayout.CENTER);
    categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    formPanel.add(categoryPanel);

    formPanel.add(createLabeledField("Batch Number:", batchField));
    formPanel.add(createLabeledField("Expiry Date (yyyy-mm-dd):", expiryField));
    formPanel.add(createLabeledField("Quantity:", qtyField));
    formPanel.add(createLabeledField("Unit Price:", unitPriceField));
    formPanel.add(createLabeledField("Selling Price:", sellingPriceField));

    // Buttons
    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");
    saveBtn.setBackground(Color.WHITE);
    cancelBtn.setBackground(Color.WHITE);
    saveBtn.setFocusPainted(false);
    cancelBtn.setFocusPainted(false);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
    buttonPanel.add(saveBtn);
    buttonPanel.add(cancelBtn);

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Save button logic
    saveBtn.addActionListener(e -> {
        try {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            Integer categoryId = categoryMap.get(selectedCategory);

            if (categoryId == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a valid category.");
                return;
            }

            Medicine med = new Medicine();
            med.setName(nameField.getText());
            med.setDescription(descField.getText());
            med.setCategoryName(selectedCategory);
            med.setCategoryId(categoryId);
            med.setBatchNumber(batchField.getText());

            // Validate and set expiry date
            try {
                med.setExpiryDate(Date.valueOf(expiryField.getText()));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Expiry date must be in yyyy-MM-dd format.");
                return;
            }

            med.setQuantity(Integer.parseInt(qtyField.getText()));
            med.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
            med.setSellingPrice(Double.parseDouble(sellingPriceField.getText()));
            med.setAddedBy(currentpharmacist.getUserId());

            // Insert into DB
            int insertedId = new MedicineDAO().insertMedicine(med);
            med.setId(insertedId); // for audit log

            // Log action
            AuditLogger.log(
                currentpharmacist.getUserId(),
                "ADD",
                "Medicine",
                insertedId,
                null,
                "Name=" + med.getName() +
                ", Description=" + med.getDescription() +
                ", Category=" + med.getCategoryName() +
                ", Batch=" + med.getBatchNumber() +
                ", Expiry=" + med.getExpiryDate() +
                ", Qty=" + med.getQuantity() +
                ", UnitPrice=" + med.getUnitPrice() +
                ", SellingPrice=" + med.getSellingPrice()
            );

            JOptionPane.showMessageDialog(this, "Medicine added successfully.");
            dialog.dispose();
            loadMedicineData();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for Quantity, Unit Price, and Selling Price.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    cancelBtn.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}
 
  // helper for add dialog design 
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

  
  private void showEditMedicineDialog() {
    int selectedRow = medicineTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a medicine to edit.");
        return;
    }

    int medicineId = (int) tableModel.getValueAt(selectedRow, 0);

    // Load categories into map
    Map<String, Integer> categoryMap = new LinkedHashMap<>();
    try {
        categoryMap = new CategoryDAO().getCategoryNameIdMap();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage());
        e.printStackTrace();
        return;
    }

    // Get current category from table and set up JComboBox
    String currentCategory = (String) tableModel.getValueAt(selectedRow, 3);
    JComboBox<String> categoryCombo = new JComboBox<>(categoryMap.keySet().toArray(new String[0]));
    categoryCombo.setSelectedItem(currentCategory);

    // Pre-fill other fields
    JTextField nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
    JTextField descField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
    JTextField batchField = new JTextField((String) tableModel.getValueAt(selectedRow, 4));
    JTextField expiryField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
    JTextField qtyField = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());
    JTextField unitPriceField = new JTextField(tableModel.getValueAt(selectedRow, 7).toString());
    JTextField sellingPriceField = new JTextField(tableModel.getValueAt(selectedRow, 8).toString());

    // Build form panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    formPanel.add(createLabeledField("Name:", nameField));
    formPanel.add(createLabeledField("Description:", descField));

    JPanel categoryPanel = new JPanel(new BorderLayout(5, 5));
    categoryPanel.setOpaque(false);
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    categoryCombo.setPreferredSize(new Dimension(250, 30));
    categoryPanel.add(categoryLabel, BorderLayout.NORTH);
    categoryPanel.add(categoryCombo, BorderLayout.CENTER);
    categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    formPanel.add(categoryPanel);

    formPanel.add(createLabeledField("Batch Number:", batchField));
    formPanel.add(createLabeledField("Expiry Date (yyyy-mm-dd):", expiryField));
    formPanel.add(createLabeledField("Quantity:", qtyField));
    formPanel.add(createLabeledField("Unit Price:", unitPriceField));
    formPanel.add(createLabeledField("Selling Price:", sellingPriceField));

    int result = JOptionPane.showConfirmDialog(
        this, formPanel,
        "Edit Medicine",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        try {
            Medicine med = new Medicine();
            med.setId(medicineId);
            med.setName(nameField.getText());
            med.setDescription(descField.getText());

            String selectedCategory = (String) categoryCombo.getSelectedItem();
            Integer categoryId = categoryMap.get(selectedCategory);

            if (categoryId == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid category.");
                return;
            }

            med.setCategoryName(selectedCategory);
            med.setCategoryId(categoryId);

            med.setBatchNumber(batchField.getText());
            med.setExpiryDate(Date.valueOf(expiryField.getText()));
            med.setQuantity(Integer.parseInt(qtyField.getText()));
            med.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
            med.setSellingPrice(Double.parseDouble(sellingPriceField.getText()));
            med.setAddedBy(currentpharmacist.getUserId());

            new MedicineDAO().updateMedicine(med);

            AuditLogger.log(
                currentpharmacist.getUserId(),
                "EDIT",
                "Medicine",
                medicineId,
                null,
                "Updated to: " +
                "Name=" + med.getName() +
                ", Description=" + med.getDescription() +
                ", Category=" + med.getCategoryName() +
                ", Batch=" + med.getBatchNumber() +
                ", Expiry=" + med.getExpiryDate() +
                ", Qty=" + med.getQuantity() +
                ", UnitPrice=" + med.getUnitPrice() +
                ", SellingPrice=" + med.getSellingPrice()
            );

            loadMedicineData();
            JOptionPane.showMessageDialog(this, "Medicine updated successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

  
  private void deleteSelectedMedicine() {
    int selectedRow = medicineTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a medicine to delete.");
        return;
    }

    int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
    String medicineName = (String) tableModel.getValueAt(selectedRow, 1);

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete \"" + medicineName + "\"?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            new MedicineDAO().deleteMedicine(medicineId);

            AuditLogger.log(
                currentpharmacist.getUserId(),
                "DELETE",
                "Medicine",
                medicineId,
                "Name=" + medicineName,
                null
            );

            loadMedicineData();
            JOptionPane.showMessageDialog(this, "Medicine deleted successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting medicine: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}


private void showAlertsIfAny() {
    AlertDAO alertDAO = new AlertDAO();
    alertDAO.checkAndGenerateAlerts(); // generate fresh alerts and auto-resolve old ones

    List<Alert> alerts = alertDAO.getUnresolvedAlerts();
    if (!alerts.isEmpty()) {
        StringBuilder message = new StringBuilder("Unresolved Alerts:\n\n");
        for (Alert alert : alerts) {
            message.append("- ").append(alert.getMessage()).append(" [")
                   .append(alert.getSeverity()).append("]\n");
        }

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setBackground(new Color(200, 200, 230));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Alert Notification",
            JOptionPane.WARNING_MESSAGE
        );
    }
}



  
}

  
  
  
  
  
  
  
  
  
  



    

    
    
    
    
    


