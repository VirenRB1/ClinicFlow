package com.example.clinicflow.business;

import com.example.clinicflow.business.validation.PatientSignupValidator;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

// Add an object in database
public class ObjectCreation {

    private final UserRepository DATABASE;
    private final PatientSignupValidator VALIDATOR;

    public ObjectCreation(UserRepository userRepository) {
        this.DATABASE = userRepository;
        this.VALIDATOR = new PatientSignupValidator(userRepository);
    }

    // Add patient with validation
    public boolean addPatientToDatabase(String firstName, String lastName, String email, String password, String gender,
                                        LocalDate dateOfBirth, String healthCardNum, String phoneNumber)
            throws ValidationExceptions.ValidationException {

        VALIDATOR.validate(
                firstName,
                lastName,
                email,
                password,
                gender,
                dateOfBirth,
                healthCardNum,
                phoneNumber
        );

        DATABASE.addPatient(
                new Patient(firstName, lastName, email, password, gender, dateOfBirth, healthCardNum, phoneNumber)
        );

        return true;
    }
}