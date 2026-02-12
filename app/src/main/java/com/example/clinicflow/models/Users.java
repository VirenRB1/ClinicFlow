package com.example.clinicflow.models;

public abstract class Users {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;   
    private int age;

    public Users(String firstName, String lastName, String email, String password, String gender, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
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

    public int getAge() {
        return age;
    }
}


