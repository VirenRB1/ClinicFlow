package com.example.clinicflow.persistence;

import com.example.clinicflow.models.Appointment;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentPersistence {
    void addAppointment(Appointment appointment);
    void updateAppointment(Appointment appointment);

    List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date);
    List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail);
    List<Appointment> getCompletedAppointmentsForPatient(String patientEmail);
    List<Appointment> getUpcomingAppointmentsForDoctor(String doctorEmail);
    List<Appointment> getCompletedAppointmentsForDoctor(String doctorEmail);
}
