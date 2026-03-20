package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;

/**
 * Validator class for verifying login credentials format.
 */
public class CredentialsValidator {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.com$";

    /**
     * Validates that the email and password are in a correct format before
     * authentication.
     * 
     * @param email    The email to validate.
     * @param password The password to validate.
     * @throws AuthExceptions.AuthException If email or password is empty or
     *                                      invalid.
     */
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
