package com.example.clinicflow.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LookupServiceTest {

    private LookupService lookupService;
    private UserRepository mockUserRepository;

    @Before
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        lookupService = new LookupService(mockUserRepository);
    }

    @Test
    public void testFindUserByEmail_UserExists() {
        String email = "test@example.com";
        Users user = new Patient("John", "Doe", email, "pass", "Male", LocalDate.of(1990, 1, 1), "123", "456");
        when(mockUserRepository.getUserByEmail(email)).thenReturn(user);

        Users result = lookupService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testFindUserByEmail_UserDoesNotExist() {
        String email = "notfound@example.com";
        when(mockUserRepository.getUserByEmail(email)).thenReturn(null);

        Users result = lookupService.findUserByEmail(email);

        assertNull(result);
    }

    @Test
    public void testFindPatientByEmail_IsPatient() {
        String email = "patient@example.com";
        Patient patient = new Patient("Alice", "Brown", email, "pass", "Female", LocalDate.of(2000, 1, 1), "123456", "5551234");
        when(mockUserRepository.getPatientByEmail(email)).thenReturn(patient);

        Patient result = lookupService.findPatientByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testFindPatientByEmail_DoesNotExist() {
        String email = "notfound@example.com";
        when(mockUserRepository.getPatientByEmail(email)).thenReturn(null);

        Patient result = lookupService.findPatientByEmail(email);

        assertNull(result);
    }

    @Test
    public void testFindPatientByEmail_IsNotPatient_ReturnsNull() {
        // The repository contract for getPatientByEmail is to return null if not a patient
        String email = "doctor@example.com";
        when(mockUserRepository.getPatientByEmail(email)).thenReturn(null);

        Patient result = lookupService.findPatientByEmail(email);
        assertNull(result);
    }

    @Test
    public void testFindDoctorByEmail_IsDoctor() {
        String email = "doctor@example.com";
        Doctor doctor = new Doctor("John", "Doe", email, "pass", "Male", LocalDate.of(1990, 1, 1), Specialization.CARDIOLOGY, "LIC123");
        when(mockUserRepository.getDoctorByEmail(email)).thenReturn(doctor);

        Doctor result = lookupService.findDoctorByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testFindDoctorByEmail_DoesNotExist() {
        String email = "notfound@example.com";
        when(mockUserRepository.getDoctorByEmail(email)).thenReturn(null);

        Doctor result = lookupService.findDoctorByEmail(email);

        assertNull(result);
    }

    @Test
    public void testFindDoctorByEmail_IsNotDoctor_ReturnsNull() {
        // The repository contract for getDoctorByEmail is to return null if not a doctor
        String email = "patient@example.com";
        when(mockUserRepository.getDoctorByEmail(email)).thenReturn(null);

        Doctor result = lookupService.findDoctorByEmail(email);
        assertNull(result);
    }

    @Test
    public void testGetFullName_UserExists() {
        String email = "test@example.com";
        Users user = new Patient("John", "Doe", email, "pass", "Male", LocalDate.of(1990, 1, 1), "123", "456");
        when(mockUserRepository.getUserByEmail(email)).thenReturn(user);

        String result = lookupService.getFullName(email);

        assertEquals("John Doe", result);
    }

    @Test
    public void testGetFullName_UserDoesNotExist() {
        String email = "notfound@example.com";
        when(mockUserRepository.getUserByEmail(email)).thenReturn(null);

        String result = lookupService.getFullName(email);

        assertEquals("No User Found", result);
    }

    @Test
    public void testGetDoctors() {
        Doctor doc1 = new Doctor("D1", "L1", "d1@test.com", "p1", "M", LocalDate.of(1980, 1, 1), Specialization.CARDIOLOGY, "L1");
        Doctor doc2 = new Doctor("D2", "L2", "d2@test.com", "p2", "F", LocalDate.of(1985, 1, 1), Specialization.NEUROLOGY, "L2");
        List<Doctor> doctors = Arrays.asList(doc1, doc2);
        when(mockUserRepository.getAllDoctors()).thenReturn(doctors);

        List<Doctor> result = lookupService.getDoctors();

        assertEquals(2, result.size());
        assertEquals(doctors, result);
    }
}
