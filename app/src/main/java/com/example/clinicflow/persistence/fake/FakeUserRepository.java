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

public class FakeUserRepository implements UserRepository, Serializable {

    List <Doctor> doctors;
    List <Patient> patients;
    List <Staff> staffs;

    HashMap<String, List<MedicalRecord>> medicalRecords;


    public FakeUserRepository(){
        createFakeData();
    }

    private void createFakeData(){
        initializeFakeDoctors();
        initializeFakePatients();
        initializeFakeStaffs();
        intializeFakeMedicalRecords();
    }

    private void initializeFakeDoctors(){
        doctors = new ArrayList<>();
        doctors.add(new Doctor("John","Doe","johndoe@clinicdoc.com","pass1","Male",25,"Cardiology","LIC12345"));
        doctors.add(new Doctor("Jane","Smith","janesmith@clinicdoc.com","pass2","Female",30,"Neurology","LIC67890"));
        doctors.add(new Doctor("Emily","Johnson","emilyjohnson@clinicdoc.com","pass3","Female",40,"Pediatrics","LIC54321"));
    }

    private void initializeFakePatients(){
        patients = new ArrayList<>();
        patients.add(new Patient("Alice","Brown","alicebrown@gmail.com","pass4","Female",28,123456,5551234));
        patients.add(new Patient("Bob","Davis","bobdavis@gmail.com","pass5","Male",35,654321,5555678));
        patients.add(new Patient("Charlie","Wilson","charliewilson@gmail.com","pass6","Male",45,789012,5559012));
    }

    private void initializeFakeStaffs(){
        staffs = new ArrayList<>();
        staffs.add(new Staff("Eve","Miller","evemiller@clinicstaff.com","pass7","Female",32,"Receptionist"));
        staffs.add(new Staff("Frank","Garcia","frankgarcia@clinicstaff.com","pass8","Male",29,"Receptionist"));
        staffs.add(new Staff("Grace","Martinez","gracemartinez@clinicstaff.com","pass9","Female",38,"Administrator"));
    }

    private void intializeFakeMedicalRecords(){
        medicalRecords = new HashMap<>();
        medicalRecords.put("Alice Brown", new ArrayList<>());
        medicalRecords.put("Bob Davis", new ArrayList<>());
        medicalRecords.put("Charlie Wilson", new ArrayList<>());

        MedicalRecord record1 = new MedicalRecord("Alice Brown", "John Doe", "Check-up", "Regular check-up", new Date());
        MedicalRecord record2 = new MedicalRecord("Alice Brown", "Jane Smith", "Follow-up", "Follow-up visit", new Date());
        MedicalRecord record3 = new MedicalRecord("Charlie Wilson", "Emily Johnson", "Prescription", "Prescribed medication", new Date());

        medicalRecords.get("Alice Brown").add(record1);
        medicalRecords.get("Alice Brown").add(record2);
        medicalRecords.get("Charlie Wilson").add(record3);
    }

    public List<MedicalRecord> getMedicalRecords(String patientName) {
        return medicalRecords.getOrDefault(patientName, new ArrayList<>());
    }


    @Override
    public List <Patient> getAllPatients() {
        return Collections.unmodifiableList(patients);
    }

    @Override
    public List <Doctor> getAllDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    @Override
    public List <Staff> getAllStaffs() {
        return Collections.unmodifiableList(staffs);
    }

    @Override
    public void addDoctor(Doctor doctor){
        doctors.add(doctor);
    }

    @Override
    public void addPatient(Patient patient){
        patients.add(patient);
    }

    @Override
    public void addStaff(Staff staff){
        staffs.add(staff);
    }

    @Override
    public Doctor getDoctorByFullName(String fullName){
        for (Doctor doctor : doctors) {
            if (doctor.getFullName().equals(fullName)) {
                return doctor;
            }
        }
        return null;
    }

    @Override
    public Patient getPatientByFullName(String fullName){
        for (Patient patient : patients) {
            if (patient.getFullName().equals(fullName)) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public Staff getStaffByFullName(String fullName){
        for (Staff staff : staffs) {
            if (staff.getFullName().equals(fullName)) {
                return staff;
            }
        }
        return null;
    }

    @Override
    public boolean deleteDoctorByFullName(String fullName){
        for (Doctor doctor : doctors) {
            if (doctor.getFullName().equals(fullName)) {
                doctors.remove(doctor);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePatientByFullName(String fullName){
        for (Patient patient : patients) {
            if (patient.getFullName().equals(fullName)) {
                patients.remove(patient);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteStaffByFullName(String fullName){
        for (Staff staff : staffs) {
            if (staff.getFullName().equals(fullName)) {
                staffs.remove(staff);
                return true;
            }
        }
        return false;
    }
}