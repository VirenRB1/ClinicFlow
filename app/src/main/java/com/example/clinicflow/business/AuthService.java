package com.example.clinicflow.business;

import com.example.clinicflow.business.auth.AuthExceptions;
import com.example.clinicflow.business.auth.CredentialsValidator;
import com.example.clinicflow.business.auth.DoctorAuthenticator;
import com.example.clinicflow.business.auth.PatientAuthenticator;
import com.example.clinicflow.business.auth.StaffAuthenticator;
import com.example.clinicflow.business.auth.UserAuthenticator;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private final UserRepository repo;
    private final CredentialsValidator validator;
    private final List<UserAuthenticator> authenticators;

    public AuthService(UserRepository userRepository) {
        this.repo = userRepository;
        this.validator = new CredentialsValidator();

        this.authenticators = new ArrayList<>();
        this.authenticators.add(new StaffAuthenticator());
        this.authenticators.add(new DoctorAuthenticator());
        this.authenticators.add(new PatientAuthenticator()); // fallback last
    }

    // ✅ Old method stays (same name/signature/behavior)
    public Users authenticate(String email, String password) {
        try {
            return authenticateOrThrow(email, password);
        } catch (AuthExceptions.AuthException e) {
            return null;
        }
    }

    // ✅ New “better” method
    public Users authenticateOrThrow(String email, String password) throws AuthExceptions.AuthException {
        validator.validate(email, password);

        UserAuthenticator authenticator = findAuthenticator(email);
        return authenticator.authenticate(repo, email, password);
    }

    private UserAuthenticator findAuthenticator(String email) {
        for (UserAuthenticator a : authenticators) {
            if (a.supports(email)) {
                return a;
            }
        }
        // fallback safety (should never happen)
        return new PatientAuthenticator();
    }
}