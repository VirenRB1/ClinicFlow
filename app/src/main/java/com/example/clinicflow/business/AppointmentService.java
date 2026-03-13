package com.example.clinicflow.business;

import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentService {
    private final UserRepository userRepository;
    private final int SLOT_DURATION_MINUTES = 30;

    public AppointmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Get all upcoming appointments for a patient
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

   //Retrieve all the past appointments for a patient
    public List<Appointment> getPastAppointmentsForPatient(String patientEmail) {
        List<Appointment> allAppointments = userRepository.getAppointmentsForPatient(patientEmail);
        List<Appointment> past = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appt = allAppointments.get(i);
            boolean isPastTime = !isAfterNow(appt.getAppointmentDate(), appt.getStartTime(), today, now);
            if ("Completed".equalsIgnoreCase(appt.getStatus()) || (isPastTime && ! "Cancelled".equalsIgnoreCase(appt.getStatus()))) {
                past.add(appt);
            }
        }

        sortAppointments(past);
        return past;
    }

 //Complete and appointment and add a doctor note
    public void completeAppointment(Appointment appointment, String doctorNote) {
        appointment.setStatus("Completed");
        appointment.setDoctorNotes(doctorNote);
        userRepository.updateAppointment(appointment);
    }

    /**
     * Updates an appointment status to completed or cancelled. Cancell will be implemented in IT3
     */
    public void updateAppointmentStatus(Appointment appointment, String newStatus) {
        appointment.setStatus(newStatus);
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

        appointment.setStatus("Confirmed");
        userRepository.addAppointment(appointment);
    }

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
            if (!"Cancelled".equalsIgnoreCase(appt.getStatus())) {
                if (start.isBefore(appt.getEndTime()) && appt.getStartTime().isBefore(end)) {
                    return false;
                }
            }
        }
        return true;
    }
}
