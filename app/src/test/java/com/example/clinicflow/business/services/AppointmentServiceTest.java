package com.example.clinicflow.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
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
import java.util.List;

public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private AppointmentPersistence mockPersistence;
    private DoctorAvailabilityPersistence mockAvailabilityPersistence;

    @Before
    public void setUp() {
        mockPersistence = mock(AppointmentPersistence.class);
        mockAvailabilityPersistence = mock(DoctorAvailabilityPersistence.class);
        appointmentService = new AppointmentService(mockPersistence, mockAvailabilityPersistence);
    }

    @Test
    public void testGetUpcomingAppointmentsForPatient_Sorting() {
        String patientEmail = "patient@test.com";
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);

        Appointment appt1 = new Appointment("doc@test.com", patientEmail, nextWeek, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        Appointment appt2 = new Appointment("doc@test.com", patientEmail, tomorrow, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "", "");
        Appointment appt3 = new Appointment("doc@test.com", patientEmail, tomorrow, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");

        when(mockPersistence.getUpcomingAppointmentsForPatient(patientEmail))
                .thenReturn(Arrays.asList(appt1, appt2, appt3));

        List<Appointment> results = appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        assertEquals(3, results.size());
        assertEquals(appt3, results.get(0));
        assertEquals(appt2, results.get(1));
        assertEquals(appt1, results.get(2));
    }

    @Test
    public void testGetPastAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Appointment appt = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.COMPLETED, "", "");
        
        when(mockPersistence.getCompletedAppointmentsForPatient(patientEmail))
                .thenReturn(Collections.singletonList(appt));

        List<Appointment> results = appointmentService.getPastAppointmentsForPatient(patientEmail);
        assertEquals(1, results.size());
    }

    @Test
    public void testGetUpcomingAppointmentsForDoctor() {
        String docEmail = "doc@test.com";
        when(mockPersistence.getUpcomingAppointmentsForDoctor(docEmail))
                .thenReturn(Collections.emptyList());
        List<Appointment> results = appointmentService.getUpcomingAppointmentsForDoctor(docEmail);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCompleteAppointment() {
        Appointment appt = new Appointment("doc@test.com", "p@test.com", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "Checkup", "");
        appointmentService.completeAppointment(appt, "Note");
        assertEquals(AppointmentStatus.COMPLETED, appt.getStatus());
        assertEquals("Note", appt.getDoctorNotes());
        verify(mockPersistence).updateAppointment(appt);
    }

    @Test
    public void testBookAppointment_PastDate_ThrowsException() {
        Appointment appt = new Appointment("d", "p", LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "", "");
        assertThrows(ValidationExceptions.InvalidAppointmentDateException.class, () -> appointmentService.bookAppointment(appt));
    }

    @Test
    public void testBookAppointment_OutsideAvailability_ThrowsException() {
        LocalDate future = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p", future, LocalTime.of(8, 0), LocalTime.of(8, 30), AppointmentStatus.CONFIRMED, "", "");
        
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockAvailabilityPersistence.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> appointmentService.bookAppointment(appt));
    }

    @Test
    public void testBookAppointment_ConflictWithExisting_ThrowsException() {
        LocalDate future = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p2", future, LocalTime.of(9, 15), LocalTime.of(9, 45), AppointmentStatus.CONFIRMED, "", "");
        
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockAvailabilityPersistence.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        Appointment existing = new Appointment("doc@test.com", "p1", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        when(mockPersistence.getAppointmentsForDoctorOnDate("doc@test.com", future))
                .thenReturn(Collections.singletonList(existing));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> appointmentService.bookAppointment(appt));
    }

    @Test
    public void testBookAppointment_OverlapWithCancelled_Success() throws ValidationExceptions.ValidationException {
        LocalDate future = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p2", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockAvailabilityPersistence.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        Appointment cancelled = new Appointment("doc@test.com", "p1", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CANCELLED, "", "");
        when(mockPersistence.getAppointmentsForDoctorOnDate("doc@test.com", future))
                .thenReturn(Collections.singletonList(cancelled));

        appointmentService.bookAppointment(appt);
        verify(mockPersistence).addAppointment(appt);
    }

    @Test
    public void testCancelAppointment_Failures() {
        LocalDate future = LocalDate.now().plusDays(1);
        
        Appointment cancelled = new Appointment("d", "p", future, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CANCELLED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(cancelled));

        Appointment completed = new Appointment("d", "p", future, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.COMPLETED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(completed));

        Appointment past = new Appointment("d", "p", LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(past));
    }

}
