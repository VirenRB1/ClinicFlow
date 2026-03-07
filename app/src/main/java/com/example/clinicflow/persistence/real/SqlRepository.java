package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.clinicflow.models.Users;

public class SqlRepository implements UserRepository {
    private final AppDbHelper dbHelper;

    public SqlRepository(Context context) {
        this.dbHelper = new AppDbHelper(context);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.PatientEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    patients.add(new Patient(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_GENDER)),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return patients;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.DoctorEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    doctors.add(new Doctor(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_GENDER)),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH))),
                            Specialization.valueOf(
                                    cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_SPECIALIZATION))
                            ),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return doctors;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Staff> getAllStaffs() {
        List<Staff> staffs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.StaffEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    staffs.add(new Staff(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_GENDER)),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_POSITION))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return staffs;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.AdminEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    admins.add(new Admin(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_GENDER)),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH)))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return admins;
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
            values.put(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH, patient.getDateOfBirth().toString());
            values.put(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER, patient.getHealthCardNumber());
            values.put(DbContract.PatientEntry.COLUMN_PHONE_NUMBER, patient.getPhoneNumber());
            db.insert(DbContract.PatientEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void addDoctor(Doctor doctor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.DoctorEntry.COLUMN_FIRST_NAME, doctor.getFirstName());
            values.put(DbContract.DoctorEntry.COLUMN_LAST_NAME, doctor.getLastName());
            values.put(DbContract.DoctorEntry.COLUMN_EMAIL, doctor.getEmail());
            values.put(DbContract.DoctorEntry.COLUMN_PASSWORD, doctor.getPassword());
            values.put(DbContract.DoctorEntry.COLUMN_GENDER, doctor.getGender());
            values.put(DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH, doctor.getDateOfBirth().toString());
            values.put(DbContract.DoctorEntry.COLUMN_SPECIALIZATION, doctor.getSpecialization().name());
            values.put(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER, doctor.getLicenseNumber());
            db.insert(DbContract.DoctorEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void addStaff(Staff staff) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.StaffEntry.COLUMN_FIRST_NAME, staff.getFirstName());
            values.put(DbContract.StaffEntry.COLUMN_LAST_NAME, staff.getLastName());
            values.put(DbContract.StaffEntry.COLUMN_EMAIL, staff.getEmail());
            values.put(DbContract.StaffEntry.COLUMN_PASSWORD, staff.getPassword());
            values.put(DbContract.StaffEntry.COLUMN_GENDER, staff.getGender());
            values.put(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH, staff.getDateOfBirth().toString());
            values.put(DbContract.StaffEntry.COLUMN_POSITION, staff.getPosition());
            db.insert(DbContract.StaffEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteUser(Users user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            if (user instanceof Patient) {
                db.delete(DbContract.PatientEntry.TABLE_NAME, DbContract.PatientEntry.COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            } else if (user instanceof Doctor) {
                db.delete(DbContract.DoctorEntry.TABLE_NAME, DbContract.DoctorEntry.COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            } else if (user instanceof Staff) {
                db.delete(DbContract.StaffEntry.TABLE_NAME, DbContract.StaffEntry.COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            } else if (user instanceof Admin) {
                db.delete(DbContract.AdminEntry.TABLE_NAME, DbContract.AdminEntry.COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Patient getPatientByEmail(String email) {
        Patient patient = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.PatientEntry.TABLE_NAME, null, DbContract.PatientEntry.COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null)) {
            if (cursor.moveToFirst()) {
                patient = new Patient(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_GENDER)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER))
                );
            }
        }
        return patient;
    }

    @Override
    public List<MedicalRecord> getMedicalRecords(String patientEmail) {
        List<MedicalRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.MedicalRecordEntry.TABLE_NAME, null, DbContract.MedicalRecordEntry.COLUMN_PATIENT_EMAIL + " = ?", new String[]{patientEmail}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    records.add(new MedicalRecord(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_RECORD_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_PATIENT_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_PATIENT_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_PURPOSE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DOCTOR_NOTE)),
                            new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.MedicalRecordEntry.COLUMN_DATE)))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return records;
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Users getUserByEmail(String email) {
        for (Admin admin : getAllAdmins()) {
            if (admin.getEmail().equalsIgnoreCase(email)) {
                return admin;
            }
        }

        for (Doctor doctor : getAllDoctors()) {
            if (doctor.getEmail().equalsIgnoreCase(email)) {
                return doctor;
            }
        }

        for (Staff staff : getAllStaffs()) {
            if (staff.getEmail().equalsIgnoreCase(email)) {
                return staff;
            }
        }

        for (Patient patient : getAllPatients()) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return patient;
            }
        }

        return null;
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
            values.put(DbContract.MedicalRecordEntry.COLUMN_PATIENT_EMAIL, record.getEmail());
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
