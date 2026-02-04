package com.example.clinicflow.persistence;

import com.example.clinicflow.model.Doctor;
import com.example.clinicflow.model.Patient;
import com.example.clinicflow.model.OfficeStaff;

import java.util.List;

public interface UserRepository {
    List<Patient> getAllPatients();

    void addDoctor(Doctor doctor);
    void addPatient(Patient patient);
    void addOfficeStaff(OfficeStaff officeStaff);

    Doctor getDoctorById(int id);
    Patient getPatientById(int id);
    OfficeStaff getOfficeStaffById(int id);

    boolean deleteDoctor(int id);
    boolean deletePatient(int id);
    boolean deleteOfficeStaff(int id);
}
