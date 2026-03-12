package com.example.clinicflow.presentation;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.presentation.authScreens.MainActivity;
import com.example.clinicflow.presentation.sharedScreens.MyRecords;

public final class Navigation {
    private Navigation() {}

    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String EXTRA_PATIENT_EMAIL = "patient_email";
    public static final String EXTRA_DOCTOR_EMAIL = "doctor_email";
    public static final String EXTRA_APPT = "appointment";
    public static final String EXTRA_SLOT = "slot";
    public static final String USER_ROLE = "user_role";
    public static final String NOTES = "show_notes";

    public static void onLogoutClick(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void onClickEmail(Activity activity, Class<?> anyClass, String email) {
        Intent intent = new Intent(activity, anyClass);
        if(email != null){
            intent.putExtra(EXTRA_USER_EMAIL, email);
        }
        activity.startActivity(intent);
    }

}
