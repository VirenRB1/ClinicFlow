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

/**
 * Interface defining the contract for user and clinic data persistence.
 */
public interface UserRepository {
    /**
     * @return A list of all patients.
     */
    List<Patient> getAllPatients();

    /**
     * @return A list of all doctors.
     */
    List<Doctor> getAllDoctors();

    /**
     * @return A list of all staff members.
     */
    List<Staff> getAllStaffs();

    /**
     * @return A list of all administrators.
     */
    List<Admin> getAllAdmins();

    /**
     * Persists a new patient record.
     * 
     * @param patient The patient to add.
     */
    void addPatient(Patient patient);

    /**
     * Persists a new doctor record.
     * 
     * @param doctor The doctor to add.
     */
    void addDoctor(Doctor doctor);

    /**
     * Persists a new staff record.
     * 
     * @param staff The staff member to add.
     */
    void addStaff(Staff staff);

    /**
     * Removes a user record from persistence.
     * 
     * @param user The user to delete.
     */
    void deleteUser(Users user);

    /**
     * Retrieves a patient by email.
     * 
     * @param email The email to search for.
     * @return The Patient object or null if not found.
     */
    Patient getPatientByEmail(String email);

    /**
     * Retrieves any user type by email.
     * 
     * @param email The email to search for.
     * @return The User object or null if not found.
     */
    Users getUserByEmail(String email);

    /**
     * Persists a new appointment.
     * 
     * @param appointment The appointment to add.
     */
    void addAppointment(Appointment appointment);

    /**
     * Adds a doctor availability shift.
     * 
     * @param availability The availability details.
     */
    void addDoctorAvailability(DoctorAvailability availability);

    /**
     * Retrieves a doctor's availability for a specific day of the week.
     * 
     * @param doctorEmail The doctor's email.
     * @param dayOfWeek   The day of the week (1-7).
     * @return A list of availability shifts.
     */
    List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek);

    /**
     * Retrieves all appointments for a doctor on a specific date.
     * 
     * @param doctorEmail The doctor's email.
     * @param date        The date to check.
     * @return A list of appointments.
     */
    List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date);

    /**
     * Retrieves all appointments for a specific patient.
     * 
     * @param patientEmail The patient's email.
     * @return A list of appointments.
     */
    List<Appointment> getAppointmentsForPatient(String patientEmail);

    void updateAppointment(Appointment appointment);
}
