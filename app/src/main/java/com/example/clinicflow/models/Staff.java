package com.example.clinicflow.models;
public class Staff extends Users {
    private String position;


    public Staff(String userId, String userName, String password, String gender,
                 String age, String position) {
        super(userId, userName, password, gender, age);
        this.position = position;       

        }
    
}
