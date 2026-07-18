package com.pharmacy.models;

import java.util.Date;

public class Medicine {
    private int id;
    private String name;
    private String batchNumber;
    private Date expiryDate;
    private int quantity;
    private double unitPrice;
    private double sellingPrice;
    private String description;
     private String categoryName;
     private int categoryId;
     private int addedBy;


    public Medicine() {}

    public Medicine(int id, String name, String batchNumber, Date expiryDate, int quantity, double unitPrice, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sellingPrice = sellingPrice;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
    
    
public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public String getCategoryName() {
    return categoryName;
}

public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
}


public int getCategoryId() {
        
    return categoryId;
}
public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
}


public int getAddedBy() {
    return addedBy;
}
public void setAddedBy(int addedBy) {
    this.addedBy = addedBy;
}


    
}

