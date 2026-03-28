package com.example.clinicflow.business.creation;

import static org.junit.Assert.*;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
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

    @Test
    public void testAddPatientSuccess() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addPatientToDatabase(
                "Najma", "Mohamed", "najma@gmail.com", "password", "password", "Female",
                LocalDate.of(2002, 6, 24), "124576877", "1234553346"
        );
        assertTrue(result);
    }

    @Test
    public void testEmptyFirstName() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () ->
                objectCreation.addPatientToDatabase(
                        "", "Mohamed", "amy@gmail.com", "password", "password", "Female",
                        LocalDate.of(2002, 6, 24), "123456789", "1434553343"
                )
        );
    }

    @Test
    public void testInvalidEmail() {
        assertThrows(ValidationExceptions.InvalidEmailException.class, () ->
                objectCreation.addPatientToDatabase(
                        "Liam", "Walter", "liam.com", "password", "password", "Male",
                        LocalDate.of(2002, 6, 24), "123456789", "1234553343"
                )
        );
    }

    @Test
    public void testDuplicateEmail() {
        assertThrows(ValidationExceptions.DuplicateEmailException.class, () ->
                objectCreation.addPatientToDatabase(
                        "Alice", "Brown", "alicebrown@gmail.com", "pass4", "pass4", "Female",
                        LocalDate.of(2000, 1, 1), "123456", "5551234"
                )
        );
    }

    @Test
    public void testInvalidGender() {
        assertThrows(ValidationExceptions.InvalidGenderException.class, () ->
                objectCreation.addPatientToDatabase(
                        "Sandra", "Ohm", "sandray@gmail.com", "password", "password", "Gundam",
                        LocalDate.of(2002, 6, 24), "123456789", "1234553343"
                )
        );
    }

    @Test
    public void testInvalidPhoneNumber() {
        assertThrows(ValidationExceptions.InvalidPhoneNumberException.class, () ->
                objectCreation.addPatientToDatabase(
                        "David", "Wan", "wan@gmail.com", "password", "password", "Male",
                        LocalDate.of(2002, 6, 24), "123456789", "123"
                )
        );
    }

    @Test
    public void testInvalidHealthCard() {
        assertThrows(ValidationExceptions.InvalidHealthCardException.class, () ->
                objectCreation.addPatientToDatabase(
                        "Heather", "Canda", "canda@gmail.com", "password", "password", "Female",
                        LocalDate.of(2002, 7, 24), "1245", "1234553343"
                )
        );
    }

    @Test
    public void testFutureDOB() {
        assertThrows(ValidationExceptions.InvalidDateOfBirthException.class, () ->
                objectCreation.addPatientToDatabase(
                        "Future", "Kid", "futurekid@gmail.com", "password", "password", "Male",
                        LocalDate.now().plusDays(1), "123456789", "1234553343"
                )
        );
    }

    @Test
    public void testAddDoctorSuccess() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addDoctorToDatabase(
                "John", "Doe", "johndoe2@test.com", "password", "password", "Male",
                LocalDate.of(1980, 1, 1), Specialization.CARDIOLOGY, "LIC12345"
        );
        assertTrue(result);
    }

    @Test
    public void testAddStaffSuccess() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addStaffToDatabase(
                "Eve", "Staff", "evestaff@test.com", "password", "password", "Female",
                LocalDate.of(1995, 5, 5), "Receptionist"
        );
        assertTrue(result);
    }

    @Test
    public void testDeleteUserSuccess() throws ValidationExceptions.ValidationException {
        String email = "delete_me@test.com";
        objectCreation.addPatientToDatabase(
                "Delete", "Me", email, "pass", "pass", "Other",
                LocalDate.of(2000, 1, 1), "123456789", "1234567890"
        );

        boolean result = objectCreation.deleteUser(email);
        assertTrue(result);
    }

    @Test
    public void testDeleteUserNotFound() {
        boolean result = objectCreation.deleteUser("nonexistent@test.com");
        assertFalse(result);
    }

    @Test
    public void testAddUserToDatabase_Patient() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addUserToDatabase(
                UserRole.PATIENT, "Pat", "Ient", "patient1@test.com", "pass", "pass", "Male",
                LocalDate.of(1990, 1, 1), "123456789", "1234567890", null, null, null
        );
        assertTrue(result);
    }

    @Test
    public void testAddUserToDatabase_Doctor() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addUserToDatabase(
                UserRole.DOCTOR, "Doc", "Tor", "doctor1@test.com", "pass", "pass", "Female",
                LocalDate.of(1985, 1, 1), null, null, "CARDIOLOGY", "LIC123", null
        );
        assertTrue(result);
    }

    @Test
    public void testAddUserToDatabase_Staff() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addUserToDatabase(
                UserRole.STAFF, "Staf", "Fy", "staff1@test.com", "pass", "pass", "Other",
                LocalDate.of(1995, 1, 1), null, null, null, null, "Manager"
        );
        assertTrue(result);
    }

    @Test
    public void testAddUserToDatabase_NullRole() throws ValidationExceptions.ValidationException {
        boolean result = objectCreation.addUserToDatabase(
                null, "Staf", "Fy", "staff1@test.com", "pass", "pass", "Other",
                LocalDate.of(1995, 1, 1), null, null, null, null, "Manager"
        );
        assertFalse(result);
    }

    @Test
    public void testParseSpecialization_NullOrEmpty() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            objectCreation.addUserToDatabase(
                    UserRole.DOCTOR, "Doc", "Tor", "doctor2@test.com", "pass", "pass", "Female",
                    LocalDate.of(1985, 1, 1), null, null, "", "LIC123", null
            );
        });
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            objectCreation.addUserToDatabase(
                    UserRole.DOCTOR, "Doc", "Tor", "doctor2@test.com", "pass", "pass", "Female",
                    LocalDate.of(1985, 1, 1), null, null, "   ", "LIC123", null
            );
        });
    }

    @Test
    public void testParseSpecialization_Invalid() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            objectCreation.addUserToDatabase(
                    UserRole.DOCTOR, "Doc", "Tor", "doctor3@test.com", "pass", "pass", "Female",
                    LocalDate.of(1985, 1, 1), null, null, "INVALID_SPEC", "LIC123", null
            );
        });
    }
}
