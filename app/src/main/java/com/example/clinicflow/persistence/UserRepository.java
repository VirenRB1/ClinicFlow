package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import java.util.List;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import java.time.LocalDate;

public interface UserRepository {
    List<Patient> getAllPatients();

    List<Doctor> getAllDoctors();

    List<Staff> getAllStaffs();
    
    List<Admin> getAllAdmins();

    void addPatient(Patient patient);

    void addDoctor(Doctor doctor);

    void addStaff(Staff staff);

    void deleteUser(Users user);

    Patient getPatientByEmail(String email);

    Users getUserByEmail(String email);

    List<MedicalRecord> getMedicalRecords(String patientEmail);

    void addMedicalRecord(MedicalRecord record);
    void addAppointment(Appointment appointment);

    void addDoctorAvailability(DoctorAvailability availability);

    List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date);

}