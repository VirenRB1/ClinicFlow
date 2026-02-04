package com.example.clinicflow.models;

public class Users {
    private String userId;
    private String userName;
    private String password;
    private String gender;   
    private String age;

    public Users(String userId, String userName, String password, 
                 String gender, String age) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    }

