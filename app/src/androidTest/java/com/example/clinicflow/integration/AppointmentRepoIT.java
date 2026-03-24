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
}