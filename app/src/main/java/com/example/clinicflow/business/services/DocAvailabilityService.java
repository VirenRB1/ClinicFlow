package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;
import com.example.clinicflow.business.validators.AvailabilityValidator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing doctor work shifts and availability windows.
 */
public class DocAvailabilityService {
    private final DoctorAvailabilityPersistence availabilityPersistence;
    private final AppointmentPersistence appointmentPersistence;
    private final AvailabilityValidator validator;
    private static final String CANCELLATION_MESSAGE = "Appointment cancelled due to schedule change. Please rebook with updated availability.";

    public DocAvailabilityService(DoctorAvailabilityPersistence availabilityPersistence,
                                  AppointmentPersistence appointmentPersistence,
                                  AvailabilityValidator validator) {
        this.availabilityPersistence = availabilityPersistence;
        this.appointmentPersistence = appointmentPersistence;
        this.validator = validator;
    }

    /**
     * Adds a new availability shift for a doctor after validation and overlap
     * checks. If the day has already passed this week, old availability for
     * that day is cleared before adding.
     */
    public void addDoctorAvailability(DoctorAvailability availability) throws ValidationExceptions.ValidationException {
        validator.validateAvailability(availability);

        List<DoctorAvailability> existingAvailabilities = availabilityPersistence.getDoctorAvailability(
                availability.getDoctorEmail(),
                availability.getDayOfWeek());

        for (DoctorAvailability existing : existingAvailabilities) {
            if (isOverlapping(availability, existing)) {
                throw new ValidationExceptions.AvailabilityOverlapException();
            }
        }

        availabilityPersistence.addDoctorAvailability(availability);
    }

    /**
     * Returns the list of upcoming appointments that would be cancelled
     * if the doctor replaces their availability with the new one.
     * Only returns appointments that fall outside the new availability window.
     */
    public List<Appointment> getAffectedAppointments(DoctorAvailability newAvailability) {
        List<Appointment> allAppointments = appointmentPersistence.getUpcomingAppointmentsForDoctorOnDay(
                newAvailability.getDoctorEmail(), newAvailability.getDayOfWeek());

        List<Appointment> affected = new ArrayList<>();
        for (Appointment appt : allAppointments) {
            if (!isWithinWindow(appt, newAvailability)) {
                affected.add(appt);
            }
        }
        return affected;
    }

    /**
     * Replaces all availability for a doctor on a given day-of-week.
     * Only cancels appointments that fall outside the new availability window.
     * Then deletes old availability and adds the new one.
     */
    public void replaceAvailability(DoctorAvailability newAvailability) throws ValidationExceptions.ValidationException {
        validator.validateAvailability(newAvailability);

        String doctorEmail = newAvailability.getDoctorEmail();
        int dayOfWeek = newAvailability.getDayOfWeek();

        // Only cancel appointments outside the new availability window
        List<Appointment> affected = getAffectedAppointments(newAvailability);
        for (Appointment appt : affected) {
            appt.setStatus(AppointmentStatus.CANCELLED);
            appt.setDoctorNotes(CANCELLATION_MESSAGE);
            appointmentPersistence.updateAppointment(appt);
        }

        // Delete all existing availability for this day
        List<DoctorAvailability> existing = availabilityPersistence.getDoctorAvailability(doctorEmail, dayOfWeek);
        for (DoctorAvailability avail : existing) {
            availabilityPersistence.deleteDoctorAvailability(avail.getId());
        }

        // Add the new availability
        availabilityPersistence.addDoctorAvailability(newAvailability);
    }

    /**
     * Checks if an appointment falls within a given availability window.
     */
    private boolean isWithinWindow(Appointment appt, DoctorAvailability availability) {
        LocalTime apptStart = appt.getStartTime();
        LocalTime apptEnd = appt.getEndTime();
        LocalTime availStart = availability.getStartTime();
        LocalTime availEnd = availability.getEndTime();

        return !apptStart.isBefore(availStart) && !apptEnd.isAfter(availEnd);
    }

    /**
     * Checks if two availability slots for the same day overlap.
     */
    private boolean isOverlapping(DoctorAvailability newAvail, DoctorAvailability existingAvail) {
        LocalTime newStart = newAvail.getStartTime();
        LocalTime newEnd = newAvail.getEndTime();
        LocalTime existingStart = existingAvail.getStartTime();
        LocalTime existingEnd = existingAvail.getEndTime();

        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
    }

    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        return availabilityPersistence.getDoctorAvailability(doctorEmail, dayOfWeek);
    }
}
