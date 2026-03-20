package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.NavigationExtras.EXTRA_USER_EMAIL;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.presentation.authScreens.MainActivity;

public final class Navigation {
    private Navigation() {
    }

    public static void logoutToMain(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void navigateWithUserEmail(Activity activity, Class<?> destination, String email) {
        Intent intent = new Intent(activity, destination);
        if (email != null) {
            intent.putExtra(EXTRA_USER_EMAIL, email);
        }
        activity.startActivity(intent);
    }

}
