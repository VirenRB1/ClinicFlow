package com.example.clinicflow.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Period;

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

}
