package com.example.clinicflow.business.exceptions;

public class ValidationExceptions {

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class EmptyFieldException extends ValidationException {
        public EmptyFieldException(String fieldName) {
            super(fieldName + " cannot be empty.");
        }
    }

    public static class InvalidEmailException extends ValidationException {
        public InvalidEmailException() {
            super("Invalid email format.");
        }
    }

    public static class DuplicateEmailException extends ValidationException {
        public DuplicateEmailException() {
            super("Email already exists.");
        }
    }

    public static class InvalidGenderException extends ValidationException {
        public InvalidGenderException() {
            super("Gender must be Male, Female, or Other.");
        }
    }

    public static class InvalidDateOfBirthException extends ValidationException {
        public InvalidDateOfBirthException(String message) {
            super(message);
        }
    }

    public static class InvalidPhoneNumberException extends ValidationException {
        public InvalidPhoneNumberException() {
            super("Phone number must be 10 digits.");
        }
    }

    public static class InvalidHealthCardException extends ValidationException {
        public InvalidHealthCardException() {
            super("Health card number must be 9 or 10 digits.");
        }
    }

    public static class InvalidSpecializationException extends ValidationException {
        public InvalidSpecializationException() {
            super("Invalid specialization.");
        }
    }

    public static class InvalidStartAndEndTimeException extends ValidationException {
        public InvalidStartAndEndTimeException() {
            super("Start time must be before end time.");
        }

    }

    public static class AvailabilityOverlapException extends ValidationException {
        public AvailabilityOverlapException() {
            super("The new availability overlaps with an existing one.");
        }
    }

    public static class AppointmentConflictException extends ValidationException {
        public AppointmentConflictException() {
            super("This time slot is no longer available.");
        }
    }

    public static class AppointmentCancellationException extends ValidationException {
        public AppointmentCancellationException() {
            super("This appointment cannot be cancelled.");
        }
    }

    public static class InvalidAppointmentDateException extends ValidationException {
        public InvalidAppointmentDateException() {
            super("Appointment date cannot be in the past.");
        }
    }

    public static class MissingDoctorException extends ValidationException {
        public MissingDoctorException() {
            super("Please select a doctor.");
        }
    }

    public static class MissingAppointmentDateException extends ValidationException {
        public MissingAppointmentDateException() {
            super("Please select a date.");
        }
    }

    public static class MissingTimeSlotException extends ValidationException {
        public MissingTimeSlotException() {
            super("Missing time slot.");
        }
    }

    public static class MissingPatientEmailException extends ValidationException {
        public MissingPatientEmailException() {
            super("Missing patient email.");
        }
    }

    public static class MissingDoctorEmailException extends ValidationException {
        public MissingDoctorEmailException() {
            super("Missing doctor email.");
        }
    }

    public static class MissingPurposeException extends ValidationException {
        public MissingPurposeException() {
            super("Missing purpose.");
        }
    }

}
