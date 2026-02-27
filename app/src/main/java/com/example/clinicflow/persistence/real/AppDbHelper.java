package com.example.clinicflow.persistence.real;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinic_flow.db";
    public static final int DATABASE_VERSION = 2;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createPatientTable(db);
        createDoctorTable(db);
        createStaffTable(db);
        createMedicalRecordTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.MedicalRecordEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.StaffEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.DoctorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.PatientEntry.TABLE_NAME);
        onCreate(db);
    }

    private void createPatientTable(SQLiteDatabase db) {
        String createPatientTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.PatientEntry.TABLE_NAME + " (" +
                DbContract.PatientEntry.COLUMN_FIRST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_LAST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL UNIQUE PRIMARY KEY, " +
                DbContract.PatientEntry.COLUMN_PASSWORD + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_GENDER + " VARCHAR(10) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER + " INTEGER NOT NULL UNIQUE, " +
                DbContract.PatientEntry.COLUMN_PHONE_NUMBER + " INTEGER NOT NULL UNIQUE" +
                ");";
        db.execSQL(createPatientTableQuery);
    }

    private void createDoctorTable(SQLiteDatabase db) {
        String createDoctorTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.DoctorEntry.TABLE_NAME + " (" +
                DbContract.DoctorEntry.COLUMN_FIRST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_LAST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL UNIQUE PRIMARY KEY, " +
                DbContract.DoctorEntry.COLUMN_PASSWORD + " VARCHAR(255) NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_GENDER + " VARCHAR(10) NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_SPECIALIZATION + " VARCHAR(255) NOT NULL, " +
                DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER + " VARCHAR(255) NOT NULL UNIQUE" +
                ");";
        db.execSQL(createDoctorTableQuery);
    }

    private void createStaffTable(SQLiteDatabase db) {
        String createStaffTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.StaffEntry.TABLE_NAME + " (" +
                DbContract.StaffEntry.COLUMN_FIRST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.StaffEntry.COLUMN_LAST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.StaffEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL UNIQUE PRIMARY KEY, " +
                DbContract.StaffEntry.COLUMN_PASSWORD + " VARCHAR(255) NOT NULL, " +
                DbContract.StaffEntry.COLUMN_GENDER + " VARCHAR(10) NOT NULL, " +
                DbContract.StaffEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                DbContract.StaffEntry.COLUMN_POSITION + " VARCHAR(255) NOT NULL" +
                ");";
        db.execSQL(createStaffTableQuery);
    }

    private void createMedicalRecordTable(SQLiteDatabase db) {
        String createMedicalRecordTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.MedicalRecordEntry.TABLE_NAME
                + " (" +
                DbContract.MedicalRecordEntry.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MedicalRecordEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, " +
                DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NAME + " TEXT NOT NULL, " +
                DbContract.MedicalRecordEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL, " +
                DbContract.MedicalRecordEntry.COLUMN_PURPOSE + " VARCHAR(255) NOT NULL, " +
                DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NOTE + " TEXT NOT NULL, " +
                DbContract.MedicalRecordEntry.COLUMN_DATE + " DATE NOT NULL," +
                "FOREIGN KEY(" + DbContract.MedicalRecordEntry.COLUMN_EMAIL + ") REFERENCES "
                + DbContract.PatientEntry.TABLE_NAME + "(" + DbContract.PatientEntry.COLUMN_EMAIL + ")" +
                ");";
        db.execSQL(createMedicalRecordTableQuery);
    }
}
