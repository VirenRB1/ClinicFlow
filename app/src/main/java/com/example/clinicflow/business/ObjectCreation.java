package com.example.clinicflow.business;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.business.validation.ObjectValidator;

//Add an object in database
public class ObjectCreation {
    private final UserRepository DATABASE;
    private final ObjectValidator VALIDATOR;


    public ObjectCreation(UserRepository userRepository) {
        DATABASE = userRepository;
        this.VALIDATOR = new ObjectValidator(userRepository);
    }
// Add patient
    public boolean addPatientToDatabase(String firstName, String lastName, String email, String password, String gender,
            int age, int healthCardNum, int phoneNumber) {
        VALIDATOR.validateObject(
                firstName,
                lastName,
                email,
                password,
                gender,
                age,
                healthCardNum,
                phoneNumber
        );

        DATABASE.addPatient(
                new Patient(firstName, lastName, email, password, gender, age, healthCardNum, phoneNumber));
        return true;

    }
}
