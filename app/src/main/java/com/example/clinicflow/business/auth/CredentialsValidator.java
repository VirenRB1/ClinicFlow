package com.example.clinicflow.business.auth;

public class CredentialsValidator {

    public void validate(String email, String password) throws AuthExceptions.InvalidInputException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new AuthExceptions.InvalidInputException();
        }
        if (password == null || password.isEmpty()) {
            throw new AuthExceptions.InvalidInputException();
        }
    }
}