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
    public void testGetUpcomingAppointmentsForPatient_Sorting() {
        String patientEmail = "patient@test.com";
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);

        // Different dates to hit dateCompare != 0
        Appointment appt1 = new Appointment("doc@test.com", patientEmail, nextWeek, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        Appointment appt2 = new Appointment("doc@test.com", patientEmail, tomorrow, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "", "");
        Appointment appt3 = new Appointment("doc@test.com", patientEmail, tomorrow, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");

        when(mockUserRepository.getUpcomingAppointmentsForPatient(patientEmail))
                .thenReturn(Arrays.asList(appt1, appt2, appt3));

        List<Appointment> results = appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        assertEquals(3, results.size());
        assertEquals(appt3, results.get(0)); // Tomorrow 9:00
        assertEquals(appt2, results.get(1)); // Tomorrow 10:00
        assertEquals(appt1, results.get(2)); // Next week 9:00
    }

    @Test
    public void testGetPastAppointmentsForPatient() {
        String patientEmail = "patient@test.com";
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Appointment appt = new Appointment("doc@test.com", patientEmail, pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.COMPLETED, "", "");
        
        when(mockUserRepository.getCompletedAppointmentsForPatient(patientEmail))
                .thenReturn(Collections.singletonList(appt));

        List<Appointment> results = appointmentService.getPastAppointmentsForPatient(patientEmail);
        assertEquals(1, results.size());
    }

    @Test
    public void testGetUpcomingAppointmentsForDoctor() {
        String docEmail = "doc@test.com";
        when(mockUserRepository.getUpcomingAppointmentsForDoctor(docEmail))
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
        verify(mockUserRepository).updateAppointment(appt);
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
        
        // Doctor only available 9-10
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockUserRepository.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> appointmentService.bookAppointment(appt));
    }

    @Test
    public void testBookAppointment_ConflictWithExisting_ThrowsException() {
        LocalDate future = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p2", future, LocalTime.of(9, 15), LocalTime.of(9, 45), AppointmentStatus.CONFIRMED, "", "");
        
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockUserRepository.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        Appointment existing = new Appointment("doc@test.com", "p1", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate("doc@test.com", future))
                .thenReturn(Collections.singletonList(existing));

        assertThrows(ValidationExceptions.AppointmentConflictException.class, () -> appointmentService.bookAppointment(appt));
    }

    @Test
    public void testBookAppointment_OverlapWithCancelled_Success() throws ValidationExceptions.ValidationException {
        LocalDate future = LocalDate.now().plusDays(1);
        Appointment appt = new Appointment("doc@test.com", "p2", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CONFIRMED, "", "");
        
        DoctorAvailability avail = new DoctorAvailability("doc@test.com", future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(mockUserRepository.getDoctorAvailability("doc@test.com", future.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));

        Appointment cancelled = new Appointment("doc@test.com", "p1", future, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.CANCELLED, "", "");
        when(mockUserRepository.getAppointmentsForDoctorOnDate("doc@test.com", future))
                .thenReturn(Collections.singletonList(cancelled));

        appointmentService.bookAppointment(appt);
        verify(mockUserRepository).addAppointment(appt);
    }

    @Test
    public void testCancelAppointment_Failures() {
        LocalDate future = LocalDate.now().plusDays(1);
        
        // Already cancelled
        Appointment cancelled = new Appointment("d", "p", future, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CANCELLED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(cancelled));

        // Already completed
        Appointment completed = new Appointment("d", "p", future, LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.COMPLETED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(completed));

        // Past date
        Appointment past = new Appointment("d", "p", LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.CONFIRMED, "", "");
        assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(past));

        // Today, but past time
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(1, 0))) {
            Appointment todayPast = new Appointment("d", "p", LocalDate.now(), now.minusHours(1), now.minusMinutes(30), AppointmentStatus.CONFIRMED, "", "");
            assertThrows(ValidationExceptions.AppointmentCancellationException.class, () -> appointmentService.cancelAppointment(todayPast));
        }
    }

    @Test
    public void testGetAvailableTimeSlots_TodayPastAndFuture() {
        String docEmail = "doc@test.com";
        LocalDate today = LocalDate.now();
        // Set availability for the whole day
        DoctorAvailability avail = new DoctorAvailability(docEmail, today.getDayOfWeek().getValue(), LocalTime.MIN, LocalTime.MAX.minusMinutes(1));
        when(mockUserRepository.getDoctorAvailability(docEmail, today.getDayOfWeek().getValue()))
                .thenReturn(Collections.singletonList(avail));
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, today))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = appointmentService.getAvailableTimeSlots(docEmail, today);
        
        // Verify that all returned slots are in the future
        LocalTime now = LocalTime.now();
        for (TimeSlot slot : slots) {
            assertTrue("Slot " + slot.getStartTime() + " should be after " + now, slot.getStartTime().isAfter(now));
        }
    }

    @Test
    public void testGetAvailableTimeSlots_MultipleAvailabilities() {
        String docEmail = "doc@test.com";
        LocalDate future = LocalDate.now().plusDays(1);
        
        DoctorAvailability avail1 = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        DoctorAvailability avail2 = new DoctorAvailability(docEmail, future.getDayOfWeek().getValue(), LocalTime.of(14, 0), LocalTime.of(15, 0));
        
        when(mockUserRepository.getDoctorAvailability(docEmail, future.getDayOfWeek().getValue()))
                .thenReturn(Arrays.asList(avail1, avail2));
        when(mockUserRepository.getAppointmentsForDoctorOnDate(docEmail, future))
                .thenReturn(Collections.emptyList());

        List<TimeSlot> slots = appointmentService.getAvailableTimeSlots(docEmail, future);
        
        // 2 slots in first block (9:00, 9:30), 2 in second (14:00, 14:30)
        assertEquals(4, slots.size());
        assertEquals(LocalTime.of(9, 0), slots.get(0).getStartTime());
        assertEquals(LocalTime.of(9, 30), slots.get(1).getStartTime());
        assertEquals(LocalTime.of(14, 0), slots.get(2).getStartTime());
        assertEquals(LocalTime.of(14, 30), slots.get(3).getStartTime());
    }
}
