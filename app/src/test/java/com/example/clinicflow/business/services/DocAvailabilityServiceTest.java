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
import java.util.Collections;

public class DocAvailabilityServiceTest {
    private DocAvailabilityService service;
    private DoctorAvailabilityPersistence mockRepo;
    private AppointmentPersistence mockApptRepo;

    // Use today's day-of-week so clearPastAvailability doesn't interfere
    private final int TODAY_DAY = LocalDate.now().getDayOfWeek().getValue();

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
    public void testReplaceAvailability_CancelsAppointmentsAndReplacesAvailability() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", TODAY_DAY, LocalTime.of(8, 0), LocalTime.of(14, 0));
        DoctorAvailability oldAvail1 = new DoctorAvailability(1, "doc@test.com", TODAY_DAY, LocalTime.of(9, 0), LocalTime.of(12, 0));
        DoctorAvailability oldAvail2 = new DoctorAvailability(2, "doc@test.com", TODAY_DAY, LocalTime.of(13, 0), LocalTime.of(17, 0));

        Appointment appt = new Appointment(1, "doc@test.com", "patient@test.com",
                LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentStatus.CONFIRMED, "Checkup", "");

        when(mockApptRepo.getUpcomingAppointmentsForDoctorOnDay("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(appt));
        when(mockRepo.getDoctorAvailability("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(oldAvail1, oldAvail2));

        service.replaceAvailability(newAvail);

        // Verify appointment was cancelled with message
        assertEquals(AppointmentStatus.CANCELLED, appt.getStatus());
        verify(mockApptRepo).updateAppointment(appt);

        // Verify both old availabilities were deleted
        verify(mockRepo).deleteDoctorAvailability(1);
        verify(mockRepo).deleteDoctorAvailability(2);

        // Verify new availability was added
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
    public void testGetAffectedAppointments() {
        Appointment appt1 = new Appointment(1, "doc@test.com", "p1@test.com",
                LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentStatus.CONFIRMED, "Checkup", "");
        Appointment appt2 = new Appointment(2, "doc@test.com", "p2@test.com",
                LocalDate.now().plusDays(7), LocalTime.of(10, 0), LocalTime.of(10, 30),
                AppointmentStatus.CONFIRMED, "Follow-up", "");

        when(mockApptRepo.getUpcomingAppointmentsForDoctorOnDay("doc@test.com", TODAY_DAY)).thenReturn(Arrays.asList(appt1, appt2));

        assertEquals(2, service.getAffectedAppointments("doc@test.com", TODAY_DAY).size());
    }
}
