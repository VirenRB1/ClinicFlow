package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Staff;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public static List<Admin> getDefaultAdmins() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin("Admin", "Admin", "admin@clinic.com", "admin", "Female", LocalDate.of(1990, 1, 1)));
        return admins;
    }

    public static List<Doctor> getDefaultDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("John", "Doe", "johndoe@clinicdoc.com", "pass1", "Male", LocalDate.of(1999, 3, 12), Specialization.CARDIOLOGY, "LIC12345"));
        doctors.add(new Doctor("Jane", "Smith", "janesmith@clinicdoc.com", "pass2", "Female", LocalDate.of(2001, 7, 4), Specialization.NEUROLOGY, "LIC67890"));
        doctors.add(new Doctor("Emily", "Johnson", "emilyjohnson@clinicdoc.com", "pass3", "Female", LocalDate.of(2003, 3, 6), Specialization.PEDIATRICS, "LIC54321"));
        return doctors;
    }

    public static List<Patient> getDefaultPatients() {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Alice", "Brown", "alicebrown@gmail.com", "pass4", "Female", LocalDate.of(2000, 1, 1), "123456789", "2045551234"));
        patients.add(new Patient("Bob", "Davis", "bobdavis@gmail.com", "pass5", "Male", LocalDate.of(1999, 3, 12), "987654321", "4315555678"));
        patients.add(new Patient("Charlie", "Wilson", "charliewilson@gmail.com", "pass6", "Male", LocalDate.of(2001, 7, 4), "789012123", "4315559012"));
        return patients;
    }

    public static List<Staff> getDefaultStaffs() {
        List<Staff> staffs = new ArrayList<>();
        staffs.add(new Staff("Eve", "Miller", "evemiller@clinicstaff.com", "pass7", "Female", LocalDate.of(2003, 3, 6), "Receptionist"));
        staffs.add(new Staff("Frank", "Garcia", "frankgarcia@clinicstaff.com", "pass8", "Male", LocalDate.of(2001, 7, 4), "Receptionist"));
        staffs.add(new Staff("Grace", "Martinez", "gracemartinez@clinicstaff.com", "pass9", "Female", LocalDate.of(1999, 3, 3), "Administrator"));
        return staffs;
    }

    public static List<Appointment> getDefaultPastAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment("johndoe@clinicdoc.com", "alicebrown@gmail.com", LocalDate.of(2023, 3, 15), LocalTime.of(10, 0), LocalTime.of(10, 30), "Completed", "Severe Headache", "Prescribed Medication"));
        appointments.add(new Appointment("janesmith@clinicdoc.com", "bobdavis@gmail.com", LocalDate.of(2023, 3, 16), LocalTime.of(14, 0), LocalTime.of(14, 30), "Completed", "Back Pain", "Prescribed Medication"));
        appointments.add(new Appointment("emilyjohnson@clinicdoc.com", "charliewilson@gmail.com", LocalDate.of(2023, 3, 17), LocalTime.of(9, 0), LocalTime.of(9, 30), "Completed", "Chest Pain", "Prescribed Medication"));
        return appointments;
    }
}
