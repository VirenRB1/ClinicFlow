package com.example.clinicflow.business;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

//Authentication
//Receive an email and password, return corresponding valid user or error if the input is invalid
public class AuthService {
    private final UserRepository DATABASE;

    public AuthService(UserRepository userRepository) {
        this.DATABASE = userRepository;
    }
//Accept email and password to check for validation
    public Users authenticate(String email, String password) {
        Users myUser;
        if (!formatCheck(email, password)) {
            myUser = null;
        } else {
            if (email.contains("@clinicstaff.com")) {
                myUser = validateStaff(email, password);
            } else if (email.contains("@clinicdoc.com")) {
                myUser = validateDoctor(email, password);
            } else {
                myUser = validatePatient(email, password);
            }
        }
        return myUser;
    }
// Validate 3 type of user, compare the input email and password to the existing users in database

    //Check patient
    private Users validatePatient(String email, String password) {
        List<Patient> patients = DATABASE.getAllPatients();
        for (Patient patient : patients) {
            if (patient.getEmail().equalsIgnoreCase(email) && patient.getPassword().equals(password)) {
                return patient;
            }
        }
        return null;
    }
    // Check staff
    private Users validateStaff(String email, String password) {
        List<Staff> staffs = DATABASE.getAllStaffs();
        for (Staff staff : staffs) {
            if (staff.getEmail().equalsIgnoreCase(email) && staff.getPassword().equals(password)) {
                return staff;
            }
        }
        return null;
    }
    // Check doctor
    private Users validateDoctor(String email, String password) {
        List<Doctor> doctors = DATABASE.getAllDoctors();
        for (Doctor doc : doctors) {
            if (doc.getEmail().equalsIgnoreCase(email) && doc.getPassword().equals(password)) {
                return doc;
            }
        }
        return null;
    }
// Check valid email format
    private boolean formatCheck(String email, String password) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return false;
        }
        return password != null && !password.isEmpty();
    }
}
