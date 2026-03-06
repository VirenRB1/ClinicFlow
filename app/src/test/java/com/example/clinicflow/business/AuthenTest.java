package com.example.clinicflow.business;

import static org.junit.Assert.*;

import com.example.clinicflow.business.auth.AuthExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import org.junit.Before;
import org.junit.Test;

public class AuthenTest {

    private AuthService auth;

    @Before
    public void setup() {
        UserRepository repo = new FakeUserRepository();
        auth = new AuthService(repo);
    }

    // non-existing email
    @Test
    public void no_email() {
        System.out.println("Test: login with non-existing email → expect null");

        Users u = auth.authenticate("noone@clinicdoc.com", "pass1");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // wrong password
    @Test
    public void bad_pw() {
        System.out.println("Test: login with wrong password → expect null");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "wrong");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // empty email and password
    @Test
    public void empty_both() {
        System.out.println("Test: login with empty email and password → expect null");

        Users u = auth.authenticate("", "");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // empty email
    @Test
    public void empty_email() {
        System.out.println("Test: login with empty email → expect null");

        Users u = auth.authenticate("", "pass1");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // empty password
    @Test
    public void empty_pw() {
        System.out.println("Test: login with empty password → expect null");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // invalid email format
    @Test
    public void invalid_email() {
        System.out.println("Test: login with invalid email format → expect null");

        Users u = auth.authenticate("johndoe@", "pass1");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // upper/lower case email
    @Test
    public void email_case() {
        System.out.println("Test: login with different email case → expect success");

        Users u = auth.authenticate("JohnDoe@clinicdoc.com", "pass1");
        assertNotNull(u);
        assertTrue(u instanceof Doctor);

        System.out.println("Result: PASS (email is case-insensitive)");
    }

    // upper/lower case password
    @Test
    public void pw_case() {
        System.out.println("Test: login with different password case → expect fail");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "PASS1");
        assertNull(u);

        System.out.println("Result: PASS (password is case-sensitive)");
    }

    // successful login as doctor
    @Test
    public void ok_doctor() {
        System.out.println("Test: login as doctor → expect Doctor object");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "pass1");
        assertNotNull(u);
        assertTrue(u instanceof Doctor);

        System.out.println("Result: PASS (Doctor authenticated)");
    }

    // successful login as staff
    @Test
    public void ok_staff() {
        System.out.println("Test: login as staff → expect Staff object");

        Users u = auth.authenticate("evemiller@clinicstaff.com", "pass7");
        assertNotNull(u);
        assertTrue(u instanceof Staff);

        System.out.println("Result: PASS (Staff authenticated)");
    }

    // successful login as patient
    @Test
    public void ok_patient() {
        System.out.println("Test: login as patient → expect Patient object");

        Users u = auth.authenticate("alicebrown@gmail.com", "pass4");
        assertNotNull(u);
        assertTrue(u instanceof Patient);

        System.out.println("Result: PASS (Patient authenticated)");
    }

    // exception: non-existing email
    @Test
    public void no_email_throw() {
        System.out.println("Test: login with non-existing email → expect UserNotFoundException");

        try {
            auth.authenticateOrThrow("noone@clinicdoc.com", "pass1");
            fail("Expected UserNotFoundException, but no exception was thrown");
        } catch (AuthExceptions.UserNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            System.out.println("Result: PASS (UserNotFoundException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected UserNotFoundException, but got: " + e.getClass().getSimpleName());
        }
    }

    // exception: wrong password
    @Test
    public void bad_pw_throw() {
        System.out.println("Test: login with wrong password → expect WrongPasswordException");

        try {
            auth.authenticateOrThrow("johndoe@clinicdoc.com", "wrong");
            fail("Expected WrongPasswordException, but no exception was thrown");
        } catch (AuthExceptions.WrongPasswordException e) {
            assertEquals("Incorrect password.", e.getMessage());
            System.out.println("Result: PASS (WrongPasswordException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected WrongPasswordException, but got: " + e.getClass().getSimpleName());
        }
    }

    // exception: empty email and password
    @Test
    public void empty_both_throw() {
        System.out.println("Test: login with empty email and password → expect EmptyEmailException");

        try {
            auth.authenticateOrThrow("", "");
            fail("Expected EmptyEmailException, but no exception was thrown");
        } catch (AuthExceptions.EmptyEmailException e) {
            assertEquals("No email entered.", e.getMessage());
            System.out.println("Result: PASS (EmptyEmailException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected EmptyEmailException, but got: " + e.getClass().getSimpleName());
        }
    }

    // exception: empty email
    @Test
    public void empty_email_throw() {
        System.out.println("Test: login with empty email → expect EmptyEmailException");

        try {
            auth.authenticateOrThrow("", "pass1");
            fail("Expected EmptyEmailException, but no exception was thrown");
        } catch (AuthExceptions.EmptyEmailException e) {
            assertEquals("No email entered.", e.getMessage());
            System.out.println("Result: PASS (EmptyEmailException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected EmptyEmailException, but got: " + e.getClass().getSimpleName());
        }
    }

    // exception: invalid email format
    @Test
    public void invalid_email_throw() {
        System.out.println("Test: login with invalid email format → expect InvalidEmailException");

        try {
            auth.authenticateOrThrow("johndoe@", "pass1");
            fail("Expected InvalidEmailException, but no exception was thrown");
        } catch (AuthExceptions.InvalidEmailException e) {
            assertEquals("Invalid email. Email must be in the format name@domain.com", e.getMessage());
            System.out.println("Result: PASS (InvalidEmailException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected InvalidEmailException, but got: " + e.getClass().getSimpleName());
        }
    }

    // exception: empty password
    @Test
    public void empty_pw_throw() {
        System.out.println("Test: login with empty password → expect EmptyPasswordException");

        try {
            auth.authenticateOrThrow("johndoe@clinicdoc.com", "");
            fail("Expected EmptyPasswordException, but no exception was thrown");
        } catch (AuthExceptions.EmptyPasswordException e) {
            assertEquals("No password entered.", e.getMessage());
            System.out.println("Result: PASS (EmptyPasswordException + message matched)");
        } catch (AuthExceptions.AuthException e) {
            fail("Expected EmptyPasswordException, but got: " + e.getClass().getSimpleName());
        }
    }
}