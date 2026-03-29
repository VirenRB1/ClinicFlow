package com.example.clinicflow.business.creation;

import com.example.clinicflow.business.validators.UserSignupValidator;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserPersistence;

import java.time.LocalDate;

/**
 * Business class responsible for creating and deleting users in the system.
 * Handles validation before data persistence.
 */
public class ObjectCreation {

    private final UserPersistence DATABASE;
    private final UserSignupValidator VALIDATOR;

    /**
     * Constructs ObjectCreation with a specific user persistence.
     * 
     * @param userPersistence The persistence used for user data.
     */
    public ObjectCreation(UserPersistence userPersistence) {
        this.DATABASE = userPersistence;
        this.VALIDATOR = new UserSignupValidator(userPersistence);
    }

    public boolean addUserToDatabase(UserRole role, String firstName, String lastName, String email, String password, String confirmPw, String gender,
                                     LocalDate dateOfBirth, String healthCardNum, String phoneNumber, String specialization, String licenseNumber, String position) throws ValidationExceptions.ValidationException {
        if (role == UserRole.PATIENT) {
            return addPatientToDatabase(firstName, lastName, email, password, confirmPw, gender, dateOfBirth, healthCardNum, phoneNumber);
        } else if (role == UserRole.DOCTOR) {
            return addDoctorToDatabase(firstName, lastName, email, password, confirmPw, gender, dateOfBirth, parseSpecialization(specialization), licenseNumber);
        } else if (role == UserRole.STAFF) {
            return addStaffToDatabase(firstName, lastName, email, password, confirmPw, gender, dateOfBirth, position);
        }
        return false;
    }

    private Specialization parseSpecialization(String spec) {
        if (spec == null || spec.trim().isEmpty()) {
            return null;
        }
        try {
            return Specialization.valueOf(spec.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Validates and adds a new patient to the database.
     */
    public boolean addPatientToDatabase(String firstName, String lastName, String email, String password, String confirmPW, String gender,
            LocalDate dateOfBirth, String healthCardNum, String phoneNumber)
            throws ValidationExceptions.ValidationException {

        VALIDATOR.patientValidator(
                firstName,
                lastName,
                email,
                password,
                confirmPW,
                gender,
                dateOfBirth,
                healthCardNum,
                phoneNumber);

        DATABASE.addPatient(
                new Patient(firstName, lastName, email, password, gender, dateOfBirth, healthCardNum, phoneNumber));

        return true;
    }

    /**
     * Validates and adds a new doctor to the database.
     * 
     * @return True if the doctor was successfully added.
     * @throws ValidationExceptions.ValidationException If any input fails
     *                                                  validation.
     */
    public boolean addDoctorToDatabase(String firstName, String lastName, String email, String password, String confirmPW, String gender,
            LocalDate dateOfBirth, Specialization specialization, String licenseNumber)
            throws ValidationExceptions.ValidationException {
        VALIDATOR.doctorValidator(
                firstName,
                lastName,
                email,
                password,
                confirmPW,
                gender,
                dateOfBirth,
                specialization,
                licenseNumber);
        DATABASE.addDoctor(
                new Doctor(firstName, lastName, email, password, gender, dateOfBirth, specialization, licenseNumber));
        return true;
    }

    /**
     * Validates and adds a new staff member to the database.
     * 
     * @return True if the staff member was successfully added.
     * @throws ValidationExceptions.ValidationException If any input fails
     *                                                  validation.
     */
    public boolean addStaffToDatabase(String firstName, String lastName, String email, String password, String confirmPW, String gender,
            LocalDate dateOfBirth, String position)
            throws ValidationExceptions.ValidationException {
        VALIDATOR.staffValidator(
                firstName,
                lastName,
                email,
                password,
                confirmPW,
                gender,
                dateOfBirth);
        DATABASE.addStaff(
                new Staff(firstName, lastName, email, password, gender, dateOfBirth, position));
        return true;
    }

    /**
     * Deletes a user from the system by their email.
     * 
     * @param email The email of the user to delete.
     * @return True if a user was found and deleted, false otherwise.
     */
    public boolean deleteUser(String email) {
        Users user = DATABASE.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        DATABASE.deleteUser(user);
        return true;
    }
}
