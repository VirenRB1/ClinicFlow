package com.example.clinicflow.business.services;

import static org.junit.Assert.*;

import com.example.clinicflow.business.exceptions.AuthExceptions;
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

    @Test
    public void testDefaultConstructor() {
        AuthService authService = new AuthService(new FakeUserRepository());
        assertNotNull(authService);
    }

    // non-existing email
    @Test
    public void no_email() {
        Users u = auth.authenticate("noone@clinicdoc.com", "pass1");
        assertNull(u);
    }

    // wrong password
    @Test
    public void bad_pw() {
        Users u = auth.authenticate("johndoe@clinicdoc.com", "wrong");
        assertNull(u);
    }

    // empty email and password
    @Test
    public void empty_both() {
        Users u = auth.authenticate("", "");
        assertNull(u);
    }

    // null email
    @Test
    public void null_email() {
        Users u = auth.authenticate(null, "pass1");
        assertNull(u);
    }

    // null password
    @Test
    public void null_pw() {
        Users u = auth.authenticate("johndoe@clinicdoc.com", null);
        assertNull(u);
    }

    // empty email
    @Test
    public void empty_email() {
        Users u = auth.authenticate("", "pass1");
        assertNull(u);
    }

    // empty password
    @Test
    public void empty_pw() {
        Users u = auth.authenticate("johndoe@clinicdoc.com", "");
        assertNull(u);
    }

    // invalid email format
    @Test
    public void invalid_email() {
        Users u = auth.authenticate("johndoe@", "pass1");
        assertNull(u);
    }

    // upper/lower case email
    @Test
    public void email_case() {
        Users u = auth.authenticate("JohnDoe@clinicdoc.com", "pass1");
        assertNotNull(u);
        assertTrue(u instanceof Doctor);
    }

    // email with spaces (normalization check)
    @Test
    public void email_with_spaces() {
        Users u = auth.authenticate("  johndoe@clinicdoc.com  ", "pass1");
        assertNotNull(u);
        assertEquals("johndoe@clinicdoc.com", u.getEmail());
    }

    // upper/lower case password
    @Test
    public void pw_case() {
        Users u = auth.authenticate("johndoe@clinicdoc.com", "PASS1");
        assertNull(u);
    }

    // successful login as doctor
    @Test
    public void ok_doctor() {
        Users u = auth.authenticate("johndoe@clinicdoc.com", "pass1");
        assertNotNull(u);
        assertTrue(u instanceof Doctor);
    }

    // successful login as staff
    @Test
    public void ok_staff() {
        Users u = auth.authenticate("evemiller@clinicstaff.com", "pass7");
        assertNotNull(u);
        assertTrue(u instanceof Staff);
    }

    // successful login as patient
    @Test
    public void ok_patient() {
        Users u = auth.authenticate("alicebrown@gmail.com", "pass4");
        assertNotNull(u);
        assertTrue(u instanceof Patient);
    }

    // exception: non-existing email
    @Test
    public void no_email_throw() {
        assertThrows(AuthExceptions.UserNotFoundException.class, () -> 
            auth.authenticateOrThrow("noone@clinicdoc.com", "pass1")
        );
    }

    // exception: wrong password
    @Test
    public void bad_pw_throw() {
        assertThrows(AuthExceptions.WrongPasswordException.class, () -> 
            auth.authenticateOrThrow("johndoe@clinicdoc.com", "wrong")
        );
    }

    // exception: null email
    @Test
    public void null_email_throw() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            auth.authenticateOrThrow(null, "pass1")
        );
    }

    // exception: null password
    @Test
    public void null_pw_throw() {
        assertThrows(AuthExceptions.EmptyPasswordException.class, () -> 
            auth.authenticateOrThrow("johndoe@clinicdoc.com", null)
        );
    }

    // exception: empty email and password
    @Test
    public void empty_both_throw() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            auth.authenticateOrThrow("", "")
        );
    }

    // exception: empty email
    @Test
    public void empty_email_throw() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            auth.authenticateOrThrow("", "pass1")
        );
    }

    // exception: invalid email format
    @Test
    public void invalid_email_throw() {
        assertThrows(AuthExceptions.InvalidEmailException.class, () -> 
            auth.authenticateOrThrow("johndoe@", "pass1")
        );
    }

    // exception: empty password
    @Test
    public void empty_pw_throw() {
        assertThrows(AuthExceptions.EmptyPasswordException.class, () -> 
            auth.authenticateOrThrow("johndoe@clinicdoc.com", "")
        );
    }
}
