package com.example.clinicflow.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.validators.AvailabilityValidator;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.AppointmentPersistence;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class DocAvailabilityServiceTest {
    private DocAvailabilityService service;
    private DoctorAvailabilityPersistence mockRepo;
    private AppointmentPersistence mockApptRepo;

    // Fixed to Wednesday March 25, 2026
    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 3, 25);
    private final int TODAY_DAY = FIXED_TODAY.getDayOfWeek().getValue();

    @Before
    public void setUp() {
        mockRepo = mock(DoctorAvailabilityPersistence.class);
        mockApptRepo = mock(AppointmentPersistence.class);
        service = new DocAvailabilityService(mockRepo, mockApptRepo, new AvailabilityValidator());
    }

    @Test
    public void testAddDoctorAvailability_Success() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Collections.emptyList());

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_BeforeExisting() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_AfterExisting() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(13, 0), LocalTime.of(14, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_TouchingExistingEnd() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(12, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_TouchingExistingStart() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_Overlap_StartsBeforeEndsDuring() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () ->
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_StartsDuringEndsAfter() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(11, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () ->
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_ContainedWithin() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () ->
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_CoversExisting() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(8, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () ->
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_MultipleExisting_OverlapWithSecond() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(13, 0), LocalTime.of(15, 0));
        DoctorAvailability first = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        DoctorAvailability second = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(14, 0), LocalTime.of(16, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(first, second));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () ->
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_InvalidTime_ThrowsValidationException() {
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(9, 30), LocalTime.of(10, 0));

        assertThrows(ValidationExceptions.ValidationException.class, () ->
            service.addDoctorAvailability(invalidAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_EndTimeBeforeStartTime_ThrowsException() {
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(11, 0), LocalTime.of(9, 0));

        assertThrows(ValidationExceptions.InvalidStartAndEndTimeException.class, () ->
            service.addDoctorAvailability(invalidAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_NullTimes_ThrowsException() {
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, null, null);

        assertThrows(ValidationExceptions.EmptyFieldException.class, () ->
            service.addDoctorAvailability(invalidAvail)
        );
    }

    @Test
    public void testReplaceAvailability_OnlyCancelsOutsideAppointments() throws ValidationExceptions.ValidationException {
        // New window: 8am-2pm
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(8, 0), LocalTime.of(14, 0));
        DoctorAvailability oldAvail = new DoctorAvailability(1, "doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(17, 0));

        // 9:00-9:30 is within new window, 15:00-15:30 is outside
        Appointment insideAppt = new Appointment(1, "doc@test.com", "p1@test.com",
                FIXED_TODAY, LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentStatus.CONFIRMED, "Checkup", "");
        Appointment outsideAppt = new Appointment(2, "doc@test.com", "p2@test.com",
                FIXED_TODAY, LocalTime.of(15, 0), LocalTime.of(15, 30),
                AppointmentStatus.CONFIRMED, "Follow-up", "");

        when(mockApptRepo.getUpcomingAppointmentsForDoctorOnDay("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(insideAppt, outsideAppt));
        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(oldAvail));

        service.replaceAvailability(newAvail);

        // Only the outside appointment should be cancelled
        assertEquals(AppointmentStatus.CONFIRMED, insideAppt.getStatus());
        assertEquals(AppointmentStatus.CANCELLED, outsideAppt.getStatus());
        verify(mockApptRepo).updateAppointment(outsideAppt);

        // Old availability deleted, new one added
        verify(mockRepo).deleteDoctorAvailability(1);
        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testReplaceAvailability_NoAppointments_JustReplacesAvailability() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(8, 0), LocalTime.of(14, 0));
        DoctorAvailability oldAvail = new DoctorAvailability(1, "doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(mockApptRepo.getUpcomingAppointmentsForDoctorOnDay("doc@test.com", TODAY_DAY)).thenReturn(Collections.emptyList());
        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(oldAvail));

        service.replaceAvailability(newAvail);

        verify(mockRepo).deleteDoctorAvailability(1);
        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testGetAffectedAppointments_OnlyReturnsOutsideAppointments() {
        // New window: 10am-3pm
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(10, 0), LocalTime.of(15, 0));

        // 11:00-11:30 is inside, 9:00-9:30 is outside
        Appointment insideAppt = new Appointment(1, "doc@test.com", "p1@test.com",
                FIXED_TODAY, LocalTime.of(11, 0), LocalTime.of(11, 30),
                AppointmentStatus.CONFIRMED, "Checkup", "");
        Appointment outsideAppt = new Appointment(2, "doc@test.com", "p2@test.com",
                FIXED_TODAY.plusDays(7), LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentStatus.CONFIRMED, "Follow-up", "");

        when(mockApptRepo.getUpcomingAppointmentsForDoctorOnDay("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(insideAppt, outsideAppt));

        List<Appointment> affected = service.getAffectedAppointments(newAvail);
        assertEquals(1, affected.size());
        assertEquals(outsideAppt, affected.get(0));
    }
}
