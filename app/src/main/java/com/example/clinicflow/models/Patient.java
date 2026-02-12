package com.example.clinicflow.models;
public class Patient extends Users {
    private int HealthcardNumber;
    private String medicalHistory;
    private int phoneNumber;

    public Patient(String email, String password, String gender
        , String age, int HealthcardNumber, String medicalHistory, int phoneNumber) {
        super(email, password, gender, age);
        this.HealthcardNumber = HealthcardNumber;  
        this.medicalHistory = medicalHistory;
        this.phoneNumber = phoneNumber;
        }
}
