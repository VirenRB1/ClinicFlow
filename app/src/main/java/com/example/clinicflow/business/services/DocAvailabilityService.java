package com.example.clinicflow.business.services;

import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.business.validators.AvailabilityValidator;

import java.time.LocalTime;
import java.util.List;

public class DocAvailabilityService {
    private final UserRepository userRepository;
    private final AvailabilityValidator validator;

    public DocAvailabilityService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new AvailabilityValidator();
    }

    //Call to add a doctor availability
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

    // Helper method to check if two doctor availabilities overlap
    private boolean isOverlapping(DoctorAvailability newAvail, DoctorAvailability existingAvail) {
        LocalTime newStart = newAvail.getStartTime();
        LocalTime newEnd = newAvail.getEndTime();
        LocalTime existingStart = existingAvail.getStartTime();
        LocalTime existingEnd = existingAvail.getEndTime();

        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
    }
}
