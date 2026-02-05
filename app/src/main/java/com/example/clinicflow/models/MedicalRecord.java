package com.example.clinicflow.models;

public class MedicalRecord {
    private String patientEmail;
    private final List<Visit> visitors;

    public MedicalRecord( String patientEmail) {
        this.patientEmail = requireNonBlank(patientEmail, "patientEmail");
        this.visitors = new ArrayList<>();
    }

    public String getPatientEmail() { return patientEmail; }
public List<Visit> getVisitHistory() {
        return Collections.unmodifiableList(visitors);
    }

    public void addVisit(Visit visit) {
        if (visit == null) throw new IllegalArgumentException("visit cannot be null");
        if (!patientEmail.equals(visit.getPatientEmail())) {
            throw new IllegalArgumentException("Visit patientEmail does not match MedicalRecord patientEmail");
        }
        visitors.add(visit);
    }

    private static String requireNonBlank( String info, String fieldMessage){
        if (info == null || info.isBlank()){
            throw new IllegalArgumentException(fieldMessage + " cannot be null or blank!!!");
        }
        return info;
    }
      
}