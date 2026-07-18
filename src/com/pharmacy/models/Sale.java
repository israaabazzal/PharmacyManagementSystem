package com.pharmacy.models;

import java.math.BigDecimal;
import java.util.Date;

public class Sale {
    private int saleId;
    private Integer prescriptionId; // nullable
    private int patientId;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal taxAmount;
    private String paymentMethod; // e.g., "cash", "credit_card", etc.
    private String paymentStatus; // e.g., "paid", "pending"
    private int processedBy;
    private Date createdAt;

    public Sale() {
        discount = BigDecimal.ZERO;
        taxAmount = BigDecimal.ZERO;
        paymentStatus = "paid"; // default
    }

    // Getters and setters
    public int getSaleId() {
        return saleId;
    }
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    public Integer getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public int getProcessedBy() {
        return processedBy;
    }
    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
