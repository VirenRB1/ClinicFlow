package com.example.clinicflow.business.auth;
public class AuthExceptions {

    // Base class for all auth errors
    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }

    // Invalid email format or empty password
    public static class InvalidInputException extends AuthException {
        public InvalidInputException() {
            super("Invalid email format or empty password.");
        }
    }

    // User email not found in system
    public static class UserNotFoundException extends AuthException {
        public UserNotFoundException() {
            super("Account not exist.");
        }
    }

    // Password does not match
    public static class WrongPasswordException extends AuthException {
        public WrongPasswordException() {
            super("Incorrect password.");
        }
    }
}