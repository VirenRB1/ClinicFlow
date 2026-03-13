package com.example.clinicflow.business;

import static org.junit.Assert.assertEquals;
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
import java.util.Collections;
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
    public void testGetUpcomingAppointmentsForPatient_Exhaustive() {
        String patientEmail = "patient@test.com";
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(1);

        // 1. Future date, Confirmed (Included) - tests apptDate.isAfter(today)
        Appointment appt1 = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "", "");
        // 2. Future date, NOT Confirmed (Excluded) - tests status check
        Appointment appt2 = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(11, 0), LocalTime.of(11, 30), "Completed", "", "");
        // 3. Today, future time, Confirmed (Included) - tests apptDate.equals(today) && startTime.isAfter(now)
        // Using MAX to ensure it's in the future regardless of test run time.
        Appointment appt3 = new Appointment("doc@test.com", patientEmail, today, LocalTime.MAX, LocalTime.MAX, "Confirmed", "", "");
        // 4. Today, past time, Confirmed (Excluded) - tests apptDate.equals(today) && startTime.isAfter(now) is false
        Appointment appt4 = new Appointment("doc@test.com", patientEmail, today, LocalTime.MIN, LocalTime.MIN.plusMinutes(30), "Confirmed", "", "");
        // 5. Past date, Confirmed (Excluded) - tests apptDate.isAfter and apptDate.equals are both false
        Appointment appt5 = new Appointment("doc@test.com", patientEmail, today.minusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "", "");
        // 6. Sorting check: Future early (9:00), Future late (10:00)
        Appointment appt6 = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(9, 0), LocalTime.of(9, 30), "Confirmed", "", "");

        when(mockUserRepository.getAppointmentsForPatient(patientEmail))
                .thenReturn(Arrays.asList(appt1, appt2, appt3, appt4, appt5, appt6));

        List<Appointment> results = appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        // Should include appt3 (today), appt6 (tomorrow 9:00), appt1 (tomorrow 10:00)
        assertEquals(3, results.size());
        assertEquals(appt3, results.get(0));
        assertEquals(appt6, results.get(1));
        assertEquals(appt1, results.get(2));
    }

    @Test
    public void testGetPastAppointmentsForPatient_Exhaustive() {
        String patientEmail = "patient@test.com";
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);

        // 1. Status Completed (Included regardless of time) - tests branch 1 of OR
        Appointment appt1 = new Appointment("doc@test.com", patientEmail, today.plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), "Completed", "", "");
        // 2. Status Confirmed, Past date (Included) - tests branch 2 of OR (isPastTime && !Cancelled)
        Appointment appt2 = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "", "");
        // 3. Status Confirmed, Today past time (Included) - tests isPastTime = true for today
        Appointment appt3 = new Appointment("doc@test.com", patientEmail, today, LocalTime.MIN, LocalTime.MIN.plusMinutes(30), "Confirmed", "", "");
        // 4. Status Cancelled, Past date (Excluded) - tests ! "Cancelled".equalsIgnoreCase
        Appointment appt4 = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(11, 0), LocalTime.of(11, 30), "Cancelled", "", "");
        // 5. Status Confirmed, Future date (Excluded) - tests isPastTime = false
        Appointment appt5 = new Appointment("doc@test.com", patientEmail, today.plusDays(1), LocalTime.of(12, 0), LocalTime.of(12, 30), "Confirmed", "", "");

        when(mockUserRepository.getAppointmentsForPatient(patientEmail))
                .thenReturn(Arrays.asList(appt1, appt2, appt3, appt4, appt5));

        List<Appointment> results = appointmentService.getPastAppointmentsForPatient(patientEmail);

        assertEquals(3, results.size());
        assertTrue(results.contains(appt1));
        assertTrue(results.contains(appt2));
        assertTrue(results.contains(appt3));
    }

    @Test
    public void testBookAppointment_AvailabilityAndConflict_Exhaustive() throws ValidationExceptions.ValidationException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        String docEmail = "doc@test.com";
        int dayOfWeek = futureDate.getDayOfWeek().getValue();

        // Availabilities: 9-10 and 11-12
        DoctorAvailability avail1 = new DoctorAvailability(docEmail, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(10, 0));
        DoctorAvailability avail2 = new DoctorAvailability(docEmail, dayOfWeek, LocalTime.of(11, 0), LocalTime.of(12, 0));
        when(mockUserRepository.getDoctorAvailability(docEmail, dayOfWeek)).thenReturn(Arrays.asList(avail1, avail2));

        // 1. Match exactly first availability (start.equals and end.equals)
        Appointment appt1 = new Appointment(docEmail, "p1", futureDate, LocalTime.of(9, 0), LocalTime.of(10, 0), "Pending", "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, futureDate)).thenReturn(Collections.emptyList());
        appointmentService.bookAppointment(appt1);

        // 2. Strictly inside second availability (start.isAfter and end.isBefore)
        Appointment appt2 = new Appointment(docEmail, "p2", futureDate, LocalTime.of(11, 15), LocalTime.of(11, 45), "Pending", "", "");
        appointmentService.bookAppointment(appt2);

        // 3. Match start of second availability (covers skip first avail, match start of second)
        Appointment appt3 = new Appointment(docEmail, "p3", futureDate, LocalTime.of(11, 0), LocalTime.of(11, 30), "Pending", "", "");
        appointmentService.bookAppointment(appt3);

        // 4. Match end of second availability
        Appointment appt4 = new Appointment(docEmail, "p4", futureDate, LocalTime.of(11, 30), LocalTime.of(12, 0), "Pending", "", "");
        appointmentService.bookAppointment(appt4);

        // 5. Conflict check: Touching (No conflict) - tests start.isBefore(appt.getEndTime) is false
        Appointment existing = new Appointment(docEmail, "p1", futureDate, LocalTime.of(11, 0), LocalTime.of(11, 30), "Confirmed", "", "");
        Appointment newAppt5 = new Appointment(docEmail, "p5", futureDate, LocalTime.of(11, 30), LocalTime.of(12, 0), "Pending", "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, futureDate)).thenReturn(Arrays.asList(existing));
        appointmentService.bookAppointment(newAppt5);

        // 6. Conflict check: Touching before (No conflict) - tests appt.getStartTime.isBefore(end) is false
        Appointment existing2 = new Appointment(docEmail, "p1", futureDate, LocalTime.of(9, 30), LocalTime.of(10, 0), "Confirmed", "", "");
        Appointment newAppt6 = new Appointment(docEmail, "p6", futureDate, LocalTime.of(9, 0), LocalTime.of(9, 30), "Pending", "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, futureDate)).thenReturn(Arrays.asList(existing2));
        appointmentService.bookAppointment(newAppt6);
        
        // 7. Conflict check: Overlap (Throws)
        Appointment conflictAppt = new Appointment(docEmail, "p7", futureDate, LocalTime.of(11, 15), LocalTime.of(11, 45), "Pending", "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, futureDate)).thenReturn(Arrays.asList(existing));
        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> appointmentService.bookAppointment(conflictAppt));
    }

    @Test
    public void testBookAppointment_EdgeFailures() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        String docEmail = "doc@test.com";
        int dayOfWeek = futureDate.getDayOfWeek().getValue();

        DoctorAvailability avail = new DoctorAvailability(docEmail, dayOfWeek, LocalTime.of(10, 0), LocalTime.of(11, 0));
        when(mockUserRepository.getDoctorAvailability(docEmail, dayOfWeek)).thenReturn(Arrays.asList(avail));

        // 1. Past date
        assertThrows(ValidationExceptions.InvalidAppointmentDateException.class, () -> 
            appointmentService.bookAppointment(new Appointment(docEmail, "p", LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), "", "", "")));

        // 2. Start before availability (start.isAfter and start.equals are both false)
        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> 
            appointmentService.bookAppointment(new Appointment(docEmail, "p", futureDate, LocalTime.of(9, 30), LocalTime.of(10, 30), "", "", "")));

        // 3. End after availability (end.isBefore and end.equals are both false)
        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> 
            appointmentService.bookAppointment(new Appointment(docEmail, "p", futureDate, LocalTime.of(10, 30), LocalTime.of(11, 30), "", "", "")));
    }

    @Test
    public void testGetAvailableTimeSlots_SortingAndBoundary() {
        String docEmail = "doc@test.com";
        LocalDate date = LocalDate.now().plusDays(1);
        
        // avail1: 11:00 - 12:00 (2 slots). Tests isBefore (11:30 < 12:00) and equals (12:00 == 12:00) in generateTimeSlots
        DoctorAvailability avail1 = new DoctorAvailability(docEmail, date.getDayOfWeek().getValue(), LocalTime.of(11, 0), LocalTime.of(12, 0));
        // avail2: 9:00 - 9:30 (1 slot). Tests equals (9:30 == 9:30)
        DoctorAvailability avail2 = new DoctorAvailability(docEmail, date.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(9, 30));
        
        when(mockUserRepository.getDoctorAvailability(docEmail, date.getDayOfWeek().getValue())).thenReturn(Arrays.asList(avail1, avail2));
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, date)).thenReturn(Collections.emptyList());

        List<TimeSlot> slots = appointmentService.getAvailableTimeSlots(docEmail, date);

        assertEquals(3, slots.size());
        assertEquals(LocalTime.of(9, 0), slots.get(0).getStartTime()); // Sorted by final sort in method
        assertEquals(LocalTime.of(11, 0), slots.get(1).getStartTime());
        assertEquals(LocalTime.of(11, 30), slots.get(2).getStartTime());
    }

    @Test
    public void testUpdateMethods() {
        Appointment appt = new Appointment("d", "p", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "", "");
        appointmentService.completeAppointment(appt, "Note");
        assertEquals("Completed", appt.getStatus());
        assertEquals("Note", appt.getDoctorNotes());
        
        appointmentService.updateAppointmentStatus(appt, "Cancelled");
        assertEquals("Cancelled", appt.getStatus());
    }
}
