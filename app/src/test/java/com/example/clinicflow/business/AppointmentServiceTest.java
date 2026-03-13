package com.example.clinicflow.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private UserRepository mockUserRepository;

    @Before
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        appointmentService = new AppointmentService(mockUserRepository);
    }

    @Test
    public void testGetUpcomingAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "Checkup", "");
        
        when(mockUserRepository.getAppointmentsForPatient(patientEmail)).thenReturn(Arrays.asList(appt));

        List<Appointment> results = appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        assertEquals(1, results.size());
        assertEquals(appt, results.get(0));
    }

    @Test
    public void testGetPastAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Appointment appt = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "Checkup", "");
        
        when(mockUserRepository.getAppointmentsForPatient(patientEmail)).thenReturn(Arrays.asList(appt));

        List<Appointment> results = appointmentService.getPastAppointmentsForPatient(patientEmail);

        assertEquals(1, results.size());
        assertEquals(appt, results.get(0));
    }

    @Test
    public void testCompleteAppointment() {
        Appointment appt = new Appointment("doc@test.com", "p@test.com", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "Checkup", "");
        String note = "Patient is healthy";

        appointmentService.completeAppointment(appt, note);

        assertEquals("Completed", appt.getStatus());
        assertEquals(note, appt.getDoctorNotes());
        verify(mockUserRepository).updateAppointment(appt);
    }

    @Test
    public void testBookAppointment_PastDate_ThrowsException() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p@test.com", pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "Checkup", "");

        assertThrows(ValidationExceptions.InvalidAppointmentDateException.class, () -> 
            appointmentService.bookAppointment(appt)
        );
    }

    @Test
    public void testBookAppointment_OutsideAvailability_ThrowsException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p@test.com", futureDate, LocalTime.of(14, 0), LocalTime.of(14, 30), "Confirmed", "Checkup", "");
        
        // Availability is only 9-10 AM
        DoctorAvailability availability = new DoctorAvailability("doc@test.com", futureDate.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        
        when(mockUserRepository.getDoctorAvailability("doc@test.com", futureDate.getDayOfWeek().getValue()))
                .thenReturn(Arrays.asList(availability));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> 
            appointmentService.bookAppointment(appt)
        );
    }

    @Test
    public void testBookAppointment_Conflict_ThrowsException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Appointment newAppt = new Appointment("doc@test.com", "p2@test.com", futureDate, LocalTime.of(9, 0), LocalTime.of(9, 30), "Confirmed", "Consult", "");
        
        DoctorAvailability availability = new DoctorAvailability("doc@test.com", futureDate.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Appointment existingAppt = new Appointment("doc@test.com", "p1@test.com", futureDate, LocalTime.of(9, 0), LocalTime.of(9, 30), "Confirmed", "Checkup", "");

        when(mockUserRepository.getDoctorAvailability("doc@test.com", futureDate.getDayOfWeek().getValue()))
                .thenReturn(Arrays.asList(availability));
        when(mockUserRepository.getAppointmentsForDoctorOnDate("doc@test.com", futureDate))
                .thenReturn(Arrays.asList(existingAppt));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> 
            appointmentService.bookAppointment(newAppt)
        );
    }

    @Test
    public void testGetAvailableTimeSlots() {
        String docEmail = "doc@test.com";
        LocalDate date = LocalDate.now().plusDays(1);
        
        DoctorAvailability availability = new DoctorAvailability(docEmail, date.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Appointment existingAppt = new Appointment(docEmail, "p1@test.com", date, LocalTime.of(9, 0), LocalTime.of(9, 30), "Confirmed", "Checkup", "");

        when(mockUserRepository.getDoctorAvailability(docEmail, date.getDayOfWeek().getValue()))
                .thenReturn(Arrays.asList(availability));
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, date))
                .thenReturn(Arrays.asList(existingAppt));

        List<TimeSlot> slots = appointmentService.getAvailableTimeSlots(docEmail, date);

        // Expected slots: 9:00-9:30 (busy), 9:30-10:00 (available)
        // getAvailableTimeSlots returns ONLY available slots.
        assertEquals(1, slots.size());
        assertEquals(LocalTime.of(9, 30), slots.get(0).getStartTime());
        assertTrue(slots.get(0).isAvailable());
    }
}
