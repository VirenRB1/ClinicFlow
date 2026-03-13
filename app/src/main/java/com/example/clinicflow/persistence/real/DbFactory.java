package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.persistence.UserFactory;

import java.util.List;

public class DbFactory {

    public static void populateFakeData(SQLiteDatabase db) {
        populateAdmins(db);
        populateDoctors(db);
        populatePatients(db);
        populateStaffs(db);
        addPastAppointments(db);
    }

    private static void populateAdmins(SQLiteDatabase db) {
        List<Admin> admins = UserFactory.getDefaultAdmins();
        for (Admin admin : admins) {
            ContentValues values = new ContentValues();
            values.put(DbContract.AdminEntry.COLUMN_FIRST_NAME, admin.getFirstName());
            values.put(DbContract.AdminEntry.COLUMN_LAST_NAME, admin.getLastName());
            values.put(DbContract.AdminEntry.COLUMN_EMAIL, admin.getEmail());
            values.put(DbContract.AdminEntry.COLUMN_PASSWORD, admin.getPassword());
            values.put(DbContract.AdminEntry.COLUMN_GENDER, admin.getGender());
            values.put(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH, admin.getDateOfBirth().toString());
            db.insertWithOnConflict(DbContract.AdminEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    private static void populateDoctors(SQLiteDatabase db) {
        List<Doctor> doctors = UserFactory.getDefaultDoctors();
        for (Doctor doctor : doctors) {
            ContentValues values = new ContentValues();
            values.put(DbContract.DoctorEntry.COLUMN_FIRST_NAME, doctor.getFirstName());
            values.put(DbContract.DoctorEntry.COLUMN_LAST_NAME, doctor.getLastName());
            values.put(DbContract.DoctorEntry.COLUMN_EMAIL, doctor.getEmail());
            values.put(DbContract.DoctorEntry.COLUMN_PASSWORD, doctor.getPassword());
            values.put(DbContract.DoctorEntry.COLUMN_GENDER, doctor.getGender());
            values.put(DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH, doctor.getDateOfBirth().toString());
            values.put(DbContract.DoctorEntry.COLUMN_SPECIALIZATION, doctor.getSpecialization().name());
            values.put(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER, doctor.getLicenseNumber());
            db.insertWithOnConflict(DbContract.DoctorEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    private static void populatePatients(SQLiteDatabase db) {
        List<Patient> patients = UserFactory.getDefaultPatients();
        for (Patient patient : patients) {
            ContentValues values = new ContentValues();
            values.put(DbContract.PatientEntry.COLUMN_FIRST_NAME, patient.getFirstName());
            values.put(DbContract.PatientEntry.COLUMN_LAST_NAME, patient.getLastName());
            values.put(DbContract.PatientEntry.COLUMN_EMAIL, patient.getEmail());
            values.put(DbContract.PatientEntry.COLUMN_PASSWORD, patient.getPassword());
            values.put(DbContract.PatientEntry.COLUMN_GENDER, patient.getGender());
            values.put(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH, patient.getDateOfBirth().toString());
            values.put(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER, patient.getHealthCardNumber());
            values.put(DbContract.PatientEntry.COLUMN_PHONE_NUMBER, patient.getPhoneNumber());
            db.insertWithOnConflict(DbContract.PatientEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    private static void populateStaffs(SQLiteDatabase db) {
        List<Staff> staffs = UserFactory.getDefaultStaffs();
        for (Staff staff : staffs) {
            ContentValues values = new ContentValues();
            values.put(DbContract.StaffEntry.COLUMN_FIRST_NAME, staff.getFirstName());
            values.put(DbContract.StaffEntry.COLUMN_LAST_NAME, staff.getLastName());
            values.put(DbContract.StaffEntry.COLUMN_EMAIL, staff.getEmail());
            values.put(DbContract.StaffEntry.COLUMN_PASSWORD, staff.getPassword());
            values.put(DbContract.StaffEntry.COLUMN_GENDER, staff.getGender());
            values.put(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH, staff.getDateOfBirth().toString());
            values.put(DbContract.StaffEntry.COLUMN_POSITION, staff.getPosition());
            db.insertWithOnConflict(DbContract.StaffEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    private static void addPastAppointments(SQLiteDatabase db) {
        List<Appointment> pastAppointments = UserFactory.getDefaultPastAppointments();
        for (Appointment appointment : pastAppointments) {
            ContentValues values = new ContentValues();
            values.put(DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL, appointment.getDoctorEmail());
            values.put(DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL, appointment.getPatientEmail());
            values.put(DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE, appointment.getAppointmentDate().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_START_TIME, appointment.getStartTime().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_END_TIME, appointment.getEndTime().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_STATUS, appointment.getStatus());
            values.put(DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE, appointment.getPatientPurpose());
            values.put(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES, appointment.getDoctorNotes());
            db.insertWithOnConflict(DbContract.AppointmentEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }
}
