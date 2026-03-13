package com.example.clinicflow.business;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.validation.ValidationExceptions;
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
    public void testAddDoctorAvailability_Overlap_ThrowsException() {
        DoctorAvailability newAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(9, 0), LocalTime.of(11, 0));
        DoctorAvailability existingAvail = new DoctorAvailability("doc@test.com", 1, LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(mockRepo.getDoctorAvailability("doc@test.com", 1)).thenReturn(Arrays.asList(existingAvail));

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
}
