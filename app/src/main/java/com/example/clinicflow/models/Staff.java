package com.example.clinicflow.models;

import java.time.LocalDate;

public class Staff extends Users {
    private String position;

    public Staff(String firstName, String lastName, String email, String password, String gender,
                 LocalDate dateOfBirth, String position) {
        super(firstName, lastName, email, password, gender, dateOfBirth);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public UserRole getRole(){
        return UserRole.STAFF;
    }
}
