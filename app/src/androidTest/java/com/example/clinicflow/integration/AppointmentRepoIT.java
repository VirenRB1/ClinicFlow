package com.example.clinicflow.integration;

import android.content.Context;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.AppointmentService;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.TimeSlot;
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
                "No notes"
                );
        appointmentService.bookAppointment(apptmt);

        // Check that the appointment was booked
        List<Appointment> apptDoctor =
                repo.getAppointmentsForDoctorOnDate(doctorEmail, date);
        List<Appointment> apptPatient =
                repo.getAppointmentsForPatient(patientEmail);
        assertEquals(1, apptDoctor.size());
        assertEquals(1, apptPatient.size());
        assertEquals("Confirmed", apptDoctor.get(0).getStatus());

        // Check that slot is removed
        boolean slotRemoved = appointmentService.getAvailableTimeSlots(doctorEmail, date).stream()
                .noneMatch(s -> s.getStartTime().equals(LocalTime.of(11,0)) &&
                        s.getEndTime().equals(LocalTime.of(11,30)));
        assertTrue(slotRemoved);
    }
}
