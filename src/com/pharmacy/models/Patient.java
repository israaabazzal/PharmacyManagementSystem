package com.pharmacy.models;

import java.time.LocalDate;

public class Patient {
    private int patientId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String medicalHistory;
    private int createdBy;  // user_id of the creator

    // Constructors
    public Patient() {}

    public Patient(int patientId, String fullName, LocalDate dateOfBirth, String gender,
                   String address, String phone, String email, String medicalHistory, int createdBy) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.medicalHistory = medicalHistory;
        this.createdBy = createdBy;
    }

    // Getters and setters...
    // e.g.
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    
    
    
        @Override
public String toString() {
    return fullName; // or return name + " - " + specialization;
}

    
}
