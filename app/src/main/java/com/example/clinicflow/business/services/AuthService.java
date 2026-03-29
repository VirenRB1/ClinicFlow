package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.business.validators.CredentialsValidator;
import com.example.clinicflow.business.validators.UniversalAuthenticator;
import com.example.clinicflow.business.validators.UserAuthenticator;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserPersistence;

/**
 * Service handling user authentication logic.
 */
public class AuthService {

    private final UserPersistence repo;
    private final CredentialsValidator validator;
    private final UserAuthenticator authenticator;

    /**
     * Default constructor using UniversalAuthenticator.
     * 
     * @param userRepository Repository for user data.
     */
    public AuthService(UserPersistence userRepository) {
        this(userRepository, new CredentialsValidator(), new UniversalAuthenticator());
    }

    /**
     * Flexible constructor for dependency injection.
     * 
     * @param userRepository Repository for user data.
     * @param validator      Validator for email/password formats.
     * @param authenticator  Strategy for verifying credentials.
     */
    public AuthService(UserPersistence userRepository, CredentialsValidator validator, UserAuthenticator authenticator) {
        this.repo = userRepository;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    /**
     * Authenticates a user, returning null on failure.
     * 
     * @param email    User email.
     * @param password User password.
     * @return The authenticated User object, or null if failed.
     */
    public Users authenticate(String email, String password) {
        try {
            return authenticateOrThrow(email, password);
        } catch (AuthExceptions.AuthException e) {
            return null;
        }
    }

    /**
     * Authenticates a user or throws a specific AuthException.
     * 
     * @param email    User email.
     * @param password User password.
     * @return The authenticated User object.
     * @throws AuthExceptions.AuthException If validation or authentication fails.
     */
    public Users authenticateOrThrow(String email, String password) throws AuthExceptions.AuthException {
        String normalizedEmail = email == null ? null : email.trim();

        validator.validate(normalizedEmail, password);
        return authenticator.authenticate(repo, normalizedEmail, password);
    }
}
