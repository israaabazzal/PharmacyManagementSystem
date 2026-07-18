package com.pharmacy.models;

import java.sql.Timestamp;

public class Alert {
    private int alertId;
    private int medicineId;
    private String alertType; // "low_stock", "expiry"
    private String message;
    private String severity; // "low", "medium", "high"
    private boolean isResolved;
    private Integer resolvedBy;
    private Timestamp resolvedAt;
    private Timestamp createdAt;

    // Getters and setters
    public int getAlertId() { return alertId; }
    public void setAlertId(int alertId) { this.alertId = alertId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public boolean isResolved() { return isResolved; }
    public void setResolved(boolean resolved) { isResolved = resolved; }

    public Integer getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(Integer resolvedBy) { this.resolvedBy = resolvedBy; }

    public Timestamp getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Timestamp resolvedAt) { this.resolvedAt = resolvedAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
