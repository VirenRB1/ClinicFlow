package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

/**
 * Concrete implementation of UserAuthenticator that works for any user type.
 * Compares plain-text passwords (not for production use).
 */
public class UniversalAuthenticator implements UserAuthenticator {

    /**
     * Authenticates a user by checking if they exist and if their password matches.
     * 
     * @param repo     The repository to look up the user.
     * @param email    The user's email.
     * @param password The user's password.
     * @return The User object if authentication is successful.
     * @throws AuthExceptions.AuthException If the user is not found or the password
     *                                      is incorrect.
     */
    @Override
    public Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException {

        Users user = repo.getUserByEmail(email);

        if (user == null) {
            throw new AuthExceptions.UserNotFoundException();
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthExceptions.WrongPasswordException();
        }

        return user;
    }
}
