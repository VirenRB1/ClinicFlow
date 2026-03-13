package com.example.clinicflow.models;

import java.time.LocalTime;

public class DoctorAvailability {
    private int id;
    private String doctorEmail;
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public DoctorAvailability(int id,
                              String doctorEmail,
                              int dayOfWeek,
                              LocalTime startTime,
                              LocalTime endTime) {
        this.id = id;
        this.doctorEmail = doctorEmail;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorAvailability(String doctorEmail,
                              int dayOfWeek,
                              LocalTime startTime,
                              LocalTime endTime) {
        this.doctorEmail = doctorEmail;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}