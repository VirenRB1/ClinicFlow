package com.example.clinicflow.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment implements Serializable {
    private int id;
    private String doctorEmail;
    private String patientEmail;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String patientPurpose;
    private String doctorNotes;

    public Appointment(int id,
                       String doctorEmail,
                       String patientEmail,
                       LocalDate appointmentDate,
                       LocalTime startTime,
                       LocalTime endTime,
                       String status,
                       String patientPurpose,
                       String doctorNotes) {
        this.id = id;
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.patientPurpose = patientPurpose;
        this.doctorNotes = doctorNotes;
    }

    public Appointment(String doctorEmail,
                       String patientEmail,
                       LocalDate appointmentDate,
                       LocalTime startTime,
                       LocalTime endTime,
                       String status,
                       String patientPurpose,
                       String doctorNotes) {
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.patientPurpose = patientPurpose;
        this.doctorNotes = doctorNotes;
    }

    public int getId() {
        return id;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientPurpose() {
        return patientPurpose;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPatientPurpose(String patientPurpose) {
        this.patientPurpose = patientPurpose;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}