package com.example.clinicflow.persistence.real;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clinicflow.models.DoctorAvailability;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Helper class only so It will be package private and not public, only SqlRepository can access it
class AvailabilitySqlHelper {
    private final AppDbHelper dbHelper;

    AvailabilitySqlHelper(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    void addDoctorAvailability(DoctorAvailability availability) {
        if (availability == null) return;
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

    void deleteDoctorAvailability(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DbContract.DoctorAvailabilityEntry.TABLE_NAME,
                    DbContract.DoctorAvailabilityEntry._ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
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
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_DOCTOR_EMAIL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_DAY_OF_WEEK)),
                        LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_START_TIME))),
                        LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DoctorAvailabilityEntry.COLUMN_END_TIME)))));
            }
        }
        return availability;
    }
}
