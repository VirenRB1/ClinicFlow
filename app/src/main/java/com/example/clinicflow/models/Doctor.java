package com.example.clinicflow.models;
public class Doctor extends Users {
    private String specialization;  //also expereience
    private String licenseNumber;

    public Doctor(String email, String password, String gender,
                  String age, String specialization, String licenseNumber) {
        super(email, password, gender, age);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }
}
