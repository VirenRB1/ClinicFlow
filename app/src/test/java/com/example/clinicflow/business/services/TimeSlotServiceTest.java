package com.example.clinicflow.business.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeSlotServiceTest {

    private TimeSlotService timeSlotService;
    private AppointmentPersistence mockPersistence;
    private DoctorAvailabilityPersistence mockAvailabilityPersistence;
    private Clock fixedClock;

    private final String docEmail = "doctor@test.com";

    // Fixed to Wednesday March 25, 2026 at 8:00 AM
    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 3, 25);

    @Before
    public void setUp() {
        mockPersistence = mock(AppointmentPersistence.class);
        mockAvailabilityPersistence = mock(DoctorAvailabilityPersistence.class);
        fixedClock = Clock.fixed(
                ZonedDateTime.of(FIXED_TODAY.atTime(8, 0), ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        timeSlotService = new TimeSlotService(mockPersistence, mockAvailabilityPersistence, fixedClock);
    }

    @Test
    public void testGetAvailableTimeSlots_FutureDate() {
        LocalDate futureDate = FIXED_TODAY.plusDays(5);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0); // 2 slots of 30 mins

        DoctorAvailability avail = new DoctorAvailability(docEmail, futureDate.getDayOfWeek().getValue(), start, end);
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, futureDate.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, futureDate))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, futureDate);

        assertEquals(2, slots.size());
        assertEquals(LocalTime.of(9, 0), slots.get(0).getStartTime());
        assertEquals(LocalTime.of(9, 30), slots.get(0).getEndTime());
        assertEquals(LocalTime.of(9, 30), slots.get(1).getStartTime());
        assertEquals(LocalTime.of(10, 0), slots.get(1).getEndTime());
    }

    @Test
    public void testGetAvailableTimeSlots_TodayPastAndFuture() {
        // Clock is fixed at 8:00 AM, so slots before 8:00 should be excluded
        DoctorAvailability avail = new DoctorAvailability(docEmail, FIXED_TODAY.getDayOfWeek().getValue(), LocalTime.MIN, LocalTime.MAX.minusMinutes(31));
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, FIXED_TODAY.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, FIXED_TODAY))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, FIXED_TODAY);

        LocalTime fixedNow = LocalTime.of(8, 0);
        for (TimeSlot slot : slots) {
            assertTrue("Slot start time " + slot.getStartTime() + " should be after 8:00 AM",
                    slot.getStartTime().isAfter(fixedNow));
        }
    }

    @Test
    public void testIsWithinAvailability() {
        LocalTime availStart = LocalTime.of(9, 0);
        LocalTime availEnd = LocalTime.of(17, 0);
        DoctorAvailability avail = new DoctorAvailability(docEmail, 1, availStart, availEnd);
        List<DoctorAvailability> avails = Collections.singletonList(avail);

        // Case 1: Exactly matches
        assertTrue(timeSlotService.isWithinAvailability(avails, availStart, availEnd));

        // Case 2: Within boundaries
        assertTrue(timeSlotService.isWithinAvailability(avails, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        // Case 3: Starts before
        assertFalse(timeSlotService.isWithinAvailability(avails, LocalTime.of(8, 30), LocalTime.of(9, 30)));

        // Case 4: Ends after
        assertFalse(timeSlotService.isWithinAvailability(avails, LocalTime.of(16, 30), LocalTime.of(17, 30)));

        // Case 5: Empty list
        assertFalse(timeSlotService.isWithinAvailability(Collections.emptyList(), availStart, availEnd));
    }

    @Test
    public void testIsSlotFree() {
        LocalTime slotStart = LocalTime.of(10, 0);
        LocalTime slotEnd = LocalTime.of(10, 30);
        
        Appointment activeAppt = new Appointment(docEmail, "p@t.com", FIXED_TODAY,
                LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "Checkup", "");

        Appointment cancelledAppt = new Appointment(docEmail, "p2@t.com", FIXED_TODAY,
                LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CANCELLED, "Checkup", "");

        // Case 1: No appointments
        assertTrue(timeSlotService.isSlotFree(Collections.emptyList(), slotStart, slotEnd));

        // Case 2: Overlapping confirmed appointment
        assertFalse(timeSlotService.isSlotFree(Collections.singletonList(activeAppt), slotStart, slotEnd));

        // Case 3: Overlapping cancelled appointment (should be free)
        assertTrue(timeSlotService.isSlotFree(Collections.singletonList(cancelledAppt), slotStart, slotEnd));

        // Case 4: Partial overlap (ends after start)
        Appointment partial = new Appointment(docEmail, "p@t.com", FIXED_TODAY,
                LocalTime.of(9, 45), LocalTime.of(10, 15), AppointmentStatus.CONFIRMED, "Checkup", "");
        assertFalse(timeSlotService.isSlotFree(Collections.singletonList(partial), slotStart, slotEnd));
    }

    @Test
    public void testGenerateTimeSlots_NullTimes() {
        // This is to hit the 'if (currentStart == null || dayEnd == null) continue;' branch
        // Although the constructor might not allow nulls normally, we can try to force it or mock it if it were a POJO.
        // Since DoctorAvailability is a POJO, we can create one with nulls.
        DoctorAvailability nullAvail = new DoctorAvailability(docEmail, 1, null, null);
        
        // We need to access the private generateTimeSlots or use getAvailableTimeSlots to trigger it.
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, 1))
                .thenReturn(Collections.singletonList(nullAvail));
        
        List<TimeSlot> result = timeSlotService.getAvailableTimeSlots(docEmail, FIXED_TODAY.plusDays(1));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsWithinBookingWindow_Today() {
        assertTrue(timeSlotService.isWithinBookingWindow(FIXED_TODAY));
    }

    @Test
    public void testIsWithinBookingWindow_Tomorrow() {
        assertTrue(timeSlotService.isWithinBookingWindow(FIXED_TODAY.plusDays(1)));
    }

    @Test
    public void testIsWithinBookingWindow_PastDate() {
        assertFalse(timeSlotService.isWithinBookingWindow(FIXED_TODAY.minusDays(1)));
    }

    @Test
    public void testIsWithinBookingWindow_BeyondNextWeek() {
        assertFalse(timeSlotService.isWithinBookingWindow(FIXED_TODAY.plusWeeks(3)));
    }

    @Test
    public void testGetAvailableTimeSlots_OutsideBookingWindow_ReturnsEmpty() {
        LocalDate farFuture = FIXED_TODAY.plusWeeks(5);
        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, farFuture);
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetAvailableTimeSlots_NoAvailability_ReturnsEmpty() {
        LocalDate future = FIXED_TODAY.plusDays(1);
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, future.getDayOfWeek().getValue()))
                .thenReturn(Collections.emptyList());
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, future))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, future);
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetAvailableTimeSlots_OccupiedSlotSkipped() {
        LocalDate future = FIXED_TODAY.plusDays(1);
        DoctorAvailability avail = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Appointment booked = new Appointment(docEmail, "p@t.com", future,
                LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "Checkup", "");

        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, future))
                .thenReturn(Collections.singletonList(booked));

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, future);

        // Only the 9:30-10:00 slot should be available
        assertEquals(1, slots.size());
        assertEquals(LocalTime.of(9, 30), slots.get(0).getStartTime());
    }

    @Test
    public void testIsSlotFree_NonOverlappingConfirmedAppointment() {
        Appointment appt = new Appointment(docEmail, "p@t.com", FIXED_TODAY,
                LocalTime.of(11, 0), LocalTime.of(11, 30), AppointmentStatus.CONFIRMED, "Checkup", "");

        // Slot is 9:00-9:30, appointment is 11:00-11:30 — no overlap
        assertTrue(timeSlotService.isSlotFree(Collections.singletonList(appt),
                LocalTime.of(9, 0), LocalTime.of(9, 30)));
    }

    @Test
    public void testIsWithinAvailability_MultipleWindows_SecondMatches() {
        DoctorAvailability morning = new DoctorAvailability(docEmail, 1, LocalTime.of(8, 0), LocalTime.of(12, 0));
        DoctorAvailability afternoon = new DoctorAvailability(docEmail, 1, LocalTime.of(14, 0), LocalTime.of(18, 0));
        List<DoctorAvailability> avails = Arrays.asList(morning, afternoon);

        // 15:00-16:00 fits in afternoon window, not morning
        assertTrue(timeSlotService.isWithinAvailability(avails, LocalTime.of(15, 0), LocalTime.of(16, 0)));
    }

    @Test
    public void testIsWithinAvailability_SpansBothWindows_ReturnsFalse() {
        DoctorAvailability morning = new DoctorAvailability(docEmail, 1, LocalTime.of(8, 0), LocalTime.of(12, 0));
        DoctorAvailability afternoon = new DoctorAvailability(docEmail, 1, LocalTime.of(14, 0), LocalTime.of(18, 0));
        List<DoctorAvailability> avails = Arrays.asList(morning, afternoon);

        // 11:00-15:00 spans the gap — doesn't fit in either window
        assertFalse(timeSlotService.isWithinAvailability(avails, LocalTime.of(11, 0), LocalTime.of(15, 0)));
    }

    @Test
    public void testGetAvailableTimeSlots_AvailabilityTooShortForSlot() {
        LocalDate future = FIXED_TODAY.plusDays(1);
        // Only 15 minutes — not enough for a 30-min slot
        DoctorAvailability avail = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(),
                LocalTime.of(9, 0), LocalTime.of(9, 15));

        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, future))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, future);
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetAvailableTimeSlots_Sorting() {
        LocalDate future = FIXED_TODAY.plusDays(1);
        // Add two separate availability windows in reverse order of time
        DoctorAvailability morning = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(), LocalTime.of(8, 0), LocalTime.of(8, 30));
        DoctorAvailability afternoon = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(), LocalTime.of(14, 0), LocalTime.of(14, 30));
        
        // Return them in an order that requires sorting
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, future.getDayOfWeek().getValue()))
                .thenReturn(Arrays.asList(afternoon, morning));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, future))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, future);

        assertEquals(2, slots.size());
        assertEquals(LocalTime.of(8, 0), slots.get(0).getStartTime());
        assertEquals(LocalTime.of(14, 0), slots.get(1).getStartTime());
    }
}
