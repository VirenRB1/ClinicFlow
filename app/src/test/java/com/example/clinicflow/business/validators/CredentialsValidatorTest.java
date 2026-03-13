package com.example.clinicflow.business.validators;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.clinicflow.business.exceptions.AuthExceptions;

public class CredentialsValidatorTest {

    private CredentialsValidator validator;

    @Before
    public void setUp() {
        validator = new CredentialsValidator();
    }

    @Test
    public void testValidate_ValidCredentials_NoExceptionThrown() throws AuthExceptions.AuthException {
        validator.validate("test@example.com", "password123");
    }

    @Test
    public void testValidate_NullEmail_ThrowsEmptyEmailException() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            validator.validate(null, "password123")
        );
    }

    @Test
    public void testValidate_EmptyEmail_ThrowsEmptyEmailException() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            validator.validate("", "password123")
        );
    }

    @Test
    public void testValidate_WhitespaceEmail_ThrowsEmptyEmailException() {
        assertThrows(AuthExceptions.EmptyEmailException.class, () -> 
            validator.validate("   ", "password123")
        );
    }

    @Test
    public void testValidate_InvalidEmailFormat_ThrowsInvalidEmailException() {
        assertThrows(AuthExceptions.InvalidEmailException.class, () -> 
            validator.validate("invalid-email", "password123")
        );
    }

    @Test
    public void testValidate_EmailWithoutDotCom_ThrowsInvalidEmailException() {
        assertThrows(AuthExceptions.InvalidEmailException.class, () -> 
            validator.validate("test@example.org", "password123")
        );
    }

    @Test
    public void testValidate_NullPassword_ThrowsEmptyPasswordException() {
        assertThrows(AuthExceptions.EmptyPasswordException.class, () -> 
            validator.validate("test@example.com", null)
        );
    }

    @Test
    public void testValidate_EmptyPassword_ThrowsEmptyPasswordException() {
        assertThrows(AuthExceptions.EmptyPasswordException.class, () -> 
            validator.validate("test@example.com", "")
        );
    }
}
