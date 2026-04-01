package com.example.clinicflow.business.validators;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.TimeSlot;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppointmentValidatorTest {

    private AppointmentValidator validator;

    // Fixed to Wednesday March 25, 2026 at 8:00 AM
    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 3, 25);

    @Before
    public void setUp() {
        Clock fixedClock = Clock.fixed(
                ZonedDateTime.of(FIXED_TODAY.atTime(8, 0), ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        validator = new AppointmentValidator(fixedClock);
    }

    @Test
    public void testValidateDoctorAndDate_NullDoctor() {
        assertThrows(ValidationExceptions.MissingDoctorException.class, () -> {
            validator.validateDoctorAndDate(null, FIXED_TODAY);
        });
    }

    @Test
    public void testValidateDoctorAndDate_NullDate() {
        Doctor doctor = mock(Doctor.class);
        assertThrows(ValidationExceptions.MissingAppointmentDateException.class, () -> {
            validator.validateDoctorAndDate(doctor, null);
        });
    }

    @Test
    public void testValidateDoctorAndDate_PastDate() {
        Doctor doctor = mock(Doctor.class);
        assertThrows(ValidationExceptions.InvalidAppointmentDateException.class, () -> {
            validator.validateDoctorAndDate(doctor, FIXED_TODAY.minusDays(1));
        });
    }

    @Test
    public void testValidateDoctorAndDate_Valid() throws ValidationExceptions.ValidationException {
        Doctor doctor = mock(Doctor.class);
        validator.validateDoctorAndDate(doctor, FIXED_TODAY);
        validator.validateDoctorAndDate(doctor, FIXED_TODAY.plusDays(1));
    }

    @Test
    public void testValidateAppointmentConfirmation_NullPatientEmail() {
        assertThrows(ValidationExceptions.MissingPatientEmailException.class, () -> {
            validator.validateAppointmentConfirmation(null, "doc@test.com", FIXED_TODAY, mock(TimeSlot.class), "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_EmptyPatientEmail() {
        assertThrows(ValidationExceptions.MissingPatientEmailException.class, () -> {
            validator.validateAppointmentConfirmation("", "doc@test.com", FIXED_TODAY, mock(TimeSlot.class), "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_NullDoctorEmail() {
        assertThrows(ValidationExceptions.MissingDoctorEmailException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", null, FIXED_TODAY, mock(TimeSlot.class), "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_EmptyDoctorEmail() {
        assertThrows(ValidationExceptions.MissingDoctorEmailException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", "  ", FIXED_TODAY, mock(TimeSlot.class), "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_NullDate() {
        assertThrows(ValidationExceptions.MissingAppointmentDateException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", "doc@test.com", null, mock(TimeSlot.class), "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_NullSlot() {
        assertThrows(ValidationExceptions.MissingTimeSlotException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", "doc@test.com", FIXED_TODAY, null, "Checkup");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_NullPurpose() {
        assertThrows(ValidationExceptions.MissingPurposeException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", "doc@test.com", FIXED_TODAY, mock(TimeSlot.class), null);
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_EmptyPurpose() {
        assertThrows(ValidationExceptions.MissingPurposeException.class, () -> {
            validator.validateAppointmentConfirmation("patient@test.com", "doc@test.com", FIXED_TODAY, mock(TimeSlot.class), " ");
        });
    }

    @Test
    public void testValidateAppointmentConfirmation_Valid() throws ValidationExceptions.ValidationException {
        validator.validateAppointmentConfirmation("patient@test.com", "doc@test.com", FIXED_TODAY, mock(TimeSlot.class), "Checkup");
    }
}
