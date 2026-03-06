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
            String healthCardNum,
            String phoneNumber) {
        validateNullFields(firstName, lastName, email, password, gender, healthCardNum, phoneNumber);
        validateEmptyFields(firstName, lastName, email, password, gender, healthCardNum, phoneNumber);
        validateDuplicateEmail(email);
        validateEmailFormat(email);
        validateDateOfBirth(dateOfBirth);
        validatePhoneNumber(phoneNumber);
        validateHealthCardNumber(healthCardNum);

    }
    private void validateNullFields(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            String healthCardNum,
            String phoneNumber) {

        if (firstName == null || lastName == null
                || email == null || password == null
                || gender == null || healthCardNum == null
                || phoneNumber == null) {
            throw new IllegalArgumentException("Null fields are not allowed");
        }
    }

    private void validateEmptyFields(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            String healthCardNum,
            String phoneNumber){
        if (firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || password.isEmpty()
                || gender.isEmpty() || healthCardNum.isEmpty()
                || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Empty fields are not allowed");
        }

    }

    private void validateEmailFormat(String email){
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (email.indexOf("@") != email.lastIndexOf("@")){
            throw new IllegalArgumentException("Email cannot contain multiple @");
        }
        String[] parts = email.split("@");
        if (parts.length != 2 || parts[1].isEmpty()){
            throw new IllegalArgumentException("Email must contain a domain");
        }
        if (!parts[1].contains(".")){
            throw new IllegalArgumentException("Email domain must contain a dot");
        }
    }

    private void validateDateOfBirth(LocalDate dateOfBirth){
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        //Current date
        LocalDate toDate = LocalDate.now();
        if (dateOfBirth.isAfter(toDate)) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        if (dateOfBirth.isBefore(toDate.minusYears(120))) {
            throw new IllegalArgumentException("Age must be less than 120 years");
        }
    }

    private void validateDuplicateEmail(String email){
        if (DATABASE.getPatientByEmail(email) != null) {
            throw new IllegalStateException("Email already exists");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        if (!phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
    }

    private void validateHealthCardNumber(String healthCardNumber){

        if (healthCardNumber == null) {
            throw new IllegalArgumentException("Health card number cannot be null");
        }

        if (!healthCardNumber.matches("\\d{9,10}")) {
            throw new IllegalArgumentException("Health card number must be 9 or 10 digits");
        }
    }

}
