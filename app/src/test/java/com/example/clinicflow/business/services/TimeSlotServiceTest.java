package com.example.clinicflow.business.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class TimeSlotServiceTest {

    private TimeSlotService timeSlotService;
    private AppointmentPersistence mockPersistence;
    private DoctorAvailabilityPersistence mockAvailabilityPersistence;

    @Before
    public void setUp() {
        mockPersistence = mock(AppointmentPersistence.class);
        mockAvailabilityPersistence = mock(DoctorAvailabilityPersistence.class);
        timeSlotService = new TimeSlotService(mockPersistence, mockAvailabilityPersistence);
    }

    @Test
    public void testGetAvailableTimeSlots_TodayPastAndFuture() {
        String docEmail = "doc@test.com";
        LocalDate today = LocalDate.now();
        DoctorAvailability avail = new DoctorAvailability(docEmail, today.getDayOfWeek().getValue(), LocalTime.MIN, LocalTime.MAX.minusMinutes(31));
        when(mockAvailabilityPersistence.getDoctorAvailability(docEmail, today.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockPersistence.getAppointmentsForDoctorOnDate(docEmail, today))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = timeSlotService.getAvailableTimeSlots(docEmail, today);

        LocalTime now = LocalTime.now();
        for (TimeSlot slot : slots) {
            assertTrue(slot.getStartTime().isAfter(now));
        }
    }
}
