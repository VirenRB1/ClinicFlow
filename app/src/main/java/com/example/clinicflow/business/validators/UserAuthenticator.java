package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.AuthExceptions;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

public interface UserAuthenticator {
    Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException;
}