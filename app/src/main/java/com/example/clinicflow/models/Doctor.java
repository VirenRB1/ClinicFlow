package com.example.clinicflow.models;
public class Doctor extends Users {
    private String specialization;  //also expereience
    private String licenseNumber;

    public Doctor(String userId, String userName, String password, String gender,
                 String age, String specialization, String licenseNumber) {
        super(userId, userName, password, gender, age);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;     
} 
