package com.example.clinicflow.models;
// Doctor account
//Contain specialization in doctor job and a unique license number
public class Doctor extends Users {
    private String specialization; // also experience
    private String licenseNumber;

    public Doctor(String firstName, String lastName, String email, String password, String gender,
            int age, String specialization, String licenseNumber) {
        super(firstName, lastName, email, password, gender, age);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}
