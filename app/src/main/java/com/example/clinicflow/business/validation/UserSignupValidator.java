package com.example.clinicflow.business.validation;

import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

public class UserSignupValidator {
    private final UserRepository repo;

    public UserSignupValidator(UserRepository repo) {
        this.repo = repo;
    }

    public void userValidator(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth
    ) throws ValidationExceptions.ValidationException {
        validateRequiredText(firstName, "First name");
        validateRequiredText(lastName, "Last name");
        validateRequiredText(password, "Password");
        validateEmail(email);
        validateDuplicateEmail(email);
        validateGender(gender);
        validateDateOfBirth(dateOfBirth);
    }

    public void patientValidator(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth,
            String healthCardNum,
            String phoneNumber
    ) throws ValidationExceptions.ValidationException {
        userValidator(firstName, lastName, email, password, gender, dateOfBirth);
        
        validatePhoneNumber(phoneNumber);
        validateHealthCardNumber(healthCardNum);
    }


    public void doctorValidator(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth,
            Specialization specialization,
            String licenseNumber
    ) throws ValidationExceptions.ValidationException {
        userValidator(firstName, lastName, email, password, gender, dateOfBirth);

        validateSpecialization(specialization);
        validateLicenseNumber(licenseNumber);
    }
    
    public void staffValidator (
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth) throws ValidationExceptions.ValidationException {
        userValidator(firstName, lastName, email, password, gender, dateOfBirth);
    }
    

    private void validateRequiredText(String value, String fieldName)
            throws ValidationExceptions.EmptyFieldException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException(fieldName);
        }
    }

    private void validateEmail(String email)
            throws ValidationExceptions.ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException("Email");
        }

        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.com$")) {
            throw new ValidationExceptions.InvalidEmailException();
        }
    }

    private void validateDuplicateEmail(String email)
            throws ValidationExceptions.DuplicateEmailException {
        if (repo.getPatientByEmail(email) != null) {
            throw new ValidationExceptions.DuplicateEmailException();
        }
    }

    private void validateGender(String gender)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(gender, "Gender");
        if (!gender.equalsIgnoreCase("Male")
                && !gender.equalsIgnoreCase("Female")
                && !gender.equalsIgnoreCase("Other")) {
            throw new ValidationExceptions.InvalidGenderException();
        }
    }

    private void validateDateOfBirth(LocalDate dateOfBirth)
            throws ValidationExceptions.ValidationException {
        if (dateOfBirth == null) {
            throw new ValidationExceptions.EmptyFieldException("Date of birth");
        }

        LocalDate today = LocalDate.now();
        if (dateOfBirth.isAfter(today)) {
            throw new ValidationExceptions.InvalidDateOfBirthException("Date of birth cannot be in the future.");
        }
        if (dateOfBirth.isBefore(today.minusYears(120))) {
            throw new ValidationExceptions.InvalidDateOfBirthException("Age must be less than 120 years.");
        }
    }

    private void validatePhoneNumber(String phoneNumber)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(phoneNumber, "Phone number");
        if (!phoneNumber.replaceAll("\\D", "").matches("\\d{10}")) {
            throw new ValidationExceptions.InvalidPhoneNumberException();
        }
    }

    private void validateHealthCardNumber(String healthCardNum)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(healthCardNum, "Health card number");
        if (!healthCardNum.matches("\\d{9,10}")) {
            throw new ValidationExceptions.InvalidHealthCardException();
        }
    }

    private void validateSpecialization(Specialization specialization)
            throws ValidationExceptions.ValidationException {
        if (specialization == null) {
            throw new ValidationExceptions.EmptyFieldException("Specialization");
        }
    }

    private void validateLicenseNumber(String licenseNumber)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(licenseNumber, "License number");
        if (licenseNumber.length() < 5) {
            throw new ValidationExceptions.ValidationException("License number is too short.");
        }
    }
}
