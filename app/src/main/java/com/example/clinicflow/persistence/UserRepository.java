package com.example.clinicflow.persistence;

//To remove code smell. Appointment methods and User methods mixed together was kind of wierd
public interface UserRepository extends UserPersistence, AppointmentPersistence {
}
