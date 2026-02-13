package com.example.clinicflow.models;

// Patient account
// Unique phone number and healthcard number
public class Patient extends Users {
    private int healthcardNumber;
    private int phoneNumber;

    public Patient(String firstName, String lastName, String email, String password, String gender, int age,
            int healthcardNumber, int phoneNumber) {
        super(firstName, lastName, email, password, gender, age);
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
