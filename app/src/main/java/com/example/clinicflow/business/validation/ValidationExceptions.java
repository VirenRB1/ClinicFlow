package com.example.clinicflow.business.validation;

public class ValidationExceptions {

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class EmptyFieldException extends ValidationException {
        public EmptyFieldException(String fieldName) {
            super(fieldName + " cannot be empty.");
        }
    }

    public static class InvalidEmailException extends ValidationException {
        public InvalidEmailException() {
            super("Invalid email format.");
        }
    }

    public static class DuplicateEmailException extends ValidationException {
        public DuplicateEmailException() {
            super("Email already exists.");
        }
    }

    public static class InvalidGenderException extends ValidationException {
        public InvalidGenderException() {
            super("Gender must be Male, Female, or Other.");
        }
    }

    public static class InvalidDateOfBirthException extends ValidationException {
        public InvalidDateOfBirthException(String message) {
            super(message);
        }
    }

    public static class InvalidPhoneNumberException extends ValidationException {
        public InvalidPhoneNumberException() {
            super("Phone number must be 10 digits.");
        }
    }

    public static class InvalidHealthCardException extends ValidationException {
        public InvalidHealthCardException() {
            super("Health card number must be 9 or 10 digits.");
        }
    }
}