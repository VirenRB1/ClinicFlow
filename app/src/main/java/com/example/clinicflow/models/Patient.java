package com.example.clinicflow.models;
public class Patient extends Users {
    private int healthcardNumber;
    private int phoneNumber;

    public Patient(String firstName, String lastName, String email, String password, String gender
        , int age, int healthcardNumber, int phoneNumber) {
        super(firstName,lastName, email, password, gender, age);
        this.healthcardNumber = healthcardNumber;
        this.phoneNumber = phoneNumber;
    }

    public int getHealthcardNumber() {
        return healthcardNumber;
    }

    public int getPhoneNumber() { return phoneNumber; }
}
