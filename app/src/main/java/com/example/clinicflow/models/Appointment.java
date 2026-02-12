//package com.example.clinicflow.models;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//
////im not sure of what parameters we need for appointment yet so i made some assumptions based on common ones.
//
//
//public class Appointment {
//    private final String appointmentId;
//    private final String patientEmail;
//    private final String physicianUserId;
//    private LocalDateTime startDateTime;
//    private LocalDateTime endDateTime;
//    // chat GPT asked for duration but i dont know if. we need that since we have start and end time
//    // and those are gonna be estimates
//    private AppointmentStatus status;
//    private final String createdByUserId; // this could be created by paitent
//    //  or staff but i believe it shoudl be by the staff cuz the staff documents for the patient
//
//    private String notes;         // optiona not sure if we need this yet
//    private String cancelReason; // optional
//
//    public Appointment(
//            String appointmentId,
//            String patientEmail,
//            String physicianUserId,
//            LocalDateTime startDateTime,
//            LocalDateTime endDateTime,
//            AppointmentStatus status,
//            //String createdByUserId,
//            String notes
//    ) {
//        this.appointmentId = requireNonBlank(appointmentId, "appointmentId");
//        this.patientEmail = requireNonBlank(patientEmail, "patientEmail");
//        this.physicianUserId = requireNonBlank(physicianUserId, "physicianUserId");
//        //this.createdByUserId = requireNonBlank(createdByUserId, "createdByUserId");
//        this.startDateTime = Objects.requireNonNull(startDateTime, "startDateTime cannot be null");
//        this.endDateTime = Objects.requireNonNull(endDateTime, "endDateTime cannot be null");
//        validateTimeRange(this.startDateTime, this.endDateTime);
//
//        this.status = Objects.requireNonNull(status, "status cannot be null");
//        this.notes = notes;
//    }
//
//    public String getAppointmentId() { return appointmentId; }
//    public String getPatientEmail() { return patientEmail; }
//    public String getPhysicianUserId() { return physicianUserId; }
//    public LocalDateTime getStartDateTime() { return startDateTime; }
//    public LocalDateTime getEndDateTime() { return endDateTime; }
//    public AppointmentStatus getStatus() { return status; }
//    //public String getCreatedByUserId() { return createdByUserId; }
//    public String getNotes() { return notes; } // idk if we need a setter for notes
//    public String getCancelReason() { return cancelReason; }
//
//    public void cancle ( String reason) {
//        if (this.status == AppointmentStatus.CANCELED){
//            throw new IllegalStateException("Appointment is already canceled");
//        }
//        this.cancelReason = reason;
//    }
//
//    public boolean overlappingAppointment(Appointment other){
//        Objects.requireNonNull(other, "Other appointment cannot be null");
//        return this.startDateTime.isBefore(other.endDateTime) &&
//               other.startDateTime.isBefore(this.endDateTime);
//    }
//
//    public void canclelAppointment(String reason) {
//        if (this.status == AppointmentStatus.CANCELED) {
//            throw new IllegalStateException("Appointment is already canceled");
//            return;
//        }
//        this.status = AppointmentStatus.CANCELED;
//        this.cancelReason = reason;
//    }
//
//    public void rescheduleAppointment(LocalDateTime newAppointmentStart, LocalDateTime newAppointmentEnd) {
//        validateTimeRange(newAppointmentStart, newAppointmentEnd);
//        this.startDateTime = newAppointmentStart;
//        this.endDateTime = newAppointmentEnd;
//        this.status = AppointmentStatus.RESCHEDULED;
//    }
//
//    private static void validateTimeRange(LocalDateTime start, LocalDateTime end){
//        if (start.isAfter(end) ){
//            throw new IllegalArgumentException("Start time must be before end time");
//        }
//    }
//}
