package com.example.clinicflow.integration;

import android.content.Context;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Specialization;
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

        // Clean DB so test is deterministic
        context.deleteDatabase(AppDbHelper.DATABASE_NAME);

        repo = new SqlRepository(context);
        object = new ObjectCreation(repo);
    }

    @Test
    public void addDeletePatient() throws ValidationExceptions.ValidationException {
        object.addPatientToDatabase(
            "Earth",
            "Elle",
            "earth@gmail.com",
            "ear",
            "Female",
            LocalDate.of(2003,5,20),
            "5429420334",
            "3426549885"
        );

        Users created = repo.getPatientByEmail("earth@gmail.com");
        assertNotNull(created);
        assertEquals("Earth", created.getFirstName());
        assertEquals("Elle", created.getLastName());

        //Delete
        boolean deleted = object.deleteUser("earth@gmail.com");
        assertTrue(deleted);

        assertEquals(0, repo.getAllPatients().size());
        assertNull(repo.getPatientByEmail("earth@gmail.com"));

    }

    @Test
    public void addDeleteDoctor() throws ValidationExceptions.ValidationException {
        object.addDoctorToDatabase(
                "Sam",
                "Mendez",
                "sam@gmail.com",
                "sam22",
                "Male",
                LocalDate.of(1993,5,20),
                Specialization.CARDIOLOGY,
                "9872342332"
        );
        Users doctor = repo.getUserByEmail("sam@gmail.com");
        assertNotNull(doctor);
        assertEquals("Sam", doctor.getFirstName());
        assertEquals("Mendez", doctor.getLastName());
        assertEquals(Specialization.CARDIOLOGY, ((Doctor) doctor).getSpecialization());


        //Delete
        boolean deleted = object.deleteUser("sam@gmail.com");
        assertTrue(deleted);

        assertEquals(0, repo.getAllDoctors().size());
        assertNull(repo.getUserByEmail("sam@gmail.com"));

    }

    @Test
    public void addDeleteStaff() throws ValidationExceptions.ValidationException {
        object.addStaffToDatabase(
                "Nune",
                "Anda",
                "Anda@gmail.com",
                "anda",
                "Male",
                LocalDate.of(1999,11,20),
                "Receptionist"
        );
        Users staff = repo.getUserByEmail("Anda@gmail.com");
        assertNotNull(staff);
        assertEquals("Nune", staff.getFirstName());
        assertEquals("Anda", staff.getLastName());
        assertEquals("Receptionist", ((Staff) staff).getPosition());


        //Delete
        boolean deleted = object.deleteUser("Anda@gmail.com");
        assertTrue(deleted);

        assertEquals(0, repo.getAllStaffs().size());
        assertNull(repo.getUserByEmail("Anda@gmail.com"));

    }

}
