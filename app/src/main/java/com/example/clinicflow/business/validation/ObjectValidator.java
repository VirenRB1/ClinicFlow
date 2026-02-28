package com.example.clinicflow.business.validation;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

//Create database
public class ObjectValidator {
    private final UserRepository DATABASE;

    public ObjectValidator(UserRepository userRepository) {
        DATABASE = userRepository;
    }

    public void validateObject(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth,
            int healthCardNum,
            int phoneNumber) {
        validateNullFields(firstName, lastName, email, password, gender);
        validateEmptyFields(firstName, lastName, email, password, gender);
        validateDuplicateEmail(email);
        validateEmailFormat(email);
        validateDateOfBirth(dateOfBirth);
    }
    private void validateNullFields(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender) {

        if (firstName == null || lastName == null
                || email == null || password == null
                || gender == null) {
            throw new IllegalArgumentException("Null fields are not allowed");
        }
    }

    private void validateEmptyFields(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender){
        if (firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || password.isEmpty()
                || gender.isEmpty()) {
            throw new IllegalArgumentException("Empty fields are not allowed");
        }

    }

    private void validateEmailFormat(String email){
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateDateOfBirth(LocalDate dateOfBirth){
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
    }

    private void validateDuplicateEmail(String email){
        if (DATABASE.getPatientByEmail(email) != null) {
            throw new IllegalStateException("Email already exists");
        }

    }

}
