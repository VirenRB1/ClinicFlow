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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeSlotServiceTest {

    private TimeSlotService timeSlotService;
    private AppointmentPersistence mockPersistence;
    private DoctorAvailabilityPersistence mockAvailabilityPersistence;

    private final String docEmail = "doctor@test.com";

    @Before
    public void setUp() {
        mockPersistence = mock(AppointmentPersistence.class);
        mockAvailabilityPersistence = mock(DoctorAvailabilityPersistence.class);
        timeSlotService = new TimeSlotService(mockPersistence, mockAvailabilityPersistence);
    }

    @Test
    public void testGetAvailableTimeSlots_FutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(5);
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
        LocalDate today = LocalDate.now();
        // Shift covers whole day to ensure we hit both past and future slots relative to 'now'
        DoctorAvailability avail = new DoctorAvailability(docEmail, today.getDayOfWeek().getValue(), LocalTime.MIN, LocalTime.MAX.minusMinutes(31));
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, today.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, today))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, today);

        LocalTime now = LocalTime.now();
        for (TimeSlot slot : slots) {
            assertTrue("Slot start time " + slot.getStartTime() + " should be after now " + now, 
                    slot.getStartTime().isAfter(now));
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
        
        Appointment activeAppt = new Appointment(docEmail, "p@t.com", LocalDate.now(), 
                LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "Checkup", "");
        
        Appointment cancelledAppt = new Appointment(docEmail, "p2@t.com", LocalDate.now(), 
                LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CANCELLED, "Checkup", "");

        // Case 1: No appointments
        assertTrue(timeSlotService.isSlotFree(Collections.emptyList(), slotStart, slotEnd));

        // Case 2: Overlapping confirmed appointment
        assertFalse(timeSlotService.isSlotFree(Collections.singletonList(activeAppt), slotStart, slotEnd));

        // Case 3: Overlapping cancelled appointment (should be free)
        assertTrue(timeSlotService.isSlotFree(Collections.singletonList(cancelledAppt), slotStart, slotEnd));

        // Case 4: Partial overlap (ends after start)
        Appointment partial = new Appointment(docEmail, "p@t.com", LocalDate.now(), 
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
        
        List<TimeSlot> result = timeSlotService.getAvailableTimeSlots(docEmail, LocalDate.now().plusDays(1));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAvailableTimeSlots_Sorting() {
        LocalDate future = LocalDate.now().plusDays(1);
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
