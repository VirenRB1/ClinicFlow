package com.example.clinicflow.persistence.real;

public final class DbContract {
    private DbContract() {
    }

    public static class PatientEntry {
        public static final String TABLE_NAME = "patients";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_HEALTHCARD_NUMBER = "healthcardNumber";
        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    }

    public static class DoctorEntry {
        public static final String TABLE_NAME = "doctors";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_SPECIALIZATION = "specialization";
        public static final String COLUMN_LICENSE_NUMBER = "licenseNumber";
    }

    public static class StaffEntry {
        public static final String TABLE_NAME = "staffs";
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_POSITION = "position";
    }

    public static class MedicalRecordEntry {
        public static final String TABLE_NAME = "medical_records";
        public static final String COLUMN_RECORD_ID = "record_id";
        public static final String COLUMN_PATIENT_NAME = "patientName";
        public static final String COLUMN_DOCTOR_NAME = "doctorName";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PURPOSE = "purpose";
        public static final String COLUMN_DOCTOR_NOTE = "doctorNote";
        public static final String COLUMN_DATE = "date";
    }
}
