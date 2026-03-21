package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.persistence.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.clinicflow.models.Users;
import java.time.LocalTime;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;

/**
 * Implementation of UserRepository using a SQLite database.
 * Handles all database operations for users, appointments, and availability.
 */
public class SqlRepository implements UserRepository {
    private final AppDbHelper dbHelper;

    /**
     * Constructor for SqlRepository.
     * 
     * @param context The application context.
     */
    public SqlRepository(Context context) {
        this.dbHelper = new AppDbHelper(context);
    }

    /**
     * Retrieves all patients from the database.
     * 
     * @return A list of all patients.
     */
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

    /**
     * Retrieves all doctors from the database.
     * 
     * @return A list of all doctors.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DbContract.DoctorEntry.TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String specStr = cursor
                            .getString(cursor.getColumnIndexOrThrow(DbContract.DoctorEntry.COLUMN_SPECIALIZATION));
                    Specialization specialization = null;
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

    /**
     * Retrieves all staff members from the database.
     * 
     * @return A list of all staff.
     */
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
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_DATE_OF_BIRTH))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StaffEntry.COLUMN_POSITION))));
                } while (cursor.moveToNext());
            }
        }
        return staffs;
    }

    /**
     * Retrieves all admins from the database.
     * 
     * @return A list of all admins.
     */
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
                            LocalDate.parse(cursor.getString(
                                    cursor.getColumnIndexOrThrow(DbContract.AdminEntry.COLUMN_DATE_OF_BIRTH)))));
                } while (cursor.moveToNext());
            }
        }
        return admins;
    }

    /**
     * Adds a new patient to the database.
     * 
     * @param patient The patient to add.
     */
    @Override
    public void addPatient(Patient patient) {
        if (patient == null)
            return;
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

    /**
     * Adds a new doctor to the database.
     * 
     * @param doctor The doctor to add.
     */
    @Override
    public void addDoctor(Doctor doctor) {
        if (doctor == null)
            return;
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

    /**
     * Adds a new staff member to the database.
     * 
     * @param staff The staff to add.
     */
    @Override
    public void addStaff(Staff staff) {
        if (staff == null)
            return;
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

    /**
     * Deletes a user from the database.
     * 
     * @param user The user to delete.
     */
    @Override
    public void deleteUser(Users user) {
        if (user == null)
            return;
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

    /**
     * Retrieves a patient by email.
     * 
     * @param email The email address.
     * @return The Patient if found, null otherwise.
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Patient getPatientByEmail(String email) {
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
                        LocalDate.parse(cursor
                                .getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_DATE_OF_BIRTH))),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_HEALTHCARD_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PatientEntry.COLUMN_PHONE_NUMBER)));
            }
        }
        return null;
    }

    /**
     * Retrieves a user by email, searching through all user types.
     * 
     * @param email The email address.
     * @return The User if found, null otherwise.
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Users getUserByEmail(String email) {
        if (email == null)
            return null;

        for (Admin admin : getAllAdmins()) {
            if (admin.getEmail().equalsIgnoreCase(email))
                return admin;
        }
        for (Doctor doctor : getAllDoctors()) {
            if (doctor.getEmail().equalsIgnoreCase(email))
                return doctor;
        }
        for (Staff staff : getAllStaffs()) {
            if (staff.getEmail().equalsIgnoreCase(email))
                return staff;
        }
        for (Patient patient : getAllPatients()) {
            if (patient.getEmail().equalsIgnoreCase(email))
                return patient;
        }

        return null;
    }

    /**
     * Adds a new appointment to the database.
     * 
     * @param appointment The appointment to add.
     */
    @Override
    public void addAppointment(Appointment appointment) {
        if (appointment == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL, appointment.getDoctorEmail());
            values.put(DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL, appointment.getPatientEmail());
            values.put(DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE,
                    appointment.getAppointmentDate().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_START_TIME, appointment.getStartTime().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_END_TIME, appointment.getEndTime().toString());
            values.put(DbContract.AppointmentEntry.COLUMN_STATUS, appointment.getStatus());
            values.put(DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE, appointment.getPatientPurpose());
            values.put(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES, appointment.getDoctorNotes());

            db.insert(DbContract.AppointmentEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Adds doctor availability to the database.
     * 
     * @param availability The availability details.
     */
    @Override
    public void addDoctorAvailability(DoctorAvailability availability) {
        if (availability == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL, availability.getDoctorEmail());
            values.put(DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK, availability.getDayOfWeek());
            values.put(DbContract.DoctorAvailabilityEntry.COLUMN_START_TIME, availability.getStartTime().toString());
            values.put(DbContract.DoctorAvailabilityEntry.COLUMN_END_TIME, availability.getEndTime().toString());

            db.insert(DbContract.DoctorAvailabilityEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieves appointments for a doctor on a specific date.
     * 
     * @param doctorEmail The doctor's email.
     * @param date        The date.
     * @return List of appointments.
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ? AND " +
                DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " = ?";

        String[] selectionArgs = { doctorEmail, date.toString() };

        try (Cursor cursor = db.query(
                DbContract.AppointmentEntry.TABLE_NAME,
                null, selection, selectionArgs, null, null,
                DbContract.AppointmentEntry.COLUMN_START_TIME + " ASC")) {

            while (cursor.moveToNext()) {
                appointments.add(new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL)),
                        LocalDate.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE))),
                        LocalTime.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_START_TIME))),
                        LocalTime.parse(cursor
                                .getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_END_TIME))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_STATUS)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES))));
            }
        }
        return appointments;
    }

    /**
     * Retrieves availability for a doctor on a specific day of the week.
     * 
     * @param doctorEmail The doctor's email.
     * @param dayOfWeek   The day of the week.
     * @return List of availability.
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        List<DoctorAvailability> availability = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL + " = ? AND " +
                DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK + " = ?";

        String[] selectionArgs = { doctorEmail, String.valueOf(dayOfWeek) };

        try (Cursor cursor = db.query(
                DbContract.DoctorAvailabilityEntry.TABLE_NAME,
                null, selection, selectionArgs, null, null, null)) {

            while (cursor.moveToNext()) {
                availability.add(new DoctorAvailability(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry._ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL)),
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK)),
                        LocalTime.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_START_TIME))),
                        LocalTime.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_END_TIME)))));
            }
        }
        return availability;
    }

    /**
     * Retrieves all appointments for a patient.
     * 
     * @param patientEmail The patient's email.
     * @return List of appointments.
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> getAppointmentsForPatient(String patientEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL + " = ?";
        String[] selectionArgs = { patientEmail };

        try (Cursor cursor = db.query(
                DbContract.AppointmentEntry.TABLE_NAME,
                null, selection, selectionArgs, null, null,
                DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " ASC, " +
                        DbContract.AppointmentEntry.COLUMN_START_TIME + " ASC")) {

            while (cursor.moveToNext()) {
                appointments.add(new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL)),
                        LocalDate.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE))),
                        LocalTime.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_START_TIME))),
                        LocalTime.parse(cursor
                                .getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_END_TIME))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_STATUS)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES))));
            }
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ?";
        String[] selectionArgs = { doctorEmail };

        try (Cursor cursor = db.query(
                DbContract.AppointmentEntry.TABLE_NAME,
                null, selection, selectionArgs, null, null,
                DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " ASC, " +
                        DbContract.AppointmentEntry.COLUMN_START_TIME + " ASC")) {
            while (cursor.moveToNext()) {
                appointments.add(new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_EMAIL)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_EMAIL)),
                        LocalDate.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_APPOINTMENT_DATE))),
                        LocalTime.parse(cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_START_TIME))),
                        LocalTime.parse(cursor
                                .getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_END_TIME))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_STATUS)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_PATIENT_PURPOSE)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES))));
            }
        }
        return appointments;
    }



    @Override
    public void updateAppointment(Appointment appointment) {
        if (appointment == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.AppointmentEntry.COLUMN_STATUS, appointment.getStatus());
            values.put(DbContract.AppointmentEntry.COLUMN_DOCTOR_NOTES, appointment.getDoctorNotes());

            db.update(DbContract.AppointmentEntry.TABLE_NAME, values, DbContract.AppointmentEntry._ID + " = ?", new String[]{String.valueOf(appointment.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
