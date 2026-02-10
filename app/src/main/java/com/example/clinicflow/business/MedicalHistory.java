package com.example.clinicflow.business;

import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import java.util.List;

public class MedicalHistory {
    private final FakeUserRepository DATABASE;

    public MedicalHistory(FakeUserRepository userRepository) {
        this.DATABASE = userRepository;
    }

    // Use this method to get a list of MedicalRecord in the frontend
    //Please note the list could also be empty so handle that case
    // Alice Brown has 2 records, Charlie Wilson has 1 record and Bob Davis has no records
    //The function will sort the records by date in descending order so the latest record will be first
    public List<MedicalRecord> getSortedMedicalHistoryForPatient(String patientName) {
        List<MedicalRecord> medicalRecords = DATABASE.getMedicalRecords(patientName);
        medicalRecords.sort((record1, record2) -> record2.getDate().compareTo(record1.getDate()));
        return medicalRecords;
    }

    public String getPatientNameByEmail(String email) {
        List<Patient> patients = DATABASE.getAllPatients();
        for (Patient patient : patients) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return patient.getFullName();
            }
        }
        return null;
    }

}
