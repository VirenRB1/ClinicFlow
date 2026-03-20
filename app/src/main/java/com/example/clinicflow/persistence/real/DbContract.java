package com.example.clinicflow.persistence.real;

import android.provider.BaseColumns;

/**
 * Defines the schema of the SQLite database including table and column names.
 */
public final class DbContract {
    private DbContract() {
    }

    /**
     * Entry for the patients table.
     */
    public static class PatientEntry {
        public static final String TABLE_NAME = "patients";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
        public static final String COLUMN_HEALTHCARD_NUMBER = "healthcardNumber";
        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    }

    /**
     * Entry for the doctors table.
     */
    public static class DoctorEntry {
        public static final String TABLE_NAME = "doctors";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
        public static final String COLUMN_SPECIALIZATION = "specialization";
        public static final String COLUMN_LICENSE_NUMBER = "licenseNumber";
    }

    /**
     * Entry for the staffs table.
     */
    public static class StaffEntry {
        public static final String TABLE_NAME = "staffs";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
        public static final String COLUMN_POSITION = "position";
    }

    /**
     * Entry for the admin table.
     */
    public static class AdminEntry {
        public static final String TABLE_NAME = "admin";

        public static final String COLUMN_ADMIN_ID = "admin_id";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
    }

    /**
     * Entry for the appointments table.
     */
    public static final class AppointmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "appointments";
        public static final String COLUMN_DOCTOR_EMAIL = "doctor_email";
        public static final String COLUMN_PATIENT_EMAIL = "patient_email";
        public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_PATIENT_PURPOSE = "patient_purpose";
        public static final String COLUMN_DOCTOR_NOTES = "doctor_notes";
    }

    /**
     * Entry for the doctor availability table.
     */
    public static final class DoctorAvailabilityEntry implements BaseColumns {
        public static final String TABLE_NAME = "doctor_availability";
        public static final String COLUMN_DOCTOR_EMAIL = "doctor_email";
        public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
    }
}
