package com.example.clinicflow.models;

import java.time.LocalDate;

public class Patient extends Users {
    private int healthcardNumber;
    private int phoneNumber;

    public Patient(String firstName, String lastName, String email, String password, String gender
        , LocalDate dateOfBirth, int healthcardNumber, int phoneNumber) {
        super(firstName,lastName, email, password, gender, dateOfBirth);
        this.healthcardNumber = healthcardNumber;
        this.phoneNumber = phoneNumber;
    }

    public int getHealthCardNumber() {
        return healthcardNumber;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
