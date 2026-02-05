package com.example.clinicflow.models;
public class Doctor extends Users {
    private String specialization;  //also experience
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
