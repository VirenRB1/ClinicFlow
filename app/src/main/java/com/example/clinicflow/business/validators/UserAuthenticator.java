package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserPersistence;

/**
 * Interface for user authentication strategies.
 */
public interface UserAuthenticator {
    /**
     * Authenticates a user against the provided persistence layer.
     * 
     * @param repo     The persistence layer to look up the user.
     * @param email    The user's email.
     * @param password The user's password.
     * @return The authenticated User object.
     * @throws AuthExceptions.AuthException If authentication fails.
     */
    Users authenticate(UserPersistence repo, String email, String password)
            throws AuthExceptions.AuthException;
}
