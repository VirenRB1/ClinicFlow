package com.example.clinicflow.business;

import com.example.clinicflow.business.validation.UserSignupValidator;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;

// Add an object in database
public class ObjectCreation {

    private final UserRepository DATABASE;
    private final UserSignupValidator VALIDATOR;

    public ObjectCreation(UserRepository userRepository) {
        this.DATABASE = userRepository;
        this.VALIDATOR = new UserSignupValidator(userRepository);
    }

    // Add patient with validation
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
    
    public boolean deleteUser(String email) {
        Users user = DATABASE.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        DATABASE.deleteUser(user);
        return true;
    }

        
}
