package com.example.clinicflow.integration;

import android.content.Context;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.real.AppDbHelper;
import com.example.clinicflow.persistence.real.SqlRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class ObjectServiceSQLRepoIT {

    private UserRepository repo;
    private ObjectCreation object;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        context.deleteDatabase(AppDbHelper.DATABASE_NAME);

        repo = new SqlRepository(context);
        object = new ObjectCreation(repo);
    }

    @Test
    public void addDeletePatient() throws ValidationExceptions.ValidationException {
        int before = repo.getAllPatients().size();

        boolean added = object.addPatientToDatabase(
                "Earth",
                "Elle",
                "earth@gmail.com",
                "ear",
                "ear",
                "Female",
                LocalDate.of(2003, 5, 20),
                "5429420334",
                "3426549885"
        );

        assertTrue(added);

        Users created = repo.getPatientByEmail("earth@gmail.com");
        assertNotNull(created);
        assertEquals("Earth", created.getFirstName());
        assertEquals("Elle", created.getLastName());
        assertEquals(before + 1, repo.getAllPatients().size());

        boolean deleted = object.deleteUser("earth@gmail.com");
        assertTrue(deleted);

        assertNull(repo.getPatientByEmail("earth@gmail.com"));
        assertEquals(before, repo.getAllPatients().size());
    }

    @Test
    public void addDeleteDoctor() throws ValidationExceptions.ValidationException {
        int before = repo.getAllDoctors().size();

        boolean added = object.addDoctorToDatabase(
                "Sam",
                "Mendez",
                "sam@gmail.com",
                "sam22",
                "sam22",
                "Male",
                LocalDate.of(1993, 5, 20),
                Specialization.CARDIOLOGY,
                "9872342332"
        );

        assertTrue(added);

        Users doctor = repo.getUserByEmail("sam@gmail.com");
        assertNotNull(doctor);
        assertEquals("Sam", doctor.getFirstName());
        assertEquals("Mendez", doctor.getLastName());
        assertEquals(Specialization.CARDIOLOGY, ((Doctor) doctor).getSpecialization());
        assertEquals(before + 1, repo.getAllDoctors().size());

        boolean deleted = object.deleteUser("sam@gmail.com");
        assertTrue(deleted);

        assertNull(repo.getUserByEmail("sam@gmail.com"));
        assertEquals(before, repo.getAllDoctors().size());
    }

    @Test
    public void addDeleteStaff() throws ValidationExceptions.ValidationException {
        int before = repo.getAllStaffs().size();

        boolean added = object.addStaffToDatabase(
                "Nune",
                "Anda",
                "Anda@gmail.com",
                "anda",
                "anda",
                "Male",
                LocalDate.of(1999, 11, 20),
                "Receptionist"
        );

        assertTrue(added);

        Users staff = repo.getUserByEmail("Anda@gmail.com");
        assertNotNull(staff);
        assertEquals("Nune", staff.getFirstName());
        assertEquals("Anda", staff.getLastName());
        assertEquals("Receptionist", ((Staff) staff).getPosition());
        assertEquals(before + 1, repo.getAllStaffs().size());

        boolean deleted = object.deleteUser("Anda@gmail.com");
        assertTrue(deleted);

        assertNull(repo.getUserByEmail("Anda@gmail.com"));
        assertEquals(before, repo.getAllStaffs().size());
    }
}