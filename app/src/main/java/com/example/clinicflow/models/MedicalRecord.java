package com.example.clinicflow.models;

import java.io.Serializable;
import java.util.Date;
//Medical record
//Name of doctor how wrote the record, and patient name whom it belong
//Purpose of the record, date created and doctor note
public class MedicalRecord implements Serializable {
    int recordId;
    private String patientName;
    private String doctorName;
    private String patientEmail;
    private String purpose;

    private String doctorNote;

    private Date date;

    public MedicalRecord(int recordId, String patientName, String doctorName, String patientEmail, String purpose, String doctorNote, Date date) {
        this.recordId = recordId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.patientEmail = patientEmail;
        this.purpose = purpose;
        this.doctorNote = doctorNote;
        this.date = date;
    }
// Get method
    public int getRecordId() { return recordId; }
    public String getPatientName() {
        return patientName;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public String getEmail() {
        return patientEmail;
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
