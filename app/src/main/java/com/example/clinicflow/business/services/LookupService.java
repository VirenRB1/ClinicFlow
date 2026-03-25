package com.example.clinicflow.business.services;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

/**
 * Service class for looking up users and user-related information.
 */
public class LookupService {
    private final UserRepository userRepository;

    /**
     * Constructs a LookupService with the provided user repository.
     * 
     * @param userRepository The repository to use for data retrieval.
     */
    public LookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds any user by their email address.
     * 
     * @param email The email to search for.
     * @return The User object if found, null otherwise.
     */
    public Users findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    /**
     * Specifically finds a patient by their email.
     * 
     * @param email The patient's email.
     * @return The Patient object if found, null otherwise.
     */
    public Patient findPatientByEmail(String email) {
        return userRepository.getPatientByEmail(email);
    }

    /**
     * Specifically finds a doctor by their email.
     * 
     * @param email The doctor's email.
     * @return The Doctor object if found, null otherwise.
     */
    public Doctor findDoctorByEmail(String email) {
        return userRepository.getDoctorByEmail(email);
    }

    /**
     * Gets the full name of a user based on their email.
     * 
     * @param email The user's email.
     * @return The user's full name, or "No User Found" if not found.
     */
    public String getFullName(String email) {
        Users user = findUserByEmail(email);
        if (user == null) {
            return "No User Found";
        }
        return user.getFullName();
    }

    /**
     * Retrieves a list of all doctors in the system.
     * 
     * @return A list of all Doctor objects.
     */
    public List<Doctor> getDoctors() {
        return userRepository.getAllDoctors();
    }
}
