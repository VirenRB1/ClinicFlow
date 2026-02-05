package com.example.clinicflow.models;
public class Patient extends Users {
    private int healthcardNumber;
    private String medicalHistory;
    private int phoneNumber;

    public Patient(String firstName, String lastName, String email, String password, String gender
        , int age, int healthcardNumber, String medicalHistory, int phoneNumber) {
        super(firstName,lastName, email, password, gender, age);
        this.HealthcardNumber = healthcardNumber;  
        this.medicalHistory = medicalHistory;
        this.phoneNumber = phoneNumber;
    }

    public int getHealthcardNumber() {
        return healthcardNumber;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
