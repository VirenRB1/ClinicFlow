package com.example.clinicflow.business.creation;

import com.example.clinicflow.business.validators.UserSignupValidator;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

/**
 * Business class responsible for creating and deleting users in the system.
 * Handles validation before data persistence.
 */
public class ObjectCreation {

    private final UserRepository DATABASE;
    private final UserSignupValidator VALIDATOR;

    /**
     * Constructs ObjectCreation with a specific user repository.
     * @param userRepository The repository used for user data persistence.
     */
    public ObjectCreation(UserRepository userRepository) {
        this.DATABASE = userRepository;
        this.VALIDATOR = new UserSignupValidator(userRepository);
    }

    /**
     * Validates and adds a new patient to the database.
     */
    public boolean addPatientToDatabase(String firstName, String lastName, String email, String password, String gender,
                                        LocalDate dateOfBirth, String healthCardNum, String phoneNumber)
            throws ValidationExceptions.ValidationException {

        VALIDATOR.patientValidator(
                firstName,
                lastName,
                email,
                password,
                gender,
                dateOfBirth,
                healthCardNum,
                phoneNumber
        );

        DATABASE.addPatient(
                new Patient(firstName, lastName, email, password, gender, dateOfBirth, healthCardNum, phoneNumber)
        );

        return true;
    }
    
    /**
     * Validates and adds a new doctor to the database.
     * @return True if the doctor was successfully added.
     * @throws ValidationExceptions.ValidationException If any input fails validation.
     */
    public boolean addDoctorToDatabase(String firstName, String lastName, String email, String password, String gender,
                                       LocalDate dateOfBirth, Specialization specialization, String licenseNumber)
            throws ValidationExceptions.ValidationException {
        VALIDATOR.doctorValidator(
                firstName,
                lastName,
                email,
                password,
                gender,
                dateOfBirth,
                specialization,
                licenseNumber
        );
        DATABASE.addDoctor(
                new Doctor(firstName, lastName, email, password, gender, dateOfBirth, specialization, licenseNumber)
        );
        return true;
    }

    /**
     * Validates and adds a new staff member to the database.
     * @return True if the staff member was successfully added.
     * @throws ValidationExceptions.ValidationException If any input fails validation.
     */
    public boolean addStaffToDatabase(String firstName, String lastName, String email, String password, String gender,
                                      LocalDate dateOfBirth, String position)
            throws ValidationExceptions.ValidationException {
        VALIDATOR.staffValidator(
                firstName,
                lastName,
                email,
                password,
                gender,
                dateOfBirth
        );
        DATABASE.addStaff(
                new Staff(firstName, lastName, email, password, gender, dateOfBirth, position)
        );
        return true;
    }
    
    /**
     * Deletes a user from the system by their email.
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
