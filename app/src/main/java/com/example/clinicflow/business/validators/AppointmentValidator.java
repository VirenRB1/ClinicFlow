package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.TimeSlot;

import java.time.LocalDate;

public class AppointmentValidator {
    public void validateDoctorAndDate(Doctor doctor, LocalDate date) throws ValidationExceptions.ValidationException {
        if (doctor == null) {
            throw new ValidationExceptions.MissingDoctorException();
        }

        if (date == null) {
            throw new ValidationExceptions.MissingAppointmentDateException();
        }

        if (date.isBefore(LocalDate.now())) {
            throw new ValidationExceptions.InvalidAppointmentDateException();
        }
    }

    public void validateAppointmentConfirmation(String patientEmail, String doctorEmail, LocalDate date, TimeSlot slot, String purpose) throws ValidationExceptions.ValidationException {
        if (patientEmail == null || patientEmail.trim().isEmpty()) {
            throw new ValidationExceptions.MissingPatientEmailException();
        }

        if (doctorEmail == null || doctorEmail.trim().isEmpty()) {
            throw new ValidationExceptions.MissingDoctorEmailException();
        }

        if (date == null) {
            throw new ValidationExceptions.MissingAppointmentDateException();
        }

        if (slot == null) {
            throw new ValidationExceptions.MissingTimeSlotException();
        }

        if (purpose == null || purpose.trim().isEmpty()) {
            throw new ValidationExceptions.MissingPurposeException();
        }
    }

}
