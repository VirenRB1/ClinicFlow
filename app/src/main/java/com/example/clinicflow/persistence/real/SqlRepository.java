package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.persistence.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlRepository implements UserRepository {
    private final AppDbHelper dbHelper;

    public SqlRepository(Context context) {
        this.dbHelper = new AppDbHelper(context);
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.PatientEntry.TABLE_NAME, null, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    patients.add(new Patient(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_GENDER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_AGE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER))
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return patients;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.DoctorEntry.TABLE_NAME, null, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    doctors.add(new Doctor(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_GENDER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_AGE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_SPECIALIZATION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER))
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return doctors;
    }

    @Override
    public List<Staff> getAllStaffs() {
        List<Staff> staffs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.StaffEntry.TABLE_NAME, null, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    staffs.add(new Staff(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_GENDER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_AGE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_POSITION))
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return staffs;
    }

    @Override
    public void addPatient(Patient patient) {
        if (patient == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.PatientEntry.COLUMN_FIRST_NAME, patient.getFirstName());
            values.put(DbContract.PatientEntry.COLUMN_LAST_NAME, patient.getLastName());
            values.put(DbContract.PatientEntry.COLUMN_EMAIL, patient.getEmail());
            values.put(DbContract.PatientEntry.COLUMN_PASSWORD, patient.getPassword());
            values.put(DbContract.PatientEntry.COLUMN_GENDER, patient.getGender());
            values.put(DbContract.PatientEntry.COLUMN_AGE, patient.getAge());
            values.put(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER, patient.getHealthCardNumber());
            values.put(DbContract.PatientEntry.COLUMN_PHONE_NUMBER, patient.getPhoneNumber());
            db.insert(DbContract.PatientEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Patient getPatientByEmail(String email) {
        Patient patient = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.PatientEntry.TABLE_NAME, null, DbContract.PatientEntry.COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                patient = new Patient(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_GENDER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_AGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER))
                );
            }
        } finally {
            cursor.close();
        }
        return patient;
    }

    @Override
    public List<MedicalRecord> getMedicalRecords(String patientEmail) {
        List<MedicalRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbContract.MedicalRecordEntry.TABLE_NAME, null, DbContract.MedicalRecordEntry.COLUMN_EMAIL + " = ?", new String[]{patientEmail}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    records.add(new MedicalRecord(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_PATIENT_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_PURPOSE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NOTE)),
                            new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DATE)))
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return records;
    }

    @Override
    public void addMedicalRecord(MedicalRecord record) {
        if (record == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.MedicalRecordEntry.COLUMN_PATIENT_NAME, record.getPatientName());
            values.put(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NAME, record.getDoctorName());
            values.put(DbContract.MedicalRecordEntry.COLUMN_EMAIL, record.getEmail());
            values.put(DbContract.MedicalRecordEntry.COLUMN_PURPOSE, record.getPurpose());
            values.put(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NOTE, record.getDoctorNote());
            values.put(DbContract.MedicalRecordEntry.COLUMN_DATE, record.getDate().getTime());
            db.insert(DbContract.MedicalRecordEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
