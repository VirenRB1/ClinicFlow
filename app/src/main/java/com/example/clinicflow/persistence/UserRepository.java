package com.example.clinicflow.persistence;

import com.example.clinicflow.model.Doctor;
import com.example.clinicflow.model.Patient;
import com.example.clinicflow.model.Staff;

import java.util.List;

public interface UserRepository {
    List<Patient> getAllPatients();

    void addDoctor(Doctor doctor);
    void addPatient(Patient patient);
    void addStaff(Staff staff);

    Doctor getDoctorById(int id);
    Patient getPatientById(int id);
    staff getStaffById(int id);

    boolean deleteDoctor(int id);
    boolean deletePatient(int id);
    boolean deleteStaff(int id);
}
