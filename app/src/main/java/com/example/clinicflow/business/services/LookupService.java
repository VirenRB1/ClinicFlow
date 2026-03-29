package com.example.clinicflow.business.services;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserPersistence;

import java.util.List;

/**
 * Service class for looking up users and user-related information.
 */
public class LookupService {
    private final UserPersistence userPersistence;

    /**
     * Constructs a LookupService with the provided user persistence.
     * 
     * @param userPersistence The persistence to use for data retrieval.
     */
    public LookupService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    /**
     * Finds any user by their email address.
     * 
     * @param email The email to search for.
     * @return The User object if found, null otherwise.
     */
    public Users findUserByEmail(String email) {
        return userPersistence.getUserByEmail(email);
    }

    /**
     * Specifically finds a patient by their email.
     * 
     * @param email The patient's email.
     * @return The Patient object if found, null otherwise.
     */
    public Patient findPatientByEmail(String email) {
        return userPersistence.getPatientByEmail(email);
    }

    /**
     * Specifically finds a doctor by their email.
     * 
     * @param email The doctor's email.
     * @return The Doctor object if found, null otherwise.
     */
    public Doctor findDoctorByEmail(String email) {
        return userPersistence.getDoctorByEmail(email);
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
        return userPersistence.getAllDoctors();
    }
}
