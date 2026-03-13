package com.example.clinicflow.business.exceptions;

public class AuthExceptions {

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }

    public static class EmptyEmailException extends AuthException {
        public EmptyEmailException() {
            super("No email entered.");
        }
    }

    public static class InvalidEmailException extends AuthException {
        public InvalidEmailException() {
            super("Invalid email. Email must be in the format name@domain.com");
        }
    }

    public static class EmptyPasswordException extends AuthException {
        public EmptyPasswordException() {
            super("No password entered.");
        }
    }

    public static class UserNotFoundException extends AuthException {
        public UserNotFoundException() {
            super("User not found.");
        }
    }

    public static class WrongPasswordException extends AuthException {
        public WrongPasswordException() {
            super("Incorrect password.");
        }
    }
}