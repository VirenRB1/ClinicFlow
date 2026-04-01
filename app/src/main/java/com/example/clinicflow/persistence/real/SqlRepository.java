package com.example.clinicflow.persistence.real;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of UserRepository using a SQLite database.
 * Delegates to focused helpers for each domain.
 */
public class SqlRepository implements UserRepository {
    private final UserSqlHelper userHelper;
    private final AppointmentSqlHelper appointmentHelper;
    private final AvailabilitySqlHelper availabilityHelper;

    public SqlRepository(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.userHelper = new UserSqlHelper(dbHelper);
        this.appointmentHelper = new AppointmentSqlHelper(dbHelper);
        this.availabilityHelper = new AvailabilitySqlHelper(dbHelper);
    }

    // User related methods are delegated to UserSqlHelper

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Patient> getAllPatients() { return userHelper.getAllPatients(); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Doctor> getAllDoctors() { return userHelper.getAllDoctors(); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Staff> getAllStaffs() { return userHelper.getAllStaffs(); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Admin> getAllAdmins() { return userHelper.getAllAdmins(); }

    @Override
    public void addPatient(Patient patient) { userHelper.addPatient(patient); }

    @Override
    public void addDoctor(Doctor doctor) { userHelper.addDoctor(doctor); }

    @Override
    public void addStaff(Staff staff) { userHelper.addStaff(staff); }

    @Override
    public void deleteUser(Users user) { userHelper.deleteUser(user); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Patient getPatientByEmail(String email) { return userHelper.getPatientByEmail(email); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Doctor getDoctorByEmail(String email) { return userHelper.getDoctorByEmail(email); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Staff getStaffByEmail(String email) { return userHelper.getStaffByEmail(email); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Admin getAdminByEmail(String email) { return userHelper.getAdminByEmail(email); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Users getUserByEmail(String email) { return userHelper.getUserByEmail(email); }

    // Appointment related methods are delegated to AppointmentSqlHelper

    @Override
    public void addAppointment(Appointment appointment) { appointmentHelper.addAppointment(appointment); }

    @Override
    public void updateAppointment(Appointment appointment) { appointmentHelper.updateAppointment(appointment); }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date) {
        return appointmentHelper.getAppointmentsForDoctorOnDate(doctorEmail, date);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getUpcomingAppointmentsForDoctorOnDay(String doctorEmail, int dayOfWeek) {
        return appointmentHelper.getUpcomingAppointmentsForDoctorOnDay(doctorEmail, dayOfWeek);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail) {
        return appointmentHelper.getUpcomingAppointmentsForPatient(patientEmail);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getCompletedAppointmentsForPatient(String patientEmail) {
        return appointmentHelper.getCompletedAppointmentsForPatient(patientEmail);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getUpcomingAppointmentsForDoctor(String doctorEmail) {
        return appointmentHelper.getUpcomingAppointmentsForDoctor(doctorEmail);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getCompletedAppointmentsForDoctor(String doctorEmail) {
        return appointmentHelper.getCompletedAppointmentsForDoctor(doctorEmail);
    }

    // Availability related methods are delegated to AvailabilitySqlHelper

    @Override
    public void addDoctorAvailability(DoctorAvailability availability) {
        availabilityHelper.addDoctorAvailability(availability);
    }

    @Override
    public void deleteDoctorAvailability(int id) {
        availabilityHelper.deleteDoctorAvailability(id);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        return availabilityHelper.getDoctorAvailability(doctorEmail, dayOfWeek);
    }
}
