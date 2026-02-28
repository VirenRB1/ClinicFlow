package com.example.clinicflow.business;

import static org.junit.Assert.*;

import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class ObjectCreationTest {
    private ObjectCreation objectCreation;

    // set up a fresh repo and service before each test
    @Before
    public void setup() {
        UserRepository repo = new FakeUserRepository();
        objectCreation = new ObjectCreation(repo);
    }

    // normal case: valid patient info should be accepted
    @Test
    public void testAddPatientToDatabase() {

        objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "williamy@.com",
                "password",
                "Female",
                LocalDate.of(2002, 6, 24),
                1245,
                1234553343
        );

    }

    //  Null field should throw exception
    @Test
    public void testNullField() {

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    objectCreation.addPatientToDatabase(
                            null,
                            "Mohamed",
                            "williamy@.com",
                            "password",
                            "Female",
                            LocalDate.of(2002, 6, 24),
                            1245,
                            1234553343
                    );
                });

        assertEquals("Null fields are not allowed", exception.getMessage());
    }

    // empty first/last name should fail
    @Test
    public void testEmptyName() {

        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "",
                    "",
                    "williamy@.com",
                    "password",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    1245,
                    1234553343
            );
        });
    }

    // email without '@' should be invalid
    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Najma",
                    "Mohamed",
                    "williamy.com",
                    "password",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    1245,
                    1234553343
            );
        });
    }

    // empty password should not be allowed and throw exception
    @Test
    public void testEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Najma",
                    "Mohamed",
                    "williamy@.com",
                    "",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    1245,
                    1234553343
            );
        });
    }

    // Empty gender should throw exception
    @Test
    public void testEmptyGender() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Najma",
                    "Mohamed",
                    "william@.com",
                    "password",
                    "",
                    LocalDate.of(2002, 6, 24),
                    1245,
                    1234553343
            );
        });
    }

    //  Duplicate email should throw IllegalStateException
    @Test
    public void testDuplicates() {
        // Alice is already in the database hence throws exceptions
        assertThrows(IllegalStateException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Alice",
                    "Brown",
                    "alicebrown@gmail.com",
                    "pass4",
                    "Female",
                    LocalDate.of(1997, 6, 24),
                    123456,
                    5551234
            );
        });
    }
}