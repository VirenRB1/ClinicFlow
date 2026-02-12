package com.example.clinicflow.business;


import static org.junit.Assert.*;

import com.example.clinicflow.business.AuthService;
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

    // unexist email
    @Test
    public void no_email() {
        System.out.println("Test: login with non-existing email → expect null");

        Users u = auth.authenticate("noone@clinicdoc.com", "pass1");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // unmatch password
    @Test
    public void bad_pw() {
        System.out.println("Test: login with wrong password → expect null");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "wrong");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // not entering email/password
    @Test
    public void empty_both() {
        System.out.println("Test: login with empty email and password → expect null");

        Users u = auth.authenticate("", "");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // not entering email
    @Test
    public void empty_email() {
        System.out.println("Test: login with empty email → expect null");

        Users u = auth.authenticate("", "pass1");
        assertNull(u);

        System.out.println("Result: PASS (returned null as expected)");
    }

    // not entering password
    @Test
    public void empty_pw() {
        System.out.println("Test: login with empty password → expect null");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "");
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

        System.out.println("Result: PASS (email local part is case-insensitive)");
    }


    // upper/lower case password
    @Test
    public void pw_case() {
        System.out.println("Test: login with different password case → expect fail");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "PASS1");
        assertNull(u);

        System.out.println("Result: PASS (password is case-sensitive)");
    }

    // successfully login as doctor
    @Test
    public void ok_doctor() {
        System.out.println("Test: login as doctor → expect Doctor object");

        Users u = auth.authenticate("johndoe@clinicdoc.com", "pass1");
        assertNotNull(u);
        assertTrue(u instanceof Doctor);

        System.out.println("Result: PASS (Doctor authenticated)");
    }

    // successfully login as staff
    @Test
    public void ok_staff() {
        System.out.println("Test: login as staff → expect Staff object");

        Users u = auth.authenticate("evemiller@clinicstaff.com", "pass7");
        assertNotNull(u);
        assertTrue(u instanceof Staff);

        System.out.println("Result: PASS (Staff authenticated)");
    }

    // successfully login as patient
    @Test
    public void ok_patient() {
        System.out.println("Test: login as patient → expect Patient object");

        Users u = auth.authenticate("alicebrown@gmail.com", "pass4");
        assertNotNull(u);
        assertTrue(u instanceof Patient);

        System.out.println("Result: PASS (Patient authenticated)");
    }
}

