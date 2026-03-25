package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.NavigationExtras.EXTRA_USER_EMAIL;
import static com.example.clinicflow.presentation.NavigationExtras.SEARCH_MODE;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.presentation.authScreens.MainActivity;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCard;

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


    public static void navigateToSearchCard(Activity activity, String email, String mode) {
        Intent intent = new Intent(activity, SearchUserCard.class);
        if (email != null) {
            intent.putExtra(EXTRA_USER_EMAIL, email);
        }
        intent.putExtra(SEARCH_MODE, mode);
        activity.startActivity(intent);
    }

}
