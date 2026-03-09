package com.example.clinicflow.models;

import java.time.LocalDate;

public class Patient extends Users {
    private String healthcardNumber;
    private String phoneNumber;

    public Patient(String firstName, String lastName, String email, String password, String gender
        , LocalDate dateOfBirth, String healthcardNumber, String phoneNumber) {
        super(firstName,lastName, email, password, gender, dateOfBirth);
        this.healthcardNumber = healthcardNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getHealthCardNumber() {
        return healthcardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRole getRole(){
        return UserRole.PATIENT;
    }
}
