package com.example.clinicflow.models;

public abstract class Users {
    private String email;
    private String password;
    private String gender;   
    private String age;

    public Users(String email, String password,
                 String gender, String age) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
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

    public String getAge() {
        return age;
    }
}


