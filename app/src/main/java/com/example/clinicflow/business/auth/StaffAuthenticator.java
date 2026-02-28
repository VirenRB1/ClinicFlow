package com.example.clinicflow.business.auth;

import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

public class StaffAuthenticator implements UserAuthenticator {

    @Override
    public boolean supports(String email) {
        return email != null && email.contains("@clinicstaff.com");
    }

    @Override
    public Users authenticate(UserRepository repo, String email, String password)
            throws AuthExceptions.AuthException {

        List<Staff> staffs = repo.getAllStaffs();

        for (Staff s : staffs) {
            if (s.getEmail().equalsIgnoreCase(email)) {
                if (s.getPassword().equals(password)) {
                    return s;
                }
                throw new AuthExceptions.WrongPasswordException();
            }
        }
        throw new AuthExceptions.UserNotFoundException();
    }
}