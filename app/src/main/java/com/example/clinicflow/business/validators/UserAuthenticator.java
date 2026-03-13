package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

/**
 * Interface for user authentication strategies.
 */
public interface UserAuthenticator {
    /**
     * Authenticates a user against the provided repository.
     * @param repo The repository to look up the user.
     * @param email The user's email.
     * @param password The user's password.
     * @return The authenticated User object.
     * @throws AuthExceptions.AuthException If authentication fails.
     */
    Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException;
}
