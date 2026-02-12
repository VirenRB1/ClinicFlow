// package com.example.clinicflow.models;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Objects;
//
//
// public class AppointmentService {
// public boolean hasConflictingAppointment(
// String doctorsEmail,
// LocalDateTime startDateTime,
// LocalDateTime endDateTime,
// List<Appointment> existing) {
// Objects.requireNonNull(existing, "existing cannot be null");
//
// // If inputs are invalid, treat as conflict (safer than allowing bad
// bookings)
// if (doctorsEmail == null)
// return true;
// if (startDateTime == null || endDateTime == null)
// return true;
// if (!startDateTime.isBefore(endDateTime))
// return true;
//
// for (Appointment a : existing) {
// if (doctorsEmail.equals(a.getDoctorsEmail())
// && a.getStatus() != AppointmentStatus.CANCELLED) {
//
// // Overlap: [start, end) overlaps [aStart, aEnd)
// boolean overlaps = startDateTime.isBefore(a.getEndDateTime()) &&
// a.getStartDateTime().isBefore(endDateTime);
//
// if (overlaps) {
// return true;
// }
// }
// }
//
// return false;
// }
//
// public Appointment bookAppointment(
// String appointmentID,
// String patientEmail,
// String doctorsEmail,
// LocalDateTime startDateTime,
// LocalDateTime endDateTime,
// String notes,
// List<Appointment> existingAppointments) {
// if (appointmentID == null || appointmentID.trim().isEmpty()) {
// throw new IllegalArgumentException("appointmentID cannot be blank");
// }
// if (patientEmail == null || patientEmail.trim().isEmpty()) {
// throw new IllegalArgumentException("patientEmail cannot be blank");
// }
//
// if (hasConflictingAppointment(doctorsEmail, startDateTime, endDateTime,
// existingAppointments)) {
// throw new IllegalStateException("The doctor is not available during the
// requested time slot.");
// }
//
// return new Appointment(
// appointmentID,
// patientEmail,
// doctorsEmail,
// startDateTime,
// endDateTime,
// AppointmentStatus.REQUESTED,
// notes);
// }
//
// public List<TimeSlot> filterAppointmentTimeSlots(List<TimeSlot> timeSlots,
// List<Appointment> existingAppointments) {
// List<TimeSlot> availableTimeSlots = new ArrayList<>();
//
// for (TimeSlot slot : timeSlots) {
// boolean theresConflict = false;
//
// for (Appointment appointment : existingAppointments) {
// boolean sameDoctor =
// slot.getDoctorsEmail().equals(appointment.getDoctorsEmail());
//
// boolean appointmentActive = appointment.getStatus() !=
// AppointmentStatus.CANCELLED;
//
// if (sameDoctor && appointmentActive) {boolean overlaps =
// slot.getStartDateTime().isBefore(appointment.getEndDateTime()) &&
// appointment.getStartDateTime().isBefore(slot.getEndDateTime());
//
// if (overlaps) {
// theresConflict = true;
// break;
// }
// }
// }
// boolean slotValid = slot.getStartDateTime().isBefore(slot.getEndDateTime());
// if (!theresConflict && slotValid) {
// availableTimeSlots.add(slot);
// }
// }
//
// return availableTimeSlots;}
//
// }
