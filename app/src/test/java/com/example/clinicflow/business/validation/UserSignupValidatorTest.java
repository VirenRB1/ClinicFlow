package com.example.clinicflow.business.validation;

import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.persistence.UserRepository;
import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;

public class UserSignupValidatorTest {

    private UserRepository repo;
    private UserSignupValidator validator;

    @Before
    public void setUp() {
        repo = mock(UserRepository.class);
        validator = new UserSignupValidator(repo);
    }

    // -------------------------------------------------
    // Helper values
    // -------------------------------------------------

    private LocalDate validDob() {
        return LocalDate.now().minusYears(25);
    }

    // -------------------------------------------------
    // userValidator tests
    // -------------------------------------------------

    @Test
    public void userValidator_validInput_shouldPass() throws Exception {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        try {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    validDob()
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void userValidator_emptyFirstName_shouldThrowEmptyFieldException() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.userValidator(
                    "   ",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_emptyLastName_shouldThrowEmptyFieldException() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.userValidator(
                    "John",
                    "",
                    "john@example.com",
                    "secret123",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_emptyPassword_shouldThrowEmptyFieldException() {
        assertThrows(ValidationExceptions.ValidationException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "   ",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_nullEmail_shouldThrowEmptyFieldException() {
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    null,
                    "secret123",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_invalidEmail_shouldThrowInvalidEmailException() {
        assertThrows(ValidationExceptions.InvalidEmailException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.org",
                    "secret123",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_duplicateEmail_shouldThrowDuplicateEmailException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(getPatient());
        assertThrows(ValidationExceptions.DuplicateEmailException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_emptyGender_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    " ",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_invalidGender_shouldThrowInvalidGenderException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidGenderException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Unknown",
                    validDob()
            );
        });
    }

    @Test
    public void userValidator_genderFemale_shouldPass() throws Exception {
        when(repo.getPatientByEmail("jane@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.userValidator(
                            "Jane",
                            "Doe",
                            "jane@example.com",
                            "secret123",
                            "Female",
                            validDob()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void userValidator_genderOther_shouldPass() throws Exception {
        when(repo.getPatientByEmail("alex@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.userValidator(
                            "Alex",
                            "Doe",
                            "alex@example.com",
                            "secret123",
                            "Other",
                            validDob()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void userValidator_nullDateOfBirth_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    null
            );
        });
    }

    @Test
    public void userValidator_futureDateOfBirth_shouldThrowInvalidDateOfBirthException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidDateOfBirthException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    LocalDate.now().plusDays(1)
            );
        });
    }

    @Test
    public void userValidator_tooOldDateOfBirth_shouldThrowInvalidDateOfBirthException() {
        when(repo.getPatientByEmail("john@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidDateOfBirthException.class, () -> {
            validator.userValidator(
                    "John",
                    "Doe",
                    "john@example.com",
                    "secret123",
                    "Male",
                    LocalDate.now().minusYears(121)
            );
        });
    }

    // -------------------------------------------------
    // patientValidator tests
    // -------------------------------------------------

    @Test
    public void patientValidator_validInput_shouldPass() throws Exception {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.patientValidator(
                            "Pat",
                            "Smith",
                            "patient@example.com",
                            "secret123",
                            "Male",
                            validDob(),
                            "123456789",
                            "2045551234"
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void patientValidator_emptyPhone_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.patientValidator(
                    "Pat",
                    "Smith",
                    "patient@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    "123456789",
                    " "
            );
        });
    }

    @Test
    public void patientValidator_invalidPhone_shouldThrowInvalidPhoneNumberException() {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidPhoneNumberException.class, () -> {
            validator.patientValidator(
                    "Pat",
                    "Smith",
                    "patient@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    "123456789",
                    "12345"
            );
        });
    }

    @Test
    public void patientValidator_formattedPhone_shouldPass() throws Exception {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.patientValidator(
                            "Pat",
                            "Smith",
                            "patient@example.com",
                            "secret123",
                            "Male",
                            validDob(),
                            "123456789",
                            "(204) 555-1234"
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void patientValidator_emptyHealthCard_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.patientValidator(
                    "Pat",
                    "Smith",
                    "patient@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    "",
                    "2045551234"
            );
        });
    }

    @Test
    public void patientValidator_invalidHealthCard_shouldThrowInvalidHealthCardException() {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidHealthCardException.class, () -> {
            validator.patientValidator(
                    "Pat",
                    "Smith",
                    "patient@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    "12A45",
                    "2045551234"
            );
        });
    }

    @Test
    public void patientValidator_tenDigitHealthCard_shouldPass() throws Exception {
        when(repo.getPatientByEmail("patient@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.patientValidator(
                            "Pat",
                            "Smith",
                            "patient@example.com",
                            "secret123",
                            "Male",
                            validDob(),
                            "1234567890",
                            "2045551234"
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    // -------------------------------------------------
    // doctorValidator tests
    // -------------------------------------------------

    @Test
    public void doctorValidator_validInput_shouldPass() throws Exception {
        when(repo.getPatientByEmail("doctor@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.doctorValidator(
                            "Doc",
                            "Brown",
                            "doctor@example.com",
                            "secret123",
                            "Male",
                            validDob(),
                            Specialization.values()[0],
                            "LIC12345"
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void doctorValidator_nullSpecialization_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("doctor@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.doctorValidator(
                    "Doc",
                    "Brown",
                    "doctor@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    null,
                    "LIC12345"
            );
        });
    }

    @Test
    public void doctorValidator_emptyLicense_shouldThrowEmptyFieldException() {
        when(repo.getPatientByEmail("doctor@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.EmptyFieldException.class, () -> {
            validator.doctorValidator(
                    "Doc",
                    "Brown",
                    "doctor@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    Specialization.values()[0],
                    " "
            );
        });
    }

    @Test
    public void doctorValidator_shortLicense_shouldThrowValidationException() {
        when(repo.getPatientByEmail("doctor@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.ValidationException.class, () -> {
            validator.doctorValidator(
                    "Doc",
                    "Brown",
                    "doctor@example.com",
                    "secret123",
                    "Male",
                    validDob(),
                    Specialization.values()[0],
                    "1234"
            );
        });
    }

    // -------------------------------------------------
    // staffValidator tests
    // -------------------------------------------------

    @Test
    public void staffValidator_validInput_shouldPass() throws Exception {
        when(repo.getPatientByEmail("staff@example.com")).thenReturn(null);
        try {
            Runnable action = () -> {
                try {
                    validator.staffValidator(
                            "Staff",
                            "Member",
                            "staff@example.com",
                            "secret123",
                            "Female",
                            validDob()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            action.run();
        } catch (RuntimeException e) {
            fail("Expected no exception, but got: " + e.getCause().getMessage());
        }
    }

    @Test
    public void staffValidator_invalidUnderlyingUserInput_shouldThrowException() {
        when(repo.getPatientByEmail("staff@example.com")).thenReturn(null);
        assertThrows(ValidationExceptions.InvalidGenderException.class, () -> {
            validator.staffValidator(
                    "Staff",
                    "Member",
                    "staff@example.com",
                    "secret123",
                    "InvalidGender",
                    validDob()
            );
        });
    }
    private static Patient getPatient(){
        return new Patient("Alice","Brown","alicebrown@gmail.com","pass4","Female", LocalDate.of(2000,1,1),"123456","5551234");
    }

}
