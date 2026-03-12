package com.example.clinicflow.integration;

import android.content.Context;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.real.AppDbHelper;
import com.example.clinicflow.persistence.real.SqlRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

//@RunWith(AndroidJUnit4.class)
public class AppointmentRepoIT {
//    private UserRepository repo;
//
//    @Before
//    public void setup() {
//        Context context = ApplicationProvider.getApplicationContext();
//        // Clean DB so test is deterministic
//        context.deleteDatabase(AppDbHelper.DATABASE_NAME);
//        repo = new SqlRepository(context);
//    }
//
//    @Test
//    public void testRepoNotNull() {
//        assertNotNull(repo);
//    }
}
