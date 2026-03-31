package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.DoctorAvailabilityPersistence;
import com.example.clinicflow.business.validators.AvailabilityValidator;

import java.time.LocalTime;
import java.util.List;

/**
 * Service class for managing doctor work shifts and availability windows.
 */
public class DocAvailabilityService {
    private final DoctorAvailabilityPersistence availabilityPersistence;
    private final AvailabilityValidator validator;

    /**
     * Constructs the service with required dependencies.
     *
     * @param availabilityPersistence The persistence for availability data.
     */
    public DocAvailabilityService(DoctorAvailabilityPersistence availabilityPersistence, AvailabilityValidator validator) {
        this.availabilityPersistence = availabilityPersistence;
        this.validator = validator;
    }

    /**
     * Adds a new availability shift for a doctor after validation and overlap
     * checks.
     * 
     * @param availability The new shift to add.
     * @throws ValidationExceptions.ValidationException If the shift is invalid or
     *                                                  overlaps with existing ones.
     */
    public void addDoctorAvailability(DoctorAvailability availability) throws ValidationExceptions.ValidationException {
        validator.validateAvailability(availability);

        List<DoctorAvailability> existingAvailabilities = availabilityPersistence.getDoctorAvailability(
                availability.getDoctorEmail(),
                availability.getDayOfWeek());

        for (DoctorAvailability existing : existingAvailabilities) {
            if (isOverlapping(availability, existing)) {
                throw new ValidationExceptions.AvailabilityOverlapException();
            }
        }

        availabilityPersistence.addDoctorAvailability(availability);
    }

    /**
     * Checks if two availability slots for the same day overlap.
     * 
     * @param newAvail      The new requested availability.
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

    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        return availabilityPersistence.getDoctorAvailability(doctorEmail, dayOfWeek);
    }
}
