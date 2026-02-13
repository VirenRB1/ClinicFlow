package com.example.clinicflow.business;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;
//Add an object in database
public class ObjectCreation {
    private final UserRepository DATABASE;

    public ObjectCreation(UserRepository userRepository) {
        DATABASE = userRepository;
    }
// Prevent duplicate email
    private boolean checkForDuplicatePatient(String email) {
        for (Patient patient : DATABASE.getAllPatients()) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
// Add patient
    public boolean addPatientToDatabase(String firstName, String lastName, String email, String password, String gender,
            int age, int healthCardNum, int phoneNumber) {
        if (firstName == null || lastName == null || email == null || password == null || gender == null) {
            return false;
        }
        if (firstName.isEmpty() || lastName.isEmpty() || !email.contains("@") || password.isEmpty()
                || gender.isEmpty()) {
            return false;
        }
        if (checkForDuplicatePatient(email)) {
            return false;
        } else {
            DATABASE.addPatient(
                    new Patient(firstName, lastName, email, password, gender, age, healthCardNum, phoneNumber));
            return true;
        }
    }
}
