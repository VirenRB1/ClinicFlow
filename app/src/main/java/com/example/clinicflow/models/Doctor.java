package com.example.clinicflow.models;

import java.time.LocalDate;
import java.util.Objects;

public class Doctor extends Users {
    private Specialization specialization; // also experience
    private String licenseNumber;

    public Doctor(String firstName, String lastName, String email, String password, String gender,
            LocalDate dateOfBirth, Specialization specialization, String licenseNumber) {
        super(firstName, lastName, email, password, gender, dateOfBirth);
        this.specialization = Objects.requireNonNull(specialization, "Specialization cannot be null");
        this.licenseNumber = licenseNumber;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public UserRole getRole() {
        return UserRole.DOCTOR;
    }
}
