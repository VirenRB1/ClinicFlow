package com.example.clinicflow.models;
public class Staff extends Users {
    private String position;


    public Staff(String email, String password, String gender,
                 String age, String position) {
        super(email,
                password,
                gender,
                age);
        this.position = position;       

    }
    
}
