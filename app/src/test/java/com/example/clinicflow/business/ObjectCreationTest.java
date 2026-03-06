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
                "124576877",
                "1234553346"
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
                            "amy@.com",
                            "password6",
                            "Female",
                            LocalDate.of(2002, 6, 24),
                            "1245",
                            "1434553343"
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
                    "niamy@.com",
                    "password7",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    "1245",
                    "1234553243"
            );
        });
    }

    // email without '@' should be invalid
    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Liam",
                    "Walter",
                    "liam.com",
                    "password5",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    "1245",
                    "1234553343"
            );
        });
    }

    // empty password should not be allowed and throw exception
    @Test
    public void testEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Will",
                    "Sandy",
                    "wilamy@.com",
                    "",
                    "Female",
                    LocalDate.of(2002, 6, 24),
                    "1245",
                    "1234563343"
            );
        });
    }

    // Empty gender should throw exception
    @Test
    public void testEmptyGender() {
        assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Sandra",
                    "Ohm",
                    "san@.com",
                    "password3",
                    "",
                    LocalDate.of(2002, 6, 24),
                    "1245",
                    "1234553343"
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
                    "123456",
                    "5551234"
            );
        });
    }

    @Test
    public void testInvalidPhoneNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "David",
                    "Wan",
                    "wan@.com",
                    "password2",
                    "Male",
                    LocalDate.of(2002, 6, 24),
                    "123456789",
                    "12345533493"
            );
        });
        assertEquals("Phone number must be 10 digits", exception.getMessage());
    }

    @Test
    public void testInvalidHealthCard() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            objectCreation.addPatientToDatabase(
                    "Heather",
                    "Canda",
                    "canda@.com",
                    "password9",
                    "Female",
                    LocalDate.of(2002, 7, 24),
                    "1245",
                    "1234553343"
            );
        });
        assertEquals("Health card number must be 9 or 10 digits", exception.getMessage());
    }
}