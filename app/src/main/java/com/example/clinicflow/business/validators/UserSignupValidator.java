package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

/**
 * Validator class for user signup and registration details.
 */
public class UserSignupValidator {
    private final UserRepository repo;

    /**
     * Constructs the validator with a repository to check for existing records.
     * @param repo The user repository.
     */
    public UserSignupValidator(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Validates common user information.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param email User's email address.
     * @param password User's password.
     * @param gender User's gender.
     * @param dateOfBirth User's date of birth.
     * @throws ValidationExceptions.ValidationException If any common field is invalid.
     */
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

    /**
     * Validates patient-specific information along with common user fields.
     * @param firstName Patient's first name.
     * @param lastName Patient's last name.
     * @param email Patient's email.
     * @param password Patient's password.
     * @param gender Patient's gender.
     * @param dateOfBirth Patient's date of birth.
     * @param healthCardNum Patient's health card number.
     * @param phoneNumber Patient's phone number.
     * @throws ValidationExceptions.ValidationException If any field is invalid.
     */
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


    /**
     * Validates doctor-specific information along with common user fields.
     * @param firstName Doctor's first name.
     * @param lastName Doctor's last name.
     * @param email Doctor's email.
     * @param password Doctor's password.
     * @param gender Doctor's gender.
     * @param dateOfBirth Doctor's date of birth.
     * @param specialization Doctor's medical specialization.
     * @param licenseNumber Doctor's medical license number.
     * @throws ValidationExceptions.ValidationException If any field is invalid.
     */
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
    
    /**
     * Validates staff-specific information along with common user fields.
     * @param firstName Staff's first name.
     * @param lastName Staff's last name.
     * @param email Staff's email.
     * @param password Staff's password.
     * @param gender Staff's gender.
     * @param dateOfBirth Staff's date of birth.
     * @throws ValidationExceptions.ValidationException If any field is invalid.
     */
    public void staffValidator (
            String firstName,
            String lastName,
            String email,
            String password,
            String gender,
            LocalDate dateOfBirth) throws ValidationExceptions.ValidationException {
        userValidator(firstName, lastName, email, password, gender, dateOfBirth);
    }
    

    /**
     * Validates that a string is not null or empty.
     * @param value The string value to check.
     * @param fieldName The name of the field for the exception message.
     * @throws ValidationExceptions.EmptyFieldException If the string is empty.
     */
    private void validateRequiredText(String value, String fieldName)
            throws ValidationExceptions.EmptyFieldException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException(fieldName);
        }
    }

    /**
     * Validates that an email is in a valid format.
     * @param email The email string to check.
     * @throws ValidationExceptions.ValidationException If the email is empty or invalid.
     */
    private void validateEmail(String email)
            throws ValidationExceptions.ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationExceptions.EmptyFieldException("Email");
        }

        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.com$")) {
            throw new ValidationExceptions.InvalidEmailException();
        }
    }

    /**
     * Checks if an email is already registered in the system.
     * @param email The email to check.
     * @throws ValidationExceptions.DuplicateEmailException If the email exists.
     */
    private void validateDuplicateEmail(String email)
            throws ValidationExceptions.DuplicateEmailException {
        if (repo.getPatientByEmail(email) != null) {
            throw new ValidationExceptions.DuplicateEmailException();
        }
    }

    /**
     * Validates that the gender matches expected values.
     * @param gender The gender string.
     * @throws ValidationExceptions.ValidationException If invalid.
     */
    private void validateGender(String gender)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(gender, "Gender");
        if (!gender.equalsIgnoreCase("Male")
                && !gender.equalsIgnoreCase("Female")
                && !gender.equalsIgnoreCase("Other")) {
            throw new ValidationExceptions.InvalidGenderException();
        }
    }

    /**
     * Validates the date of birth is not in the future and within reasonable limits.
     * @param dateOfBirth The date of birth.
     * @throws ValidationExceptions.ValidationException If invalid.
     */
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

    /**
     * Validates that a phone number is exactly 10 digits.
     * @param phoneNumber The phone number string.
     * @throws ValidationExceptions.ValidationException If invalid.
     */
    private void validatePhoneNumber(String phoneNumber)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(phoneNumber, "Phone number");
        if (!phoneNumber.replaceAll("\\D", "").matches("\\d{10}")) {
            throw new ValidationExceptions.InvalidPhoneNumberException();
        }
    }

    /**
     * Validates that a health card number is 9 or 10 digits.
     * @param healthCardNum The health card string.
     * @throws ValidationExceptions.ValidationException If invalid.
     */
    private void validateHealthCardNumber(String healthCardNum)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(healthCardNum, "Health card number");
        if (!healthCardNum.matches("\\d{9,10}")) {
            throw new ValidationExceptions.InvalidHealthCardException();
        }
    }

    /**
     * Validates that the specialization is not null and is a valid enum value.
     * @param specialization The Specialization enum.
     * @throws ValidationExceptions.ValidationException If invalid.
     */
    private void validateSpecialization(Specialization specialization)
            throws ValidationExceptions.ValidationException {
        if (specialization == null) {
            throw new ValidationExceptions.EmptyFieldException("Specialization");
        }else{
            for (Specialization spec : Specialization.values()) {
                if (spec.equals(specialization)) {
                    return;
                }
            }
            throw new ValidationExceptions.InvalidSpecializationException();
        }
    }

    /**
     * Validates that a medical license number has a minimum length.
     * @param licenseNumber The license string.
     * @throws ValidationExceptions.ValidationException If too short.
     */
    private void validateLicenseNumber(String licenseNumber)
            throws ValidationExceptions.ValidationException {
        validateRequiredText(licenseNumber, "License number");
        if (licenseNumber.length() < 5) {
            throw new ValidationExceptions.ValidationException("License number is too short.");
        }
    }
}
