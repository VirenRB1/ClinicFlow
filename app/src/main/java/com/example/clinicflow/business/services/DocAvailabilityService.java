package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.business.validators.AvailabilityValidator;

import java.time.LocalTime;
import java.util.List;

/**
 * Service class for managing doctor work shifts and availability windows.
 */
public class DocAvailabilityService {
    private final UserRepository userRepository;
    private final AvailabilityValidator validator;

    /**
     * Constructs the service with required dependencies.
     * @param userRepository The repository for availability data.
     */
    public DocAvailabilityService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new AvailabilityValidator();
    }

    /**
     * Adds a new availability shift for a doctor after validation and overlap checks.
     * @param availability The new shift to add.
     * @throws ValidationExceptions.ValidationException If the shift is invalid or overlaps with existing ones.
     */
    public void addDoctorAvailability(DoctorAvailability availability) throws ValidationExceptions.ValidationException {
        validator.validateAvailability(availability);

        List<DoctorAvailability> existingAvailabilities = userRepository.getDoctorAvailability(
                availability.getDoctorEmail(), 
                availability.getDayOfWeek()
        );

        for (DoctorAvailability existing : existingAvailabilities) {
            if (isOverlapping(availability, existing)) {
                throw new ValidationExceptions.AvailabilityOverlapException();
            }
        }

        userRepository.addDoctorAvailability(availability);
    }

    /**
     * Checks if two availability slots for the same day overlap.
     * @param newAvail The new requested availability.
     * @param existingAvail An already existing availability.
     * @return True if they overlap, false otherwise.
     */
    private boolean isOverlapping(DoctorAvailability newAvail, DoctorAvailability existingAvail) {
        LocalTime newStart = newAvail.getStartTime();
        LocalTime newEnd = newAvail.getEndTime();
        LocalTime existingStart = existingAvail.getStartTime();
        LocalTime existingEnd = existingAvail.getEndTime();

        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
    }
}
