package com.example.clinicflow.business;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

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

    public Doctor findDoctorByEmail(String email) {
        return (Doctor) userRepository.getUserByEmail(email);
    }

    public String getFullName(String email) {
        Users user = findUserByEmail(email);
        if (user == null) {
            return "No User Found";
        }
        return user.getFullName();
    }

    //need implementation
    public List<Doctor> getDoctors() {
        return userRepository.getAllDoctors();
    }
}
