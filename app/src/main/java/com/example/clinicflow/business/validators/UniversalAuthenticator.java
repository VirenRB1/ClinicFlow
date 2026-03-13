package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

public class UniversalAuthenticator implements UserAuthenticator {

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