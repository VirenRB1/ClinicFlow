package com.example.clinicflow.business.validators;

import static org.junit.Assert.assertThrows;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

public class AvailabilityValidatorTest {

    private AvailabilityValidator validator;

    @Before
    public void setUp() {
        validator = new AvailabilityValidator();
    }

    @Test
    public void testValidateAvailability_ValidData_NoException() throws ValidationExceptions.ValidationException {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(9, 0), LocalTime.of(17, 0));
        validator.validateAvailability(availability);
    }

    @Test
    public void testValidateAvailability_NullStartTime_ThrowsEmptyFieldException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, null, LocalTime.of(17, 0));
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> validator.validateAvailability(availability));
    }

    @Test
    public void testValidateAvailability_NullEndTime_ThrowsEmptyFieldException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(9, 0), null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> validator.validateAvailability(availability));
    }

    @Test
    public void testValidateAvailability_StartTimeAfterEndTime_ThrowsInvalidStartAndEndTimeException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(17, 0), LocalTime.of(9, 0));
        assertThrows(ValidationExceptions.InvalidStartAndEndTimeException.class, () -> validator.validateAvailability(availability));
    }

    @Test
    public void testValidateAvailability_StartTimeEqualsEndTime_ThrowsInvalidStartAndEndTimeException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(9, 0), LocalTime.of(9, 0));
        assertThrows(ValidationExceptions.InvalidStartAndEndTimeException.class, () -> validator.validateAvailability(availability));
    }

    @Test
    public void testValidateAvailability_StartTimeNotOnHour_ThrowsValidationException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(9, 30), LocalTime.of(17, 0));
        assertThrows(ValidationExceptions.ValidationException.class, () -> validator.validateAvailability(availability));
    }

    @Test
    public void testValidateAvailability_EndTimeNotOnHour_ThrowsValidationException() {
        DoctorAvailability availability = new DoctorAvailability("doctor@example.com", 1, LocalTime.of(9, 0), LocalTime.of(17, 15));
        assertThrows(ValidationExceptions.ValidationException.class, () -> validator.validateAvailability(availability));
    }
}
