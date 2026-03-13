package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinic_flow.db";
    public static final int DATABASE_VERSION = 6;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createAdminTable(db);
        createPatientTable(db);
        createDoctorTable(db);
        createStaffTable(db);
        db.execSQL(SQL_CREATE_APPOINTMENT_TABLE);
        db.execSQL(SQL_CREATE_DOCTOR_AVAILABILITY_TABLE);
        // Adding the ddefault admin
        addAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.StaffEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.DoctorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.PatientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.AdminEntry.TABLE_NAME);
        db.execSQL(SQL_DROP_APPOINTMENT_TABLE);
        db.execSQL(SQL_DROP_DOCTOR_AVAILABILITY_TABLE);
        onCreate(db);
    }

    private void createPatientTable(SQLiteDatabase db) {
        String createPatientTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.PatientEntry.TABLE_NAME + " (" +
                DbContract.PatientEntry.COLUMN_FIRST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_LAST_NAME + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL UNIQUE PRIMARY KEY, " +
                DbContract.PatientEntry.COLUMN_PASSWORD + " VARCHAR(255) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_GENDER + " VARCHAR(10) NOT NULL, " +
                DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
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
                DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
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
                DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
                DbContract.StaffEntry.COLUMN_POSITION + " VARCHAR(255) NOT NULL" +
                ");";
        db.execSQL(createStaffTableQuery);
    }

    private void createAdminTable(SQLiteDatabase db) {
        String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.AdminEntry.TABLE_NAME + " (" +
                DbContract.AdminEntry.COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.AdminEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                DbContract.AdminEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                DbContract.AdminEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL UNIQUE, " +
                DbContract.AdminEntry.COLUMN_PASSWORD + " VARCHAR(255) NOT NULL, " +
                DbContract.AdminEntry.COLUMN_GENDER + " VARCHAR(10) NOT NULL, " +
                DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL" +
                ");";
        db.execSQL(createAdminTableQuery);
    }

    private void addAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DbContract.AdminEntry.COLUMN_FIRST_NAME, "Admin");
        values.put(DbContract.AdminEntry.COLUMN_LAST_NAME, "Admin");
        values.put(DbContract.AdminEntry.COLUMN_EMAIL, "admin@clinic.com");
        values.put(DbContract.AdminEntry.COLUMN_PASSWORD, "admin");
        values.put(DbContract.AdminEntry.COLUMN_GENDER, "Female");
        values.put(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH, "1990-01-01");
        
        db.insert(DbContract.AdminEntry.TABLE_NAME, null, values);
    }
    private static final String SQL_CREATE_APPOINTMENT_TABLE =
            "CREATE TABLE " + DbContract.AppointmentEntry.TABLE_NAME + " (" +
                    DbContract.AppointmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_END_TIME + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                    DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE + " TEXT DEFAULT '', " +
                    DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES + " TEXT DEFAULT ''" +
                    ");";

    private static final String SQL_CREATE_DOCTOR_AVAILABILITY_TABLE =
            "CREATE TABLE " + DbContract.DoctorAvailabilityEntry.TABLE_NAME + " (" +
                    DbContract.DoctorAvailabilityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL + " TEXT NOT NULL, " +
                    DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK + " INTEGER NOT NULL, " +
                    DbContract.DoctorAvailabilityEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                    DbContract.DoctorAvailabilityEntry.COLUMN_END_TIME + " TEXT NOT NULL" +
                    ");";

    private static final String SQL_DROP_APPOINTMENT_TABLE =
            "DROP TABLE IF EXISTS " + DbContract.AppointmentEntry.TABLE_NAME;

    private static final String SQL_DROP_DOCTOR_AVAILABILITY_TABLE =
            "DROP TABLE IF EXISTS " + DbContract.DoctorAvailabilityEntry.TABLE_NAME;


}
