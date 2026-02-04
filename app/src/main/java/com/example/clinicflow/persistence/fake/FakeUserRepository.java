package com.example.clinicflow.persistence.fake;

import com.example.clinicflow.persistence.UserRepository;

import com.example.clinicflow.model.Doctor;
import com.example.clinicflow.model.Patient;
import com.example.clinicflow.model.OfficeStaff;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FakeUserRepository implements UserRepository {

    List <Doctor> doctors = new ArrayList<>();
    List <Patient> patients = new ArrayList<>();
    List <OfficeStaff> officeStaffs = new ArrayList<>();

    @Override
    public void getAllPatients() {
        return Collections.unmodifiableList(patients);
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
    public void addOfficeStaff(OfficeStaff officeStaff){
        officeStaffs.add(officeStaff);
    }

    @Override
    public Doctor getDoctorById(int id){
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    @Override
    public Patient getPatientById(int id){
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public OfficeStaff getOfficeStaffById(int id){
        for (OfficeStaff officeStaff : officeStaffs) {
            if (officeStaff.getId() == id) {
                return officeStaff;
            }
        }
        return null;
    }

    @Override
    public boolean deleteDoctor(int id){
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                doctors.remove(doctor);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePatient(int id){
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                patients.remove(patient);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteOfficeStaff(int id){
        for (OfficeStaff officeStaff : officeStaffs) {
            if (officeStaff.getId() == id) {
                officeStaffs.remove(officeStaff);
                return true;
            }
        }
        return null;
    }
}