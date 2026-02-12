package com.example.clinicflow.business;

import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistory {
    private final UserRepository DATABASE;

    public MedicalHistory(UserRepository userRepository) {
        this.DATABASE = userRepository;
    }

    // Use this method to get a list of MedicalRecord in the frontend
    //Please note the list could also be empty so handle that case
    // Alice Brown has 2 records, Charlie Wilson has 1 record and Bob Davis has no records
    //The function will sort the records by date in descending order so the latest record will be first

    public List<MedicalRecord> getSortedMedicalHistoryForPatient(String patientName) {
        List<MedicalRecord> medicalRecords = DATABASE.getMedicalRecords(patientName);

        // handling null cases
        if( medicalRecords == null){
            return new ArrayList<>();
        }

        medicalRecords.sort((record1, record2) -> record2.getDate().compareTo(record1.getDate()));
        return medicalRecords;
    }

}
