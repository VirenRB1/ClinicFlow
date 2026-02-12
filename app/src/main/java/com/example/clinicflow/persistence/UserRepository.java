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
    void addPatient(Patient patient);

    Patient getPatientByEmail(String email);

    List<MedicalRecord> getMedicalRecords(String patientName);

}
