package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class handling business logic related to appointments.
 */
public class AppointmentService {
    private final UserRepository userRepository;
    private final int SLOT_DURATION_MINUTES = 30;

    /**
     * Constructs an AppointmentService with a user repository.
     * @param userRepository The repository to use for data access.
     */
    public AppointmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all upcoming confirmed appointments for a specific patient.
     * @param patientEmail The email of the patient.
     * @return A sorted list of upcoming confirmed appointments.
     */
    public List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail) {
        List<Appointment> allAppointments = userRepository.getAppointmentsForPatient(patientEmail);
        List<Appointment> upcoming = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appt = allAppointments.get(i);
            if (isAfterNow(appt.getAppointmentDate(), appt.getStartTime(), today, now)) {
                if ("Confirmed".equalsIgnoreCase(appt.getStatus())) {
                    upcoming.add(appt);
                }
            }
        }

        sortAppointments(upcoming);
        return upcoming;
    }

    /**
     * Retrieves all past appointments for a specific patient.
     * @param patientEmail The email of the patient.
     * @return A sorted list of past appointments (completed or expired).
     */
    public List<Appointment> getPastAppointmentsForPatient(String patientEmail) {
        List<Appointment> allAppointments = userRepository.getAppointmentsForPatient(patientEmail);
        List<Appointment> past = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appt = allAppointments.get(i);
            boolean isPastTime = !isAfterNow(appt.getAppointmentDate(), appt.getStartTime(), today, now);
            if ("Completed".equalsIgnoreCase(appt.getStatus()) || (isPastTime && !"Cancelled".equalsIgnoreCase(appt.getStatus()))) {
                past.add(appt);
            }
        }

        sortAppointments(past);
        return past;
    }

    /**
     * Checks if a specific date and time are in the future relative to the current moment.
     * @param apptDate Date to check.
     * @param startTime Start time to check.
     * @param today Current date.
     * @param now Current time.
     * @return True if the date/time is after now, false otherwise.
     */
    private boolean isAfterNow(LocalDate apptDate, LocalTime startTime, LocalDate today, LocalTime now) {
        return apptDate.isAfter(today) || (apptDate.equals(today) && startTime.isAfter(now));
    }

    /**
     * Sorts a list of appointments chronologically by date and then start time.
     * @param list The list of appointments to sort.
     */
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

    /**
     * Gets all available 30-minute time slots for a doctor on a given date.
     * @param doctorEmail The doctor's email.
     * @param date The requested date.
     * @return A list of available TimeSlot objects.
     */
    public List<TimeSlot> getAvailableTimeSlots(String doctorEmail, LocalDate date) {
        List<Appointment> appointments = userRepository.getAppointmentsForDoctorOnDate(doctorEmail, date);
        List<DoctorAvailability> doctorAvailabilities = userRepository.getDoctorAvailability(doctorEmail, date.getDayOfWeek().getValue());

        List<TimeSlot> allSlots = generateTimeSlots(doctorAvailabilities, appointments);
        
        List<TimeSlot> availableSlots = new ArrayList<>();
        for (TimeSlot slot : allSlots) {
            if (slot.isAvailable()) {
                availableSlots.add(slot);
            }
        }
        
        availableSlots.sort(new Comparator<TimeSlot>() {
            @Override
            public int compare(TimeSlot t1, TimeSlot t2) {
                return t1.getStartTime().compareTo(t2.getStartTime());
            }
        });
        return availableSlots;
    }

    /**
     * Books a new appointment after validating the date and doctor availability.
     * @param appointment The appointment details to book.
     * @throws ValidationExceptions.ValidationException If the date is invalid or if there's a conflict.
     */
    public void bookAppointment(Appointment appointment) throws ValidationExceptions.ValidationException {
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new ValidationExceptions.InvalidAppointmentDateException();
        }

        List<DoctorAvailability> currentAvailabilities = userRepository.getDoctorAvailability(
                appointment.getDoctorEmail(), 
                appointment.getAppointmentDate().getDayOfWeek().getValue()
        );

        if (!isWithinAvailability(currentAvailabilities, appointment.getStartTime(), appointment.getEndTime())) {
            throw new ValidationExceptions.AppointmentConflictException();
        }

        List<Appointment> existingAppointments = userRepository.getAppointmentsForDoctorOnDate(
                appointment.getDoctorEmail(), 
                appointment.getAppointmentDate()
        );

        if (!isSlotFree(existingAppointments, appointment.getStartTime(), appointment.getEndTime())) {
            throw new ValidationExceptions.AppointmentConflictException();
        }

        appointment.setStatus("Confirmed");
        userRepository.addAppointment(appointment);
    }

    /**
     * Generates all possible time slots based on doctor availability and existing appointments.
     * @param availabilities The doctor's shifts.
     * @param appointments Already booked appointments.
     * @return A list of all potential time slots.
     */
    private List<TimeSlot> generateTimeSlots(List<DoctorAvailability> availabilities, List<Appointment> appointments) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (DoctorAvailability availability : availabilities) {
            LocalTime currentStart = availability.getStartTime();
            LocalTime dayEnd = availability.getEndTime();

            while (currentStart.plusMinutes(SLOT_DURATION_MINUTES).isBefore(dayEnd) || 
                   currentStart.plusMinutes(SLOT_DURATION_MINUTES).equals(dayEnd)) {
                
                LocalTime currentEnd = currentStart.plusMinutes(SLOT_DURATION_MINUTES);
                boolean available = isSlotFree(appointments, currentStart, currentEnd);
                
                timeSlots.add(new TimeSlot(currentStart, currentEnd, available));
                currentStart = currentEnd;
            }
        }
        return timeSlots;
    }

    /**
     * Checks if a given time range falls within any of the doctor's availability windows.
     * @param availabilities The doctor's scheduled availabilities.
     * @param start Requested start time.
     * @param end Requested end time.
     * @return True if the slot is within availability, false otherwise.
     */
    private boolean isWithinAvailability(List<DoctorAvailability> availabilities, LocalTime start, LocalTime end) {
        for (DoctorAvailability avail : availabilities) {
            if ((start.isAfter(avail.getStartTime()) || start.equals(avail.getStartTime())) &&
                (end.isBefore(avail.getEndTime()) || end.equals(avail.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a requested time slot is free of existing, non-cancelled appointments.
     * @param appointments List of existing appointments for the day.
     * @param start Requested start time.
     * @param end Requested end time.
     * @return True if the slot is free, false otherwise.
     */
    private boolean isSlotFree(List<Appointment> appointments, LocalTime start, LocalTime end) {
        for (Appointment appt : appointments) {
            if (!"Cancelled".equalsIgnoreCase(appt.getStatus())) {
                if (start.isBefore(appt.getEndTime()) && appt.getStartTime().isBefore(end)) {
                    return false;
                }
            }
        }
        return true;
    }
}
