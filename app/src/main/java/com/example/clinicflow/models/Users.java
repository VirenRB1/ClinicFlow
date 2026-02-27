package com.example.clinicflow.models;


import java.time.LocalDate;
import java.time.Period;

public abstract class Users {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;   
    private LocalDate dateOfBirth;

    public Users(String firstName, String lastName, String email, String password, String gender, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

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

    public LocalDate getDateOfBirth() {return dateOfBirth;}

    public int getAge() {
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalStateException("Date of birth cannot be in the future.");
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}


