package com.example.clinicflow.models;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Period;

public abstract class Users {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;

    public Users(String firstName, String lastName, String email, String password, String gender, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dob;
    }

    //Get methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getAge() {
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalStateException("Date of birth cannot be in the future.");
        }
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }

}
