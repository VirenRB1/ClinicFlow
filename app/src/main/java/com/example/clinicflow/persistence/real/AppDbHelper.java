package com.example.clinicflow.persistence.real;

import static com.example.clinicflow.persistence.real.DbFactory.populateFakeData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite helper class for managing database creation and version management.
 */
public class AppDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinic_flow.db";
    public static final int DATABASE_VERSION = 9;

    /**
     * Constructs a new database helper.
     * 
     * @param context The application context.
     */
    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Creates all required tables and populates seed data.
     * 
     * @param db The database.
     */
    public void onCreate(SQLiteDatabase db) {
        createAdminTable(db);
        createPatientTable(db);
        createDoctorTable(db);
        createStaffTable(db);
        createAppointmentTable(db);
        createDoctorAvailabilityTable(db);
        populateFakeData(db);
    }

    /**
     * Called when the database needs to be upgraded.
     * 
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.StaffEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.DoctorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.PatientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.AdminEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.AppointmentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.DoctorAvailabilityEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Creates the patients table.
     */
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

    /**
     * Creates the doctors table.
     */
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

    /**
     * Creates the staffs table.
     */
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

    /**
     * Creates the admin table.
     */
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

    /**
     * Creates the appointments table.
     */
    private void createAppointmentTable(SQLiteDatabase db) {
        String createAppointmentTableQuery = "CREATE TABLE IF NOT EXISTS " + DbContract.AppointmentEntry.TABLE_NAME
                + " (" +
                DbContract.AppointmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_END_TIME + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE + " TEXT DEFAULT '', " +
                DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES + " TEXT" +
                ");";
        db.execSQL(createAppointmentTableQuery);
    }

    /**
     * Creates the doctor availability table.
     */
    private void createDoctorAvailabilityTable(SQLiteDatabase db) {
        String createDoctorAvailabilityTableQuery = "CREATE TABLE IF NOT EXISTS "
                + DbContract.DoctorAvailabilityEntry.TABLE_NAME + " (" +
                DbContract.DoctorAvailabilityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL + " TEXT NOT NULL, " +
                DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK + " INTEGER NOT NULL, " +
                DbContract.DoctorAvailabilityEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                DbContract.DoctorAvailabilityEntry.COLUMN_END_TIME + " TEXT NOT NULL" +
                ");";

        db.execSQL(createDoctorAvailabilityTableQuery);
    }
}
