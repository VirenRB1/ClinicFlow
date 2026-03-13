package com.example.clinicflow.persistence.fake;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.persistence.UserFactory;
import com.example.clinicflow.persistence.UserRepository;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;

// Fake temporary database
public class FakeUserRepository implements UserRepository, Serializable {
    // Lists of doctors, patients and staffs
    List<Doctor> doctors;
    List<Patient> patients;
    List<Staff> staffs;
    List<Admin> admins;
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<DoctorAvailability> doctorAvailabilities = new ArrayList<>();

    public FakeUserRepository() {
        createFakeData();
    }

    // Create database
    private void createFakeData() {
        admins = UserFactory.getDefaultAdmins();
        doctors = UserFactory.getDefaultDoctors();
        patients = UserFactory.getDefaultPatients();
        staffs = UserFactory.getDefaultStaffs();
        appointments.addAll(UserFactory.getDefaultPastAppointments());
    }

    // Get methods
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

    @Override
    public List<Admin> getAllAdmins() {
        return Collections.unmodifiableList(admins);
    }

    // Add a patient to database
    @Override
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    @Override
    public void addStaff(Staff staff) {
        staffs.add(staff);
    }

    @Override
    public void deleteUser(Users user) {
        if (user instanceof Patient) {
            patients.remove(user);
        } else if (user instanceof Doctor) {
            doctors.remove(user);
        } else if (user instanceof Staff) {
            staffs.remove(user);
        }
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

    // Get user by email
    @Override
    public Users getUserByEmail(String email) {
        for (Doctor doctor : doctors) {
            if (doctor.getEmail().equalsIgnoreCase(email)) {
                return doctor;
            }
        }

        for (Staff staff : staffs) {
            if (staff.getEmail().equalsIgnoreCase(email)) {
                return staff;
            }
        }

        for (Patient patient : patients) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return patient;
            }
        }

        for (Admin admin : admins) {
            if (admin.getEmail().equalsIgnoreCase(email)) {
                return admin;
            }
        }

        return null;
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == appointment.getId()) {
                appointments.set(i, appointment);
                return;
            }
        }
    }

    @Override
    public void addDoctorAvailability(DoctorAvailability availability) {
        doctorAvailabilities.add(availability);
    }

    @Override
    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        List<DoctorAvailability> result = new ArrayList<>();
        for (DoctorAvailability availability : doctorAvailabilities) {
            if (availability.getDoctorEmail().equals(doctorEmail) && availability.getDayOfWeek() == dayOfWeek) {
                result.add(availability);
            }
        }
        return result;
    }

    @Override
    public List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorEmail().equals(doctorEmail)
                    && appointment.getAppointmentDate().equals(date)) {
                result.add(appointment);
            }
        }

        return result;
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(String patientEmail) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientEmail().equalsIgnoreCase(patientEmail)) {
                result.add(appointment);
            }
        }
        return result;
    }
}
