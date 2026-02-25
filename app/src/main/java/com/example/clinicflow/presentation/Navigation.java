package com.example.clinicflow.presentation;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.presentation.authScreens.MainActivity;

public final class Navigation {
    public static void onLogoutClick(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void onClickEmail(Activity activity, Class<?> anyClass, String email) {
        Intent intent = new Intent(activity, anyClass);
        if(email != null){
            intent.putExtra(MainActivity.EXTRA_USER_EMAIL, email);
        }
        activity.startActivity(intent);
    }
}
