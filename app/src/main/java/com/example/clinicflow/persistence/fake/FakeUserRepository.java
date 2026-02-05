package com.example.clinicflow.persistence.fake;

import com.example.clinicflow.persistence.UserRepository;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FakeUserRepository implements UserRepository {

    List <Doctor> doctors;
    List <Patient> patients;
    List <Staff> staffs;

    public FakeUserRepository(){
        createFakeData();
    }

    private void createFakeData(){
        initializeFakeDoctors();
        initializeFakePatients();
        initializeFakeStaffs();
    }

    private void initializeFakeDoctors(){
        doctors = new ArrayList<>();
        doctors.add(new Doctor("John","Doe","johndoe@clinicdoc.com","pass1","Male",25,"Cardiology","LIC12345"));
        doctors.add(new Doctor("Jane","Smith","janesmith@clinicdoc.com","pass2","Female",30,"Neurology","LIC67890"));
        doctors.add(new Doctor("Emily","Johnson","emilyjohnson@clinicdoc.com","pass3","Female",40,"Pediatrics","LIC54321"));
    }

    private void initializeFakePatients(){
        patients = new ArrayList<>();
        patients.add(new Patient("Alice","Brown","alicebrown@gmail.com","pass4","Female",28,123456,"No significant history",5551234));
        patients.add(new Patient("Bob","Davis","bobdavis@gmail.com","pass5","Male",35,654321,"Allergic to penicillin",5555678));
        patients.add(new Patient("Charlie","Wilson","charliewilson@gmail.com","pass6","Male",45,789012,"Diabetic",5559012));
    }

    private void initializeFakeStaffs(){
        staffs = new ArrayList<>();
        staffs.add(new Staff("Eve","Miller","evemiller@clinicstaff.com","pass7","Female",32,"Receptionist"));
        staffs.add(new Staff("Frank","Garcia","frankgarcia@clinicstaff.com","pass8","Male",29,"Receptionist"));
        staffs.add(new Staff("Grace","Martinez","gracemartinez@clinicstaff.com","pass9","Female",38,"Administrator"));
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