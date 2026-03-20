package com.example.clinicflow.business.validators;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;

import java.time.LocalTime;

public class AvailabilityValidator {
    public void validateAvailability(DoctorAvailability availability) throws ValidationExceptions.ValidationException {
        int dayOfWeek = availability.getDayOfWeek();
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();

        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new ValidationExceptions.ValidationException("Invalid day of the week.");
        }
        if (startTime == null || endTime == null) {
            throw new ValidationExceptions.EmptyFieldException("Start/End Time");
        }

        if (!startTime.isBefore(endTime)) {
            throw new ValidationExceptions.InvalidStartAndEndTimeException();
        }

        // Requirement: Start and end times must be on the hour (XX:00)
        if (startTime.getMinute() != 0 || endTime.getMinute() != 0) {
            throw new ValidationExceptions.ValidationException(
                    "Availability times must be at the top of the hour (e.g., 09:00).");
        }

    }
}
