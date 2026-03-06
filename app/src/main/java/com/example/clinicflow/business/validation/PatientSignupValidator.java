package com.example.clinicflow.business.validation;

import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

public class PatientSignupValidator {
    private final UserRepository repo;

    public PatientSignupValidator(UserRepository repo) {
        this.repo = repo;
    }

    public void validate(
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth,
            String healthCardNum,
            String phoneNumber
    ) throws ValidationExceptions.ValidationException {

        validateRequiredText(firstName, "First name");
        validateRequiredText(lastName, "Last name");
        validateRequiredText(password, "Password");
        validateEmail(email);
        validateDuplicateEmail(email);
        validateGender(gender);
        validateDateOfBirth(dateOfBirth);
        validatePhoneNumber(phoneNumber);
        validateHealthCardNumber(healthCardNum);
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
        if (repo.getUserByEmail(email) != null) {
            throw new ValidationExceptions.DuplicateEmailException();
        }
    }

    private void validateGender(String gender)
            throws ValidationExceptions.ValidationException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException("Gender");
        }

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
            throw new ValidationExceptions.InvalidDateOfBirthException(
                    "Date of birth cannot be in the future."
            );
        }

        if (dateOfBirth.isBefore(today.minusYears(120))) {
            throw new ValidationExceptions.InvalidDateOfBirthException(
                    "Age must be less than 120 years."
            );
        }
    }

    private void validatePhoneNumber(String phoneNumber)
            throws ValidationExceptions.ValidationException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException("Phone number");
        }

        if (!phoneNumber.matches("\\d{10}")) {
            throw new ValidationExceptions.InvalidPhoneNumberException();
        }
    }

    private void validateHealthCardNumber(String healthCardNum)
            throws ValidationExceptions.ValidationException {
        if (healthCardNum == null || healthCardNum.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException("Health card number");
        }

        if (!(healthCardNum.matches("\\d{9}") || healthCardNum.matches("\\d{10}"))) {
            throw new ValidationExceptions.InvalidHealthCardException();
        }
    }
}