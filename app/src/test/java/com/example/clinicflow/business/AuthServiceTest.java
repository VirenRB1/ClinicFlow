package com.example.clinicflow.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.clinicflow.business.auth.AuthExceptions;
import com.example.clinicflow.business.auth.CredentialsValidator;
import com.example.clinicflow.business.auth.UserAuthenticator;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class AuthServiceTest {

    private AuthService authService;
    private UserRepository mockRepo;
    private CredentialsValidator realValidator;
    private UserAuthenticator mockAuthenticator;
    private Users testUser;

    @Before
    public void setUp() {
        mockRepo = mock(UserRepository.class);
        // Using real validator and real User implementation to avoid Mockito environment issues
        realValidator = new CredentialsValidator();
        mockAuthenticator = mock(UserAuthenticator.class);
        testUser = new Patient("John", "Doe", "test@example.com", "password123", "Male", 
                               LocalDate.of(1990, 1, 1), "123456789", "555-5555");

        authService = new AuthService(mockRepo, realValidator, mockAuthenticator);
    }

    @Test
    public void testAuthenticate_ValidCredentials_ReturnsUser() throws AuthExceptions.AuthException {
        String email = " test@example.com ";
        String trimmedEmail = "test@example.com";
        String password = "password123";

        when(mockAuthenticator.authenticate(mockRepo, trimmedEmail, password)).thenReturn(testUser);

        Users result = authService.authenticate(email, password);

        assertEquals(testUser, result);
        verify(mockAuthenticator).authenticate(mockRepo, trimmedEmail, password);
    }

    @Test
    public void testAuthenticate_InvalidCredentials_ReturnsNull() {
        String email = "invalid-email";
        String password = "password123";

        // Real validator will throw InvalidEmailException for this input
        Users result = authService.authenticate(email, password);

        assertNull(result);
    }

    @Test
    public void testAuthenticateOrThrow_ValidCredentials_ReturnsUser() throws AuthExceptions.AuthException {
        String email = "test@example.com";
        String password = "password123";

        when(mockAuthenticator.authenticate(mockRepo, email, password)).thenReturn(testUser);

        Users result = authService.authenticateOrThrow(email, password);

        assertEquals(testUser, result);
        verify(mockAuthenticator).authenticate(mockRepo, email, password);
    }

    @Test
    public void testAuthenticateOrThrow_ValidatorFails_ThrowsException() {
        String email = "";
        String password = "password";

        // Real validator throws EmptyEmailException
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            authService.authenticateOrThrow(email, password)
        );
    }

    @Test
    public void testAuthenticateOrThrow_AuthenticatorFails_ThrowsException() throws AuthExceptions.AuthException {
        String email = "test@example.com";
        String password = "wrong";

        // Mocking the authenticator (interface) usually works even when mocking classes fails
        when(mockAuthenticator.authenticate(mockRepo, email, password))
            .thenThrow(new AuthExceptions.WrongPasswordException());

        assertThrows(AuthExceptions.WrongPasswordException.class, () -> 
            authService.authenticateOrThrow(email, password)
        );
    }
}
