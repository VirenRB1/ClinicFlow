package com.example.clinicflow.business.auth;

public class CredentialsValidator {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.com$";

    public void validate(String email, String password) throws AuthExceptions.AuthException {
        if (email == null || email.trim().isEmpty()) {
            throw new AuthExceptions.EmptyEmailException();
        }

        if (!email.trim().matches(EMAIL_PATTERN)) {
            throw new AuthExceptions.InvalidEmailException();
        }

        if (password == null || password.isEmpty()) {
            throw new AuthExceptions.EmptyPasswordException();
        }
    }
}