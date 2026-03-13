package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.Navigation.logoutToMain;

import android.app.Activity;
import android.widget.Button;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class BasicBinds {

    private Button logout;
    private Button profile;
    private Button back;

    public static BasicBinds setBasicBinds(Activity activity) {
        BasicBinds binds = new BasicBinds();

        binds.logout = activity.findViewById(R.id.logoutButton);
        binds.profile = activity.findViewById(R.id.profileButton);
        binds.back = activity.findViewById(R.id.backButton);

        return binds;
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
            profile.setOnClickListener(v ->
                    Navigation.navigateWithUserEmail(activity, Profile.class, email));
        }
    }
}
