package com.example.clinicflow.models;
public class Patient extends Users {
    private int HealthcardNumber;
    private String medicalHistory;
    private int phoneNumber;

    public Patient(String userId, String userName, String password, String gender
        , String age, int HealthcardNumber, String medicalHistory, int phoneNumber) {
        super(userId, userName, password, gender, age);
        this.HealthcardNumber = HealthcardNumber;  
        this.medicalHistory = medicalHistory;
        this.phoneNumber = phoneNumber;
        }
}
