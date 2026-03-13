package com.example.clinicflow.business.auth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class UniversalAuthenticatorTest {

    private UniversalAuthenticator authenticator;
    private UserRepository mockRepo;
    private Users testUser;

    @Before
    public void setUp() {
        authenticator = new UniversalAuthenticator();
        mockRepo = mock(UserRepository.class);
        // Using a concrete implementation instead of mocking the abstract Users class
        testUser = new Patient("John", "Doe", "test@example.com", "password123", "Male", 
                               LocalDate.of(1990, 1, 1), "123456789", "555-5555");
    }

    @Test
    public void testAuthenticate_ValidCredentials_ReturnsUser() throws AuthExceptions.AuthException {
        String email = "test@example.com";
        String password = "password123";

        when(mockRepo.getUserByEmail(email)).thenReturn(testUser);

        Users result = authenticator.authenticate(mockRepo, email, password);

        assertNotNull(result);
        assertEquals(testUser, result);
        verify(mockRepo).getUserByEmail(email);
    }

    @Test
    public void testAuthenticate_UserNotFound_ThrowsUserNotFoundException() {
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        when(mockRepo.getUserByEmail(email)).thenReturn(null);

        assertThrows(AuthExceptions.UserNotFoundException.class, () -> 
            authenticator.authenticate(mockRepo, email, password)
        );
    }

    @Test
    public void testAuthenticate_WrongPassword_ThrowsWrongPasswordException() {
        String email = "test@example.com";
        String wrongPassword = "wrongPassword";

        when(mockRepo.getUserByEmail(email)).thenReturn(testUser);

        assertThrows(AuthExceptions.WrongPasswordException.class, () -> 
            authenticator.authenticate(mockRepo, email, wrongPassword)
        );
    }
}
