package com.example.clinicflow.business;

import static org.junit.Assert.*;

//import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import org.junit.Before;
import org.junit.Test;

public class ObjectCreationTest  {
    private ObjectCreation objectCreation;

    @Before
    public void setup() {
        UserRepository repo = new FakeUserRepository();
        objectCreation = new ObjectCreation(repo);
    }

    @Test
    public void testAddPatientToDatabase(){
        boolean result = objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "williamy@.com",
                "password",
                "Female",
                23,
                1245,
                1234553343
        );
        assert(result);

    }
    @Test
    public void testNullField(){
        boolean result = objectCreation.addPatientToDatabase(
                null,
                "Mohamed",
                "williamy@.com",
                "password",
                "Female",
                23,
                1245,
                1234553343
        );
        assertFalse(result);
    }

    @Test
    public void testEmptyName(){
        boolean result = objectCreation.addPatientToDatabase(
                 "",
                 "",
                "williamy@.com",
                "password",
                "Female",
                23,
                1245,
                1234553343
        );
        assertFalse(result);
    }
    @Test
    public void testInvalidEmail(){
        boolean result = objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "williamy.com",
                "password",
                "Female",
                23,
                1245,
                1234553343
        );
        assertFalse(result);
    }
    @Test
    public void testEmptyPassword(){
        boolean result = objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "williamy@.com",
                "",
                "Female",
                23,
                1245,
                1234553343
        );
        assertFalse(result);
    }

    @Test
    public void testEmptyGender(){
        boolean result = objectCreation.addPatientToDatabase(
                "Najma",
                "Mohamed",
                "william@.com",
                "password",
                "",
                23,
                1245,
                1234553343
        );
        assertFalse(result);
    }

    @Test
    public void testDuplicates(){
        boolean result = objectCreation.addPatientToDatabase(
                "Alice",
                "Brown",
                "alicebrown@gmail.com",
                "pass4",
                "Female",
                28,
                123456,
                5551234
        );
        assertFalse(result);
    }
}