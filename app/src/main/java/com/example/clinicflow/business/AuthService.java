package com.example.clinicflow.business;

import com.example.clinicflow.business.auth.AuthExceptions;
import com.example.clinicflow.business.auth.CredentialsValidator;
import com.example.clinicflow.business.auth.UniversalAuthenticator;
import com.example.clinicflow.business.auth.UserAuthenticator;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

public class AuthService {

    private final UserRepository repo;
    private final CredentialsValidator validator;
    private final UserAuthenticator authenticator;

    public AuthService(UserRepository userRepository) {
        this(userRepository, new CredentialsValidator(), new UniversalAuthenticator());
    }

    public AuthService(UserRepository userRepository, CredentialsValidator validator, UserAuthenticator authenticator) {
        this.repo = userRepository;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    public Users authenticate(String email, String password) {
        try {
            return authenticateOrThrow(email, password);
        } catch (AuthExceptions.AuthException e) {
            return null;
        }
    }

    public Users authenticateOrThrow(String email, String password) throws AuthExceptions.AuthException {
        String normalizedEmail = email == null ? null : email.trim();

        validator.validate(normalizedEmail, password);
        return authenticator.authenticate(repo, normalizedEmail, password);
    }
}
