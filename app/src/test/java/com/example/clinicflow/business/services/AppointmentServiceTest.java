package com.example.clinicflow.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public void testGetUpcomingAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(1);

        // Sorting check: Future early (9:00), Future late (10:00)
        Appointment appt1 = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Confirmed", "", "");
        Appointment appt2 = new Appointment("doc@test.com", patientEmail, futureDate, LocalTime.of(9, 0), LocalTime.of(9, 30), "Confirmed", "", "");

        when(mockUserRepository.getUpcomingAppointmentsForPatient(patientEmail))
                .thenReturn(Arrays.asList(appt1, appt2));

        List<Appointment> results = appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        // Should include both and be sorted
        assertEquals(2, results.size());
        assertEquals(appt2, results.get(0)); // 9:00 first
        assertEquals(appt1, results.get(1)); // 10:00 second
    }

    @Test
    public void testGetPastAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Appointment appt1 = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), "Completed", "", "all ok");
        
        when(mockUserRepository.getCompletedAppointmentsForPatient(patientEmail))
                .thenReturn(Collections.singletonList(appt1));

        List<Appointment> results = appointmentService.getPastAppointmentsForPatient(patientEmail);

        assertEquals(1, results.size());
        assertEquals(appt1, results.get(0));
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

}
