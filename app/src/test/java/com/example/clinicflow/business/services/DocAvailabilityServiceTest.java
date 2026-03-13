package com.example.clinicflow.business.services;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

public class DocAvailabilityServiceTest {

    private DocAvailabilityService service;
    private UserRepository mockRepo;

    @Before
    public void setUp() {
        mockRepo = mock(UserRepository.class);
        service = new DocAvailabilityService(mockRepo);
    }

    @Test
    public void testAddDoctorAvailability_Success() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(10, 0));
        
        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Collections.emptyList());

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_BeforeExisting() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(8, 0), LocalTime.of(9, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_AfterExisting() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(13, 0), LocalTime.of(14, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_TouchingExistingEnd() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(12, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_NoOverlap_TouchingExistingStart() throws ValidationExceptions.ValidationException {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(10, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        service.addDoctorAvailability(newAvail);

        verify(mockRepo).addDoctorAvailability(newAvail);
    }

    @Test
    public void testAddDoctorAvailability_Overlap_StartsBeforeEndsDuring() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(11, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () -> 
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_StartsDuringEndsAfter() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(11, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () -> 
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_ContainedWithin() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(11, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () -> 
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_Overlap_CoversExisting() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(8, 0), LocalTime.of(13, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () -> 
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_MultipleExisting_OverlapWithSecond() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(13, 0), LocalTime.of(15, 0));
        DoctorAvailability first = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(11, 0));
        DoctorAvailability second = new DoctorAvailability("doc@test.com", 1, LocalTime.of(14, 0), LocalTime.of(16, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(first, second));

        assertThrows(ValidationExceptions.AvailabilityOverlapException.class, () -> 
            service.addDoctorAvailability(newAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_InvalidTime_ThrowsValidationException() {
        // Validator checks for "on the hour". 09:30 is invalid.
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 30), LocalTime.of(10, 0));

        assertThrows(ValidationExceptions.ValidationException.class, () -> 
            service.addDoctorAvailability(invalidAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_EndTimeBeforeStartTime_ThrowsException() {
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(11, 0), LocalTime.of(9, 0));

        assertThrows(ValidationExceptions.InvalidStartAndEndTimeException.class, () -> 
            service.addDoctorAvailability(invalidAvail)
        );
    }

    @Test
    public void testAddDoctorAvailability_NullTimes_ThrowsException() {
        DoctorAvailability invalidAvail = new DoctorAvailability("doc@test.com", 1, null, null);

        assertThrows(ValidationExceptions.EmptyFieldException.class, () ->
            service.addDoctorAvailability(invalidAvail)
        );
    }
}
