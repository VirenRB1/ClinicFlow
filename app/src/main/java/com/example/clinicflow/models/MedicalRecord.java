package com.example.clinicflow.models;

import java.io.Serializable;
import java.util.Date;
//Medical record
//Name of doctor how wrote the record, and patient name whom it belong
//Purpose of the record, date created and doctor note
public class MedicalRecord implements Serializable {
    private String patientName;
    private String doctorName;

    private String purpose;

    private String doctorNote;

    private Date date;

    public MedicalRecord(String patientName, String doctorName, String purpose, String doctorNote, Date date) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.purpose = purpose;
        this.doctorNote = doctorNote;
        this.date = date;
    }
// Get method
    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getDoctorNote() {
        return doctorNote;
    }

    public Date getDate() {
        return date;
    }

}
