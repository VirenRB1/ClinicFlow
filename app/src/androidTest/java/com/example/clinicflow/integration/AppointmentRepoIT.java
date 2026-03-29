package com.example.clinicflow.integration;

import android.content.Context;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.real.AppDbHelper;
import com.example.clinicflow.persistence.real.SqlRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AppointmentRepoIT {
    private UserRepository repo;
    private AppointmentService appointmentService;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        context.deleteDatabase(AppDbHelper.DATABASE_NAME);

        repo = new SqlRepository(context);
        appointmentService = new AppointmentService(repo);
    }

    @Test
    public void bookAppointment() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctor1@gmail.com";
        String patientEmail = "patient11@gmail.com";

        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment apptmt = new Appointment(
                doctorEmail,
                patientEmail,
                date,
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );

        appointmentService.bookAppointment(apptmt);

        List<Appointment> apptDoctor = repo.getUpcomingAppointmentsForDoctor(doctorEmail);
        List<Appointment> apptPatient = repo.getUpcomingAppointmentsForPatient(patientEmail);

        assertEquals(1, apptDoctor.size());
        assertEquals(1, apptPatient.size());
        assertEquals(AppointmentStatus.CONFIRMED, apptDoctor.get(0).getStatus());

        boolean slotRemoved = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .noneMatch(s ->
                        s.getStartTime().equals(LocalTime.of(11, 0)) &&
                                s.getEndTime().equals(LocalTime.of(11, 30)));

        assertTrue(slotRemoved);
    }

    @Test
    public void pastAndUpcomingAppointments() throws ValidationExceptions.ValidationException {
        LocalDate today = LocalDate.now();
        String doctorEmail = "doctorPU@gmail.com";
        String patientEmail = "patientPU@gmail.com";

        LocalDate upcomingDate = today.plusDays(2);
        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                upcomingDate.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment upcoming = new Appointment(
                doctorEmail,
                patientEmail,
                upcomingDate,
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );

        appointmentService.bookAppointment(upcoming);

        List<Appointment> upcomingList = repo.getUpcomingAppointmentsForPatient(patientEmail);
        List<Appointment> completedList = repo.getCompletedAppointmentsForPatient(patientEmail);

        assertEquals(1, upcomingList.size());
        assertEquals(0, completedList.size());

        List<Appointment> serviceUpcoming =
                appointmentService.getUpcomingAppointmentsForPatient(patientEmail);
        List<Appointment> servicePast =
                appointmentService.getPastAppointmentsForPatient(patientEmail);

        assertEquals(1, serviceUpcoming.size());
        assertEquals(0, servicePast.size());
    }

    @Test(expected = ValidationExceptions.AppointmentConflictException.class)
    public void bookingConflictingSlotForDifferentPatientThrowsException() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctorConflict@gmail.com";

        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment first = new Appointment(
                doctorEmail,
                "patient1@gmail.com",
                date,
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );

        Appointment conflict = new Appointment(
                doctorEmail,
                "patient2@gmail.com",  // different patient, same slot
                date,
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                AppointmentStatus.CONFIRMED,
                "Follow-up",
                ""
        );

        appointmentService.bookAppointment(first);
        appointmentService.bookAppointment(conflict); // should throw
    }

    @Test(expected = ValidationExceptions.InvalidAppointmentDateException.class)
    public void bookingAppointmentInPastThrowsException() throws ValidationExceptions.ValidationException {
        Appointment pastAppointment = new Appointment(
                "doctorPast@gmail.com",
                "patientPast@gmail.com",
                LocalDate.now().minusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );

        appointmentService.bookAppointment(pastAppointment); // should throw
    }

    @Test
    public void completingAppointmentMovesItFromUpcomingToRecords() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctorComplete@gmail.com";
        String patientEmail = "patientComplete@gmail.com";

        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment appointment = new Appointment(
                doctorEmail,
                patientEmail,
                date,
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                AppointmentStatus.CONFIRMED,
                "Annual physical",
                ""
        );

        appointmentService.bookAppointment(appointment);

        Appointment booked = repo.getUpcomingAppointmentsForPatient(patientEmail).get(0);
        appointmentService.completeAppointment(booked, "Patient is healthy.");

        List<Appointment> upcoming = repo.getUpcomingAppointmentsForPatient(patientEmail);
        List<Appointment> completed = repo.getCompletedAppointmentsForPatient(patientEmail);

        assertEquals(0, upcoming.size());
        assertEquals(1, completed.size());
        assertEquals(AppointmentStatus.COMPLETED, completed.get(0).getStatus());
        assertEquals("Patient is healthy.", completed.get(0).getDoctorNotes());
    }

    @Test
    public void cancellingAppointmentRemovesItFromUpcoming() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctorCancel@gmail.com";
        String patientEmail = "patientCancel@gmail.com";

        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment appointment = new Appointment(
                doctorEmail,
                patientEmail,
                date,
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                AppointmentStatus.CONFIRMED,
                "Consultation",
                ""
        );

        appointmentService.bookAppointment(appointment);

        Appointment booked = repo.getUpcomingAppointmentsForPatient(patientEmail).get(0);
        appointmentService.cancelAppointment(booked);

        List<Appointment> upcoming = repo.getUpcomingAppointmentsForPatient(patientEmail);
        assertEquals(0, upcoming.size());
    }

    @Test
    public void rebookAppointmentWithDifferentPatient() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctor1@gmail.com";
        String patientEmail = "patient11@gmail.com";
        String secondPatientEmail = "patient12@gmail.com";


        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                date.getDayOfWeek().getValue(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        Appointment apptmt = new Appointment(
                doctorEmail,
                patientEmail,
                date,
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );

        appointmentService.bookAppointment(apptmt);

        List<Appointment> apptDoctor = repo.getUpcomingAppointmentsForDoctor(doctorEmail);
        List<Appointment> apptPatient = repo.getUpcomingAppointmentsForPatient(patientEmail);

        assertEquals(1, apptDoctor.size());
        assertEquals(1, apptPatient.size());
        assertEquals(AppointmentStatus.CONFIRMED, apptDoctor.get(0).getStatus());

        boolean slotRemoved = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .noneMatch(s ->
                        s.getStartTime().equals(LocalTime.of(11, 0)) &&
                                s.getEndTime().equals(LocalTime.of(11, 30)));

        assertTrue(slotRemoved);

        Appointment booked = repo.getUpcomingAppointmentsForPatient(patientEmail).get(0);
        appointmentService.cancelAppointment(booked);

        List<Appointment> upcoming = repo.getUpcomingAppointmentsForPatient(patientEmail);
        assertEquals(0, upcoming.size());

        // Check that slot is available for second patient
        boolean slotAvailable = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .anyMatch(s ->
                        s.getStartTime().equals(LocalTime.of(11, 0)) &&
                                s.getEndTime().equals(LocalTime.of(11, 30)));
        assertTrue(slotAvailable);

        // Rebook with second patient for same time slot
        Appointment rebook = new Appointment(
                doctorEmail,
                secondPatientEmail,
                date,
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                AppointmentStatus.CONFIRMED,
                "Checkup",
                ""
        );


        appointmentService.bookAppointment(rebook);

        List<Appointment> apptDoctorRebook = repo.getUpcomingAppointmentsForDoctor(doctorEmail);
        List<Appointment> apptPatientRebook = repo.getUpcomingAppointmentsForPatient(secondPatientEmail);

        assertEquals(1, apptDoctorRebook.size());
        assertEquals(1, apptPatientRebook.size());
        assertEquals(AppointmentStatus.CONFIRMED, apptDoctorRebook.get(0).getStatus());

        boolean slotRemovedRebook = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .noneMatch(s ->
                        s.getStartTime().equals(LocalTime.of(11, 0)) &&
                                s.getEndTime().equals(LocalTime.of(11, 30)));

        assertTrue(slotRemovedRebook);
    }
}