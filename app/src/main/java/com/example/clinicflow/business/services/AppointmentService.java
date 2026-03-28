package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentService {
    private final UserRepository userRepository;
    private final int SLOT_DURATION_MINUTES = 30;
    private final String DEFAULT_NOTES = "";

    public AppointmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Get all upcoming appointments for a patient
    public List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail) {
        List<Appointment> upcoming = userRepository.getUpcomingAppointmentsForPatient(patientEmail);
        sortAppointments(upcoming);
        return upcoming;
    }

    //Retrieve all the past appointments for a patient
    public List<Appointment> getPastAppointmentsForPatient(String patientEmail) {
        List<Appointment> past = userRepository.getCompletedAppointmentsForPatient(patientEmail);
        sortAppointments(past);
        return past;
    }

    public List<Appointment> getUpcomingAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> upcoming = userRepository.getUpcomingAppointmentsForDoctor(doctorEmail);
        sortAppointments(upcoming);
        return upcoming;
    }

    //Complete and appointment and add a doctor note
    public void completeAppointment(Appointment appointment, String doctorNote) {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setDoctorNotes(doctorNote);
        userRepository.updateAppointment(appointment);
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

    //Get all available time slots for a doctor on a specific date
    public List<TimeSlot> getAvailableTimeSlots(String doctorEmail, LocalDate date) {
        List<Appointment> appointments = userRepository.getAppointmentsForDoctorOnDate(doctorEmail, date);
        List<DoctorAvailability> doctorAvailabilities = userRepository.getDoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue()
        );

        List<TimeSlot> availableSlots = generateTimeSlots(doctorAvailabilities, appointments, date);

        availableSlots.sort(new Comparator<TimeSlot>() {
            @Override
            public int compare(TimeSlot t1, TimeSlot t2) {
                return t1.getStartTime().compareTo(t2.getStartTime());
            }
        });
        return availableSlots;
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

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        userRepository.addAppointment(appointment);
    }

    public void cancelAppointment(Appointment appointment)  throws ValidationExceptions.ValidationException {

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
        userRepository.updateAppointment(appointment);
    }

    private List<TimeSlot> generateTimeSlots(List<DoctorAvailability> availabilities, List<Appointment> appointments, LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (DoctorAvailability availability : availabilities) {
            LocalTime currentStart = availability.getStartTime();
            LocalTime dayEnd = availability.getEndTime();

            if (currentStart == null || dayEnd == null) continue;

            while (true) {
                LocalTime currentEnd = currentStart.plusMinutes(SLOT_DURATION_MINUTES);
                
                // Stop if we wrap around midnight or exceed availability
                if (currentEnd.isBefore(currentStart) || currentEnd.isAfter(dayEnd)) {
                    break;
                }
                
                LocalDateTime slotStartTime = LocalDateTime.of(date, currentStart);

                // Ensure we only add slots that are in the future
                if (slotStartTime.isAfter(now)) {
                    boolean available = isSlotFree(appointments, currentStart, currentEnd);
                    // Only add the slot to the list if it is actually available
                    if (available) {
                        timeSlots.add(new TimeSlot(currentStart, currentEnd, true));
                    }
                }
                currentStart = currentEnd;
            }
        }
        return timeSlots;
    }

    // Helper method to check if a time slot is within the doctor's availability
    private boolean isWithinAvailability(List<DoctorAvailability> availabilities, LocalTime start, LocalTime end) {
        for (DoctorAvailability avail : availabilities) {
            if ((start.isAfter(avail.getStartTime()) || start.equals(avail.getStartTime())) &&
                    (end.isBefore(avail.getEndTime()) || end.equals(avail.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    // Helper method to check if a time slot is free
    private boolean isSlotFree(List<Appointment> appointments, LocalTime start, LocalTime end) {
        for (Appointment appt : appointments) {
            if (appt.getStatus() != AppointmentStatus.CANCELLED) {
                if (start.isBefore(appt.getEndTime()) && appt.getStartTime().isBefore(end)) {
                    return false;
                }
            }
        }
        return true;
    }
}
