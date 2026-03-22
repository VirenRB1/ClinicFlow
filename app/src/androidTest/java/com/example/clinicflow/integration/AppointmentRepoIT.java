package com.example.clinicflow.integration;

import android.content.Context;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
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

        // Clean DB so test is deterministic
        context.deleteDatabase(AppDbHelper.DATABASE_NAME);

        repo = new SqlRepository(context);
        appointmentService = new AppointmentService(repo);
    }

    @Test
    public void bookAppointment() throws ValidationExceptions.ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        String doctorEmail = "doctor1@gmail.com";
        String patientEmail = "patient11@gmail.com";

        // Add doctor availability so booking doesn't fail validation
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
                LocalTime.of(11,0),
                LocalTime.of(11,30),
                "Confirmed",
                "Checkup",
                ""
                );
        appointmentService.bookAppointment(apptmt);

        // Check that the appointment was booked
        List<Appointment> apptDoctor =
                repo.getUpcomingAppointmentsForDoctor(doctorEmail);
        List<Appointment> apptPatient =
                repo.getUpcomingAppointmentsForPatient(patientEmail);
        assertEquals(1, apptDoctor.size());
        assertEquals(1, apptPatient.size());
        assertEquals("Confirmed", apptDoctor.get(0).getStatus());

        // Check that slot is removed
        boolean slotRemoved = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .noneMatch(s -> s.getStartTime().equals(LocalTime.of(11,0)) &&
                        s.getEndTime().equals(LocalTime.of(11,30)));
        assertTrue(slotRemoved);
    }

    @Test
    public void pastAndUpcomingAppointments() throws ValidationExceptions.ValidationException {
        LocalDate today = LocalDate.now();
        String doctorEmail = "doctorPU@gmail.com";
        String patientEmail = "patientPU@gmail.com";

        // Add availability for today and the upcoming date
        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                today.getDayOfWeek().getValue(),
                LocalTime.of(9,0),
                LocalTime.of(17,0)
        ));

        LocalDate upcomingDate = today.plusDays(2);
        repo.addDoctorAvailability(new DoctorAvailability(
                doctorEmail,
                upcomingDate.getDayOfWeek().getValue(),
                LocalTime.of(9,0),
                LocalTime.of(17,0)
        ));

        // Past appointment
        Appointment past = new Appointment(
                doctorEmail,
                patientEmail,
                today.minusDays(1),
                LocalTime.of(10,0),
                LocalTime.of(10,30),
                "Completed",
                "Checkup",
                "all ok"
        );

        // Upcoming appointment
        Appointment upcoming = new Appointment(
                doctorEmail,
                patientEmail,
                upcomingDate,
                LocalTime.of(11,0),
                LocalTime.of(11,30),
                "Confirmed",
                "Checkup",
                ""
        );

        // Manually add past appointment to completed table logic (via update or add then complete)
        // Since repo.addAppointment adds to upcoming, we can complete it
        repo.addAppointment(past);
        appointmentService.completeAppointment(past, "all ok");
        
        appointmentService.bookAppointment(upcoming);

        List<Appointment> upcomingList =
                repo.getUpcomingAppointmentsForPatient(patientEmail);
        List<Appointment> completedList =
                repo.getCompletedAppointmentsForPatient(patientEmail);

        assertEquals(1, upcomingList.size());
        assertEquals(1, completedList.size());

        List<Appointment> serviceUpcoming =
                appointmentService.getUpcomingAppointmentsForPatient(patientEmail);

        List<Appointment> servicePast =
                appointmentService.getPastAppointmentsForPatient(patientEmail);

        assertEquals(1, serviceUpcoming.size());
        assertEquals(1, servicePast.size());
    }
}
