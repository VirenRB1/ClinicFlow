package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;

import java.util.List;

public interface UserRepository {
    List<Patient> getAllPatients();

    List<Doctor> getAllDoctors();

    List<Staff> getAllStaffs();

    void addPatient(Patient patient);

    Patient getPatientByEmail(String email);

    Users getUserByEmail(String email);

    List<MedicalRecord> getMedicalRecords(String patientEmail);

    void addMedicalRecord(MedicalRecord record);
}