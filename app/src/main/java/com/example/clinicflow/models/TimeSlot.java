//package com.example.clinicflow.models;
//import java.time.LocalDateTime;
//
//public class TimeSlot {
//    private final String  doctorsEmail;
//    private final LocalDateTime startDateTime;
//    private final LocalDateTime endDateTime;
//
//    public TimeSlot(String doctorsEmail, LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        this.doctorsEmail = requireNonBlank(doctorsEmail, "doctorsEmail");
//        this.startDateTime = Objects.requireNonNull(startDateTime, "startDateTime cannot be null");
//        this.endDateTime = Objects.requireNonNull(endDateTime, "endDateTime cannot be null");
//        validateTimeRange();
//    }
//
//    public void validateTimeRange(){ //isAvailable
//        if (!startDateTime.isBefore(endDateTime)){
//            throw new IllegalArgumentException("Start time must be before end time");
//        }
//    }
//
//    private static String requireNonBlank( String info, String fieldMessage){
//        if (info == null || info.isBlank()){
//            throw new IllegalArgumentException(fieldMessage + " cannot be null or blank!!!");
//        }
//        return info;
//    }
//
//    public String getDoctorsEmail() {return doctorsEmail;}
//    public LocalDateTime getStartDateTime() {return startDateTime;}
//    public LocalDateTime getEndDateTime() {return endDateTime;}
//}
