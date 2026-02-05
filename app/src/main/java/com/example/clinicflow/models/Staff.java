package com.example.clinicflow.models;

public class Staff extends Users {
    private String position;

    public Staff(String firstName, String lastName, String email, String password, String gender,
            int age, String position) {
        super(firstName, lastName, email, password, gender, age);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

}
