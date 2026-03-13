package com.example.clinicflow.integration;

import android.content.Context;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.DocAvailabilityService;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.real.AppDbHelper;
import com.example.clinicflow.persistence.real.SqlRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AvailabitlityRepoIT {
    private UserRepository repo;
    private DocAvailabilityService service;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        // Clean DB so test is deterministic
        context.deleteDatabase(AppDbHelper.DATABASE_NAME);

        repo = new SqlRepository(context);
        service= new DocAvailabilityService(repo);
    }

    @Test
    public void addAvailability() throws ValidationExceptions.ValidationException {
        DoctorAvailability availability = new DoctorAvailability(
                "doctor1@gmail.com",
                1,
                LocalTime.of(9, 0),
                LocalTime.of(14, 0)
        );

        service.addDoctorAvailability(availability);

        List<DoctorAvailability> availabilities =
                repo.getDoctorAvailability("doctor1@gmail.com", 1);
        assertEquals(1, availabilities.size());
    }



}
