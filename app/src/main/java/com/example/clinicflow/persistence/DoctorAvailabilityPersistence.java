package com.example.clinicflow.persistence;

import com.example.clinicflow.models.DoctorAvailability;
import java.util.List;

public interface DoctorAvailabilityPersistence {
    void addDoctorAvailability(DoctorAvailability availability);
    void deleteDoctorAvailability(int id);
    List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek);
}
