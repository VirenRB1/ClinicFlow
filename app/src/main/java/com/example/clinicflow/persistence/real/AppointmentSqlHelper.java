package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Helper class only so It will be package private and not public, only SqlRepository can access it
class AppointmentSqlHelper {
    private final AppDbHelper dbHelper;

    AppointmentSqlHelper(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    void addAppointment(Appointment appointment) {
        if (appointment == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_DOCTOR_EMAIL, appointment.getDoctorEmail());
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_PATIENT_EMAIL, appointment.getPatientEmail());
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_APPOINTMENT_DATE,
                    appointment.getAppointmentDate().toString());
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_START_TIME, appointment.getStartTime().toString());
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_END_TIME, appointment.getEndTime().toString());
            values.put(DbContract.UpcomingAppointmentEntry.COLUMN_PATIENT_PURPOSE, appointment.getPatientPurpose());
            db.insert(DbContract.UpcomingAppointmentEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    void updateAppointment(Appointment appointment) {
        if (appointment == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
                db.delete(DbContract.UpcomingAppointmentEntry.TABLE_NAME,
                        DbContract.UpcomingAppointmentEntry._ID + " = ?",
                        new String[]{String.valueOf(appointment.getId())});

                ContentValues values = new ContentValues();
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_EMAIL, appointment.getDoctorEmail());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_EMAIL, appointment.getPatientEmail());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_APPOINTMENT_DATE, appointment.getAppointmentDate().toString());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_START_TIME, appointment.getStartTime().toString());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_END_TIME, appointment.getEndTime().toString());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_PURPOSE, appointment.getPatientPurpose());
                values.put(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_NOTES, appointment.getDoctorNotes());
                db.insert(DbContract.CompletedAppointmentEntry.TABLE_NAME, null, values);
            } else if (AppointmentStatus.CANCELLED.equals(appointment.getStatus())) {
                db.delete(DbContract.UpcomingAppointmentEntry.TABLE_NAME,
                        DbContract.UpcomingAppointmentEntry._ID + " = ?",
                        new String[]{String.valueOf(appointment.getId())});

                ContentValues cancelValues = new ContentValues();
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_EMAIL, appointment.getDoctorEmail());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_EMAIL, appointment.getPatientEmail());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_APPOINTMENT_DATE, appointment.getAppointmentDate().toString());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_START_TIME, appointment.getStartTime().toString());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_END_TIME, appointment.getEndTime().toString());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_PURPOSE, appointment.getPatientPurpose());
                cancelValues.put(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_NOTES, appointment.getDoctorNotes());
                db.insert(DbContract.CompletedAppointmentEntry.TABLE_NAME, null, cancelValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbContract.UpcomingAppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ? AND " +
                DbContract.UpcomingAppointmentEntry.COLUMN_APPOINTMENT_DATE + " = ?";
        String[] selectionArgs = { doctorEmail, date.toString() };

        try (Cursor cursor = db.query(
                DbContract.UpcomingAppointmentEntry.TABLE_NAME,
                null, selection, selectionArgs, null, null,
                DbContract.UpcomingAppointmentEntry.COLUMN_START_TIME + " ASC")) {
            while (cursor.moveToNext()) {
                appointments.add(mapUpcoming(cursor));
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getUpcomingAppointmentsForDoctorOnDay(String doctorEmail, int dayOfWeek) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LocalDate today = LocalDate.now();

        String selection = DbContract.UpcomingAppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ? AND " +
                DbContract.UpcomingAppointmentEntry.COLUMN_APPOINTMENT_DATE + " >= ?";
        String[] selectionArgs = { doctorEmail, today.toString() };

        try (Cursor cursor = db.query(DbContract.UpcomingAppointmentEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                Appointment appt = mapUpcoming(cursor);
                if (appt.getAppointmentDate().getDayOfWeek().getValue() == dayOfWeek) {
                    appointments.add(appt);
                }
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getUpcomingAppointmentsForPatient(String patientEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.UpcomingAppointmentEntry.COLUMN_PATIENT_EMAIL + " = ?";
        String[] selectionArgs = { patientEmail };
        try (Cursor cursor = db.query(DbContract.UpcomingAppointmentEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                appointments.add(mapUpcoming(cursor));
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getCompletedAppointmentsForPatient(String patientEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_EMAIL + " = ?";
        String[] selectionArgs = { patientEmail };
        try (Cursor cursor = db.query(DbContract.CompletedAppointmentEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                appointments.add(mapCompleted(cursor));
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getUpcomingAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.UpcomingAppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ?";
        String[] selectionArgs = { doctorEmail };
        try (Cursor cursor = db.query(DbContract.UpcomingAppointmentEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                appointments.add(mapUpcoming(cursor));
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Appointment> getCompletedAppointmentsForDoctor(String doctorEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_EMAIL + " = ?";
        String[] selectionArgs = { doctorEmail };
        try (Cursor cursor = db.query(DbContract.CompletedAppointmentEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                appointments.add(mapCompleted(cursor));
            }
        }
        return appointments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Appointment mapUpcoming(Cursor cursor) {
        return new Appointment(
                cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_DOCTOR_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_PATIENT_EMAIL)),
                LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_APPOINTMENT_DATE))),
                LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_START_TIME))),
                LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_END_TIME))),
                AppointmentStatus.CONFIRMED,
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.UpcomingAppointmentEntry.COLUMN_PATIENT_PURPOSE)),
                null
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Appointment mapCompleted(Cursor cursor) {
        return new Appointment(
                cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_EMAIL)),
                LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_APPOINTMENT_DATE))),
                LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_START_TIME))),
                LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_END_TIME))),
                AppointmentStatus.COMPLETED,
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_PATIENT_PURPOSE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CompletedAppointmentEntry.COLUMN_DOCTOR_NOTES))
        );
    }
}
