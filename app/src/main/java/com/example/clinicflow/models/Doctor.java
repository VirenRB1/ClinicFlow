package com.example.clinicflow.models;

import java.time.LocalDate;

public class Doctor extends Users {
    private String specialization; // also experience
    private String licenseNumber;

    public Doctor(String firstName, String lastName, String email, String password, String gender,
                  LocalDate dob, String specialization, String licenseNumber) {
        super(firstName, lastName, email, password, gender,dob);
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
