package com.example.clinicflow.business.services;

import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeSlotService {
    private final AppointmentPersistence appointmentPersistence;
    private final DoctorAvailabilityPersistence availabilityPersistence;
    private final Clock clock;
    private final int SLOT_DURATION_MINUTES = 30;

    public TimeSlotService(AppointmentPersistence appointmentPersistence, DoctorAvailabilityPersistence availabilityPersistence, Clock clock) {
        this.appointmentPersistence = appointmentPersistence;
        this.availabilityPersistence = availabilityPersistence;
        this.clock = clock;
    }

    //Get all available time slots for a doctor on a specific date
    //Only returns slots if the date is within the current or next week
    public List<TimeSlot> getAvailableTimeSlots(String doctorEmail, LocalDate date) {
        if (!isWithinBookingWindow(date)) {
            return Collections.emptyList();
        }

        List<Appointment> appointments = appointmentPersistence.getAppointmentsForDoctorOnDate(doctorEmail, date);
        List<DoctorAvailability> doctorAvailabilities = availabilityPersistence.getDoctorAvailability(
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

    // Checks if a date falls within the booking window (current week + next week)
    public boolean isWithinBookingWindow(LocalDate date) {
        LocalDate today = LocalDate.now(clock);
        LocalDate endOfNextWeek = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).plusWeeks(1);
        return !date.isBefore(today) && !date.isAfter(endOfNextWeek);
    }

    // Check if a time slot is within the doctor's availability
    public boolean isWithinAvailability(List<DoctorAvailability> availabilities, LocalTime start, LocalTime end) {
        for (DoctorAvailability avail : availabilities) {
            if ((start.isAfter(avail.getStartTime()) || start.equals(avail.getStartTime())) &&
                    (end.isBefore(avail.getEndTime()) || end.equals(avail.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    // Check if a time slot is free
    public boolean isSlotFree(List<Appointment> appointments, LocalTime start, LocalTime end) {
        for (Appointment appt : appointments) {
            if (appt.getStatus() != AppointmentStatus.CANCELLED) {
                if (start.isBefore(appt.getEndTime()) && appt.getStartTime().isBefore(end)) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<TimeSlot> generateTimeSlots(List<DoctorAvailability> availabilities, List<Appointment> appointments, LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(clock);

        for (DoctorAvailability availability : availabilities) {
            LocalTime currentStart = availability.getStartTime();
            LocalTime dayEnd = availability.getEndTime();

            if (currentStart == null || dayEnd == null) continue;

            while (true) {
                LocalTime currentEnd = currentStart.plusMinutes(SLOT_DURATION_MINUTES);

                if (currentEnd.isBefore(currentStart) || currentEnd.isAfter(dayEnd)) {
                    break;
                }

                LocalDateTime slotStartTime = LocalDateTime.of(date, currentStart);

                if (slotStartTime.isAfter(now)) {
                    boolean available = isSlotFree(appointments, currentStart, currentEnd);
                    if (available) {
                        timeSlots.add(new TimeSlot(currentStart, currentEnd, true));
                    }
                }
                currentStart = currentEnd;
            }
        }
        return timeSlots;
    }
}
