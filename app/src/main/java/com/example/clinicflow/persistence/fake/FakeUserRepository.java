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

/**
 * In-memory implementation of UserRepository for testing and development
 * purposes.
 * Implements Serializable to allow state to be passed between activities if
 * needed.
 */
public class FakeUserRepository implements UserRepository, Serializable {

    // In-memory collections of users and clinic data
    List<Doctor> doctors;
    List<Patient> patients;
    List<Staff> staffs;
    List<Admin> admins;
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<DoctorAvailability> doctorAvailabilities = new ArrayList<>();

    /**
     * Constructs the fake repository and initializes it with default test data.
     */
    public FakeUserRepository() {
        createFakeData();
    }

    /**
     * populates internal lists using the UserFactory.
     */
    private void createFakeData() {
        admins = UserFactory.getDefaultAdmins();
        doctors = UserFactory.getDefaultDoctors();
        patients = UserFactory.getDefaultPatients();
        staffs = UserFactory.getDefaultStaffs();
        appointments.addAll(UserFactory.getDefaultPastAppointments());
    }

    /**
     * @return An unmodifiable view of all patients.
     */
    @Override
    public List<Patient> getAllPatients() {
        return Collections.unmodifiableList(patients);
    }

    /**
     * @return An unmodifiable view of all doctors.
     */
    @Override
    public List<Doctor> getAllDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    /**
     * @return An unmodifiable view of all staff.
     */
    @Override
    public List<Staff> getAllStaffs() {
        return Collections.unmodifiableList(staffs);
    }

    /**
     * @return An unmodifiable view of all admins.
     */
    @Override
    public List<Admin> getAllAdmins() {
        return Collections.unmodifiableList(admins);
    }

    /**
     * Adds a patient to the in-memory list.
     * 
     * @param patient The patient to add.
     */
    @Override
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    /**
     * Adds a doctor to the in-memory list.
     * 
     * @param doctor The doctor to add.
     */
    @Override
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    /**
     * Adds a staff member to the in-memory list.
     * 
     * @param staff The staff to add.
     */
    @Override
    public void addStaff(Staff staff) {
        staffs.add(staff);
    }

    /**
     * Removes a user from the appropriate in-memory list.
     * 
     * @param user The user object to remove.
     */
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

    /**
     * Finds a patient by email.
     * 
     * @param email The email to search for.
     * @return The Patient object or null.
     */
    @Override
    public Patient getPatientByEmail(String email) {
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Finds any user type by email.
     * 
     * @param email The email to search for.
     * @return The User object or null.
     */
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

    /**
     * Adds a new appointment to the list.
     * 
     * @param appointment The appointment details.
     */
    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Adds a doctor availability slot.
     * 
     * @param availability The availability details.
     */
    @Override
    public void addDoctorAvailability(DoctorAvailability availability) {
        doctorAvailabilities.add(availability);
    }

    /**
     * Retrieves availability slots for a doctor on a specific day of the week.
     * 
     * @param doctorEmail The doctor's email.
     * @param dayOfWeek   The day of the week (1-7).
     * @return A list of matching availability slots.
     */
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

    /**
     * Retrieves appointments for a doctor on a specific date.
     * 
     * @param doctorEmail The doctor's email.
     * @param date        The date to search for.
     * @return A list of matching appointments.
     */
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

    /**
     * Retrieves all appointments for a patient.
     * 
     * @param patientEmail The patient's email.
     * @return A list of matching appointments.
     */
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

    @Override
    public List<Appointment> getAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorEmail().equalsIgnoreCase(doctorEmail)) {
                result.add(appointment);
            }
        }
        return result;
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
}
