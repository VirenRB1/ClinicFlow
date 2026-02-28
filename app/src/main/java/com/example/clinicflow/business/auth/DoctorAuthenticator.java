package com.example.clinicflow.business.auth;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

public class DoctorAuthenticator implements UserAuthenticator {

    @Override
    public boolean supports(String email) {
        return email != null && email.contains("@clinicdoc.com");
    }

    @Override
    public Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException {

        List<Doctor> doctors = repo.getAllDoctors();

        for (Doctor d : doctors) {
            if (d.getEmail().equalsIgnoreCase(email)) {
                if (d.getPassword().equals(password)) {
                    return d;
                }
                throw new AuthExceptions.WrongPasswordException();
            }
        }
        throw new AuthExceptions.UserNotFoundException();
    }
}