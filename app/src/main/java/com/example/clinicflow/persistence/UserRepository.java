package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;

import java.util.List;

public interface UserRepository {
    List <Patient> getAllPatients();
    List <Doctor> getAllDoctors();
    List <Staff> getAllStaffs();

    void addDoctor(Doctor doctor);
    void addPatient(Patient patient);
    void addStaff(Staff staff);

    Doctor getDoctorByFullName(String fullName);
    Patient getPatientByFullName(String fullName);
    Staff getStaffByFullName(String fullName);

    List<MedicalRecord> getMedicalRecords(String patientName);


    boolean deleteDoctorByFullName(String fullName);
    boolean deletePatientByFullName(String fullName);
    boolean deleteStaffByFullName(String fullName);
}
