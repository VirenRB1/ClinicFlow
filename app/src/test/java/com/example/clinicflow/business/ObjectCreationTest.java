package com.example.clinicflow.business;

import static org.junit.Assert.*;

import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class ObjectCreationTest {

    private ObjectCreation objectCreation;

    @Before
    public void setup() {
        UserRepository repo = new FakeUserRepository();
        objectCreation = new ObjectCreation(repo);
    }

    // Valid patient signup
    @Test
    public void testAddPatientSuccess() throws ValidationExceptions.ValidationException {

        boolean result = objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "najma@gmail.com",
                "password",
                "Female",
                LocalDate.of(2002,6,24),
                "124576877",
                "1234553346"
        );

        assertTrue(result);
    }

    // Empty field
    @Test
    public void testEmptyFirstName() {

        try {
            objectCreation.addPatientToDatabase(
                    "",
                    "Mohamed",
                    "amy@gmail.com",
                    "password",
                    "Female",
                    LocalDate.of(2002,6,24),
                    "123456789",
                    "1434553343"
            );

            fail("Expected EmptyFieldException");

        } catch (ValidationExceptions.EmptyFieldException e) {
            assertEquals("First name cannot be empty.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Invalid email
    @Test
    public void testInvalidEmail() {

        try {
            objectCreation.addPatientToDatabase(
                    "Liam",
                    "Walter",
                    "liam.com",
                    "password",
                    "Male",
                    LocalDate.of(2002,6,24),
                    "123456789",
                    "1234553343"
            );

            fail("Expected InvalidEmailException");

        } catch (ValidationExceptions.InvalidEmailException e) {
            assertEquals("Invalid email format.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Duplicate email
    @Test
    public void testDuplicateEmail() {

        try {
            objectCreation.addPatientToDatabase(
                    "Alice",
                    "Brown",
                    "alicebrown@gmail.com",
                    "pass4",
                    "Female",
                    LocalDate.of(2000,1,1),
                    "123456",
                    "5551234"
            );

            fail("Expected DuplicateEmailException");

        } catch (ValidationExceptions.DuplicateEmailException e) {
            assertEquals("Email already exists.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Invalid gender
    @Test
    public void testInvalidGender() {

        try {
            objectCreation.addPatientToDatabase(
                    "Sandra",
                    "Ohm",
                    "sandray@gmail.com",
                    "password",
                    "Gundam",
                    LocalDate.of(2002,6,24),
                    "123456789",
                    "1234553343"
            );

            fail("Expected InvalidGenderException");

        } catch (ValidationExceptions.InvalidGenderException e) {
            assertEquals("Gender must be Male, Female, or Other.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Invalid phone number
    @Test
    public void testInvalidPhoneNumber() {

        try {
            objectCreation.addPatientToDatabase(
                    "David",
                    "Wan",
                    "wan@gmail.com",
                    "password",
                    "Male",
                    LocalDate.of(2002,6,24),
                    "123456789",
                    "123"
            );

            fail("Expected InvalidPhoneNumberException");

        } catch (ValidationExceptions.InvalidPhoneNumberException e) {
            assertEquals("Phone number must be 10 digits.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Invalid health card
    @Test
    public void testInvalidHealthCard() {

        try {
            objectCreation.addPatientToDatabase(
                    "Heather",
                    "Canda",
                    "canda@gmail.com",
                    "password",
                    "Female",
                    LocalDate.of(2002,7,24),
                    "1245",
                    "1234553343"
            );

            fail("Expected InvalidHealthCardException");

        } catch (ValidationExceptions.InvalidHealthCardException e) {
            assertEquals("Health card number must be 9 or 10 digits.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

    // Future DOB
    @Test
    public void testFutureDOB() {

        try {
            objectCreation.addPatientToDatabase(
                    "Future",
                    "Kid",
                    "futurekid@gmail.com",
                    "password",
                    "Male",
                    LocalDate.now().plusDays(1),
                    "123456789",
                    "1234553343"
            );

            fail("Expected InvalidDateOfBirthException");

        } catch (ValidationExceptions.InvalidDateOfBirthException e) {
            assertEquals("Date of birth cannot be in the future.", e.getMessage());
        } catch (ValidationExceptions.ValidationException e) {
            fail("Wrong exception type");
        }
    }

}
