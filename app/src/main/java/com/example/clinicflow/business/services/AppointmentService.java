package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class AppointmentService {
    private final AppointmentPersistence appointmentPersistence;
    private final DoctorAvailabilityPersistence availabilityPersistence;
    private final TimeSlotService timeSlotService;
    private final String DEFAULT_NOTES = "";

    public AppointmentService(AppointmentPersistence appointmentPersistence, DoctorAvailabilityPersistence availabilityPersistence) {
        this.appointmentPersistence = appointmentPersistence;
        this.availabilityPersistence = availabilityPersistence;
        this.timeSlotService = new TimeSlotService(appointmentPersistence, availabilityPersistence);
    }

    public AppointmentService(AppointmentPersistence appointmentPersistence, DoctorAvailabilityPersistence availabilityPersistence, TimeSlotService timeSlotService) {
        this.appointmentPersistence = appointmentPersistence;
        this.availabilityPersistence = availabilityPersistence;
        this.timeSlotService = timeSlotService;
    }

    //Get all upcoming appointments for a patient
    public List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail) {
        List<Appointment> upcoming = appointmentPersistence.getUpcomingAppointmentsForPatient(patientEmail);
        sortAppointments(upcoming);
        return upcoming;
    }

    //Retrieve all the past appointments for a patient
    public List<Appointment> getPastAppointmentsForPatient(String patientEmail) {
        List<Appointment> past = appointmentPersistence.getCompletedAppointmentsForPatient(patientEmail);
        sortAppointments(past);
        return past;
    }

    public List<Appointment> getUpcomingAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> upcoming = appointmentPersistence.getUpcomingAppointmentsForDoctor(doctorEmail);
        sortAppointments(upcoming);
        return upcoming;
    }

    //Complete an appointment and add a doctor note
    public void completeAppointment(Appointment appointment, String doctorNote) {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setDoctorNotes(doctorNote);
        appointmentPersistence.updateAppointment(appointment);
    }

    public void bookAppointment(String doctorEmail, String patientEmail, LocalDate date, TimeSlot slot, String purpose) throws ValidationExceptions.ValidationException {
        Appointment appointment = new Appointment(doctorEmail, patientEmail, date, slot.getStartTime(),
                slot.getEndTime(), AppointmentStatus.CONFIRMED, purpose, DEFAULT_NOTES);
        bookAppointment(appointment);
    }

    //Book an appointment for a patient
    public void bookAppointment(Appointment appointment) throws ValidationExceptions.ValidationException {
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new ValidationExceptions.InvalidAppointmentDateException();
        }

        if (!timeSlotService.isWithinBookingWindow(appointment.getAppointmentDate())) {
            throw new ValidationExceptions.InvalidAppointmentDateException();
        }

        List<DoctorAvailability> currentAvailabilities = availabilityPersistence.getDoctorAvailability(
                appointment.getDoctorEmail(),
                appointment.getAppointmentDate().getDayOfWeek().getValue()
        );

        if (!timeSlotService.isWithinAvailability(currentAvailabilities, appointment.getStartTime(), appointment.getEndTime())) {
            throw new ValidationExceptions.AppointmentConflictException();
        }

        List<Appointment> existingAppointments = appointmentPersistence.getAppointmentsForDoctorOnDate(
                appointment.getDoctorEmail(),
                appointment.getAppointmentDate()
        );

        if (!timeSlotService.isSlotFree(existingAppointments, appointment.getStartTime(), appointment.getEndTime())) {
            throw new ValidationExceptions.AppointmentConflictException();
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentPersistence.addAppointment(appointment);
    }

    public void cancelAppointment(Appointment appointment) throws ValidationExceptions.ValidationException {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if(AppointmentStatus.CANCELLED.equals(appointment.getStatus())){
            throw new ValidationExceptions.AppointmentCancellationException();
        }

        if (AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
            throw new ValidationExceptions.AppointmentCancellationException();
        }

        if(!isAfterNow(appointment.getAppointmentDate(), appointment.getStartTime(), today, now)){
            throw new ValidationExceptions.AppointmentCancellationException();
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentPersistence.updateAppointment(appointment);
    }

    private boolean isAfterNow(LocalDate apptDate, LocalTime startTime, LocalDate today, LocalTime now) {
        return apptDate.isAfter(today) || (apptDate.equals(today) && startTime.isAfter(now));
    }

    private void sortAppointments(List<Appointment> list) {
        list.sort(new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                int dateCompare = a1.getAppointmentDate().compareTo(a2.getAppointmentDate());
                if (dateCompare != 0) return dateCompare;
                return a1.getStartTime().compareTo(a2.getStartTime());
            }
        });
    }
}
