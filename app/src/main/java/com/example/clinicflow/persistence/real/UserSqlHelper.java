package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//THis is a helper class so I will maket package private and not public, only SqlRepository can access it
class UserSqlHelper {
    private final AppDbHelper dbHelper;

    UserSqlHelper(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Patient> getAllPatients() {
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
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH))),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER))));
                } while (cursor.moveToNext());
            }
        }
        return patients;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.DoctorEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String specStr = cursor
                            .getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_SPECIALIZATION));
                    Specialization specialization;
                    try {
                        specialization = Specialization.valueOf(specStr.trim().toUpperCase().replace(" ", "_"));
                    } catch (Exception e) {
                        specialization = Specialization.GENERAL_MEDICINE;
                    }

                    doctors.add(new Doctor(
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_GENDER)),
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH))),
                            specialization,
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER))));
                } while (cursor.moveToNext());
            }
        }
        return doctors;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Staff> getAllStaffs() {
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
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_POSITION))));
                } while (cursor.moveToNext());
            }
        }
        return staffs;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Admin> getAllAdmins() {
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
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH)))));
                } while (cursor.moveToNext());
            }
        }
        return admins;
    }

    void addPatient(Patient patient) {
        if (patient == null) return;
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

    void addDoctor(Doctor doctor) {
        if (doctor == null) return;
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

    void addStaff(Staff staff) {
        if (staff == null) return;
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

    void deleteUser(Users user) {
        if (user == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (user instanceof Patient) {
                db.delete(DbContract.PatientEntry.TABLE_NAME, DbContract.PatientEntry.COLUMN_EMAIL + " = ?",
                        new String[] { user.getEmail() });
            } else if (user instanceof Doctor) {
                db.delete(DbContract.DoctorEntry.TABLE_NAME, DbContract.DoctorEntry.COLUMN_EMAIL + " = ?",
                        new String[] { user.getEmail() });
            } else if (user instanceof Staff) {
                db.delete(DbContract.StaffEntry.TABLE_NAME, DbContract.StaffEntry.COLUMN_EMAIL + " = ?",
                        new String[] { user.getEmail() });
            } else if (user instanceof Admin) {
                db.delete(DbContract.AdminEntry.TABLE_NAME, DbContract.AdminEntry.COLUMN_EMAIL + " = ?",
                        new String[] { user.getEmail() });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Patient getPatientByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.PatientEntry.TABLE_NAME, null,
                DbContract.PatientEntry.COLUMN_EMAIL + " = ?", new String[] { email }, null, null, null)) {
            if (cursor.moveToFirst()) {
                return new Patient(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_GENDER)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER)));
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Doctor getDoctorByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.DoctorEntry.TABLE_NAME, null,
                DbContract.DoctorEntry.COLUMN_EMAIL + " = ?", new String[] { email }, null, null, null)) {
            if (cursor.moveToFirst()) {
                String specStr = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_SPECIALIZATION));
                Specialization specialization;
                try {
                    specialization = Specialization.valueOf(specStr.trim().toUpperCase().replace(" ", "_"));
                } catch (Exception e) {
                    specialization = Specialization.GENERAL_MEDICINE;
                }
                return new Doctor(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_GENDER)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_DATE_OF_BIRTH))),
                        specialization,
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_LICENSE_NUMBER)));
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Staff getStaffByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.StaffEntry.TABLE_NAME, null,
                DbContract.StaffEntry.COLUMN_EMAIL + " = ?", new String[] { email }, null, null, null)) {
            if (cursor.moveToFirst()) {
                return new Staff(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_GENDER)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_POSITION)));
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Admin getAdminByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.AdminEntry.TABLE_NAME, null,
                DbContract.AdminEntry.COLUMN_EMAIL + " = ?", new String[] { email }, null, null, null)) {
            if (cursor.moveToFirst()) {
                return new Admin(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_GENDER)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH))));
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Users getUserByEmail(String email) {
        if (email == null) return null;
        Users user = getAdminByEmail(email);
        if (user == null) user = getDoctorByEmail(email);
        if (user == null) user = getStaffByEmail(email);
        if (user == null) user = getPatientByEmail(email);
        return user;
    }
}
