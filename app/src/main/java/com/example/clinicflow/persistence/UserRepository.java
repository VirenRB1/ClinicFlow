package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
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
    
    void addAppointment(Appointment appointment);
    
    void updateAppointment(Appointment appointment);

    void addDoctorAvailability(DoctorAvailability availability);

    List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek);

    List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date);

    List<Appointment> getAppointmentsForPatient(String patientEmail);
}