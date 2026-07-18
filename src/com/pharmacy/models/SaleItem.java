package com.pharmacy.models;

import java.math.BigDecimal;

public class SaleItem {
    private int saleItemId; // optional, usually DB generated
    private int saleId;     // set after sale insert
    private int medicineId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public SaleItem() {}

    // Getters and setters
    public int getSaleItemId() {
        return saleItemId;
    }
    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }
    public int getSaleId() {
        return saleId;
    }
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    public int getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
