// package com.example.clinicflow.models;
//
//
// public class Visit {
// private final String visitorId;
// private final String patientEmail;
// private final String physicianEmail;
// private final LocalDateTime dateTime;
//
// private final String patientVisibleSummary;
// private final String physicianNotes; // restricted
//
// private final List<String> diagnoses;
// private final List<String> prescriptions;
//
// public Visit(
// String visitId,
// String patientUserId,
// String physicianUserId,
// LocalDateTime dateTime,
// String patientVisibleSummary,
// String physicianNotes,
// List<String> diagnoses,
// List<String> prescriptions
// ) {
// this.visitId = requireNonBlank(visitId, "visitId");
// this.patientUserId = requireNonBlank(patientUserId, "patientUserId");
// this.physicianUserId = requireNonBlank(physicianUserId, "physicianUserId");
// this.dateTime = Objects.requireNonNull(dateTime, "dateTime cannot be null");
//
// this.patientVisibleSummary = patientVisibleSummary == null ? "" :
// patientVisibleSummary;// tenary operator
// this.physicianNotes = physicianNotes == null ? "" : physicianNotes;
// //condition ? valueIfTrue : valueIfFalse
//
// this.diagnoses = new ArrayList<>(diagnoses == null ? List.of() : diagnoses);
// this.prescriptions = new ArrayList<>(prescriptions == null ? List.of() :
// prescriptions);
// }
// public String getVisitorId() { return visitorId; }
// public String getPatientEmail() { return patientEmail; }
// public String getPhysicianEmail() { return physicianEmail; }
// public LocalDateTime getDateTime() { return dateTime; }
// public String getPatientVisibleSummary() { return patientVisibleSummary; }
// public String getPhysicianNotes() { return physicianNotes; }
//
// public String getClinicalNotes( Roles role ){
// if (role == Role.PHYSICIAN || role == Role.STAFF){
// return physicianNotes;
// }
// else {
// return patientVisibleSummary;
// }
// }
//
// public List<String> getDiagnoses() {
// return Collections.unmodifiableList(diagnoses);
// }
//
// public List<String> getPrescriptions() {
// return Collections.unmodifiableList(prescriptions);
// }
//
// private static String requireNonBlank( String info, String fieldMessage){
// if (info == null || info.isBlank()){
// throw new IllegalArgumentException(fieldMessage + " cannot be null or
// blank!!!");
// }
// return info;
// }
//
// }
