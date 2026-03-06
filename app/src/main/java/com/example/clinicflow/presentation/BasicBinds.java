package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.app.Activity;
import android.widget.Button;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class BasicBinds {

    public Button logout;
    public Button profile;
    public Button back;

    public static BasicBinds setBasicBinds(Activity activity) {
        BasicBinds binds = new BasicBinds();

        binds.logout = activity.findViewById(R.id.logoutButton);
        binds.profile = activity.findViewById(R.id.profileButton);
        binds.back = activity.findViewById(R.id.backButton);

        return binds;
    }

    public void setBasicEvents(Activity activity, String email) {
        logout.setOnClickListener(v -> onLogoutClick(activity));
        back.setOnClickListener(v -> activity.finish());
        profile.setOnClickListener(v -> onClickEmail(activity, Profile.class, email));
    }
}
