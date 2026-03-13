package com.example.clinicflow.models;

import java.time.LocalDate;

public class Admin extends Users {

    public Admin(String firstName, String lastName, String email, String password, String gender, LocalDate dob) {
        super(firstName, lastName, email, password, gender, dob);
    }

    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
