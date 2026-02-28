package com.example.clinicflow.business.auth;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

public class PatientAuthenticator implements UserAuthenticator {

    @Override
    public boolean supports(String email) {
        return true; // fallback
    }

    @Override
    public Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException {

        List<Patient> patients = repo.getAllPatients();

        for (Patient p : patients) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                if (p.getPassword().equals(password)) {
                    return p;
                }
                throw new AuthExceptions.WrongPasswordException();
            }
        }
        throw new AuthExceptions.UserNotFoundException();
    }
}