package com.example.clinicflow.persistence.fake;

import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.persistence.UserRepository;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
// Fake temporary database
public class FakeUserRepository implements UserRepository, Serializable {
//Lists of doctors, patients and staffs
    List<Doctor> doctors;
    List<Patient> patients;
    List<Staff> staffs;

    HashMap<String, List<MedicalRecord>> medicalRecords;

    public FakeUserRepository() {
        createFakeData();
    }

    //Create database
    private void createFakeData() {
        initializeFakeDoctors();
        initializeFakePatients();
        initializeFakeStaffs();
        intializeFakeMedicalRecords();
    }
    //Add doctors
    private void initializeFakeDoctors() {
        doctors = new ArrayList<>();
        doctors.add(new Doctor("John", "Doe", "johndoe@clinicdoc.com", "pass1", "Male", 25, "Cardiology", "LIC12345"));
        doctors.add(
                new Doctor("Jane", "Smith", "janesmith@clinicdoc.com", "pass2", "Female", 30, "Neurology", "LIC67890"));
        doctors.add(new Doctor("Emily", "Johnson", "emilyjohnson@clinicdoc.com", "pass3", "Female", 40, "Pediatrics",
                "LIC54321"));
    }
    // Add patienta
    private void initializeFakePatients() {
        patients = new ArrayList<>();
        patients.add(new Patient("Alice", "Brown", "alicebrown@gmail.com", "pass4", "Female", 28, 123456, 5551234));
        patients.add(new Patient("Bob", "Davis", "bobdavis@gmail.com", "pass5", "Male", 35, 654321, 5555678));
        patients.add(new Patient("Charlie", "Wilson", "charliewilson@gmail.com", "pass6", "Male", 45, 789012, 5559012));
    }
    // Add staffs
    private void initializeFakeStaffs() {
        staffs = new ArrayList<>();
        staffs.add(new Staff("Eve", "Miller", "evemiller@clinicstaff.com", "pass7", "Female", 32, "Receptionist"));
        staffs.add(new Staff("Frank", "Garcia", "frankgarcia@clinicstaff.com", "pass8", "Male", 29, "Receptionist"));
        staffs.add(new Staff("Grace", "Martinez", "gracemartinez@clinicstaff.com", "pass9", "Female", 38,
                "Administrator"));
    }
    // Add medical records
    private void intializeFakeMedicalRecords() {
        medicalRecords = new HashMap<>();
        medicalRecords.put("alicebrown@gmail.com", new ArrayList<>());
        medicalRecords.put("bobdavis@gmail.com", new ArrayList<>());
        medicalRecords.put("charliewilson@gmail.com", new ArrayList<>());

        MedicalRecord record1 = new MedicalRecord("Alice Brown", "John Doe", "alicebrown@gmail.com", "Check-up", "Regular check-up",
                new Date());
        MedicalRecord record2 = new MedicalRecord("Alice Brown", "Jane Smith", "alicebrown@gmail.com", "Follow-up", "Follow-up visit",
                new Date());
        MedicalRecord record3 = new MedicalRecord("Charlie Wilson", "Emily Johnson", "charliewilson@gmail.com", "Prescription",
                "Prescribed medication", new Date());

        medicalRecords.get("alicebrown@gmail.com").add(record1);
        medicalRecords.get("alicebrown@gmail.com").add(record2);
        medicalRecords.get("charliewilson@gmail.com").add(record3);
    }

    public List<MedicalRecord> getMedicalRecords(String patientEmail) {
        return medicalRecords.getOrDefault(patientEmail, new ArrayList<>());
    }
    //  Get methods
    @Override
    public List<Patient> getAllPatients() {
        return Collections.unmodifiableList(patients);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    @Override
    public List<Staff> getAllStaffs() {
        return Collections.unmodifiableList(staffs);
    }

    // Add a patient to database
    @Override
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void addMedicalRecord(MedicalRecord record) {
        String email = record.getEmail();
        if (!medicalRecords.containsKey(email)) {
            medicalRecords.put(email, new ArrayList<>());
        }
        medicalRecords.get(email).add(record);
    }

    // Get a patient by email
    @Override
    public Patient getPatientByEmail(String email) {
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                return patient;
            }
        }
        return null;
    }

}
