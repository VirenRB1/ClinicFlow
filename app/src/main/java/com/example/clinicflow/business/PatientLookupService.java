package com.example.clinicflow.business;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;

public class PatientLookupService {
    private final UserRepository userRepository;

    public PatientLookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Patient findPatientByEmail(String email) {
        return userRepository.getPatientByEmail(email);
    }
}
