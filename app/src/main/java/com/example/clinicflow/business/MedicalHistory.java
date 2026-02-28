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

    public List<MedicalRecord> getSortedMedicalHistoryForPatient(String patientEmail) {
        List<MedicalRecord> medicalRecords = DATABASE.getMedicalRecords(patientEmail);

        if (medicalRecords == null) {
            return new ArrayList<>();
        }

        medicalRecords.sort((record1, record2) -> record2.getDate().compareTo(record1.getDate()));
        return medicalRecords;
    }

}
