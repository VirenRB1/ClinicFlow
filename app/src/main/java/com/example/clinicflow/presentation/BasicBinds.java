package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.Navigation.logoutToMain;

import android.app.Activity;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class BasicBinds {

    private final View logout;
    private final View profile;
    private final View back;

    private BasicBinds (View logout, View profile, View back) {
        this.logout = logout;
        this.profile = profile;
        this.back = back;
    }

    public static BasicBinds setBasicBinds(Activity activity) {
        View logout = activity.findViewById(R.id.logoutButton);
        View profile = activity.findViewById(R.id.profileButton);
        View back = activity.findViewById(R.id.backButton);

        return new BasicBinds(logout, profile, back);
    }

    public static void setWindowInsets(Activity activity) {
        ViewCompat.setOnApplyWindowInsetsListener(activity.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setBasicEvents(Activity activity, String email) {
        if (logout != null) {
            logout.setOnClickListener(v -> logoutToMain(activity));
        }

        if (back != null) {
            back.setOnClickListener(v -> activity.finish());
        }

        if (profile != null) {
            profile.setOnClickListener(v -> Navigation.navigateWithUserEmail(activity, Profile.class, email));
        }
    }
}
