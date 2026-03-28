package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import java.util.List;

public interface UserPersistence {
    List<Patient> getAllPatients();
    List<Doctor> getAllDoctors();
    List<Staff> getAllStaffs();
    List<Admin> getAllAdmins();
    
    void addPatient(Patient patient);
    void addDoctor(Doctor doctor);
    void addStaff(Staff staff);
    void deleteUser(Users user);
    
    Patient getPatientByEmail(String email);
    Doctor getDoctorByEmail(String email);
    Staff getStaffByEmail(String email);
    Admin getAdminByEmail(String email);
    Users getUserByEmail(String email);
}
