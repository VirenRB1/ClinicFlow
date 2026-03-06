package com.example.clinicflow.business;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

public class LookupService {
    private final UserRepository userRepository;

    public LookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public Patient findPatientByEmail(String email) {
        return (Patient) userRepository.getUserByEmail(email);
    }
}
