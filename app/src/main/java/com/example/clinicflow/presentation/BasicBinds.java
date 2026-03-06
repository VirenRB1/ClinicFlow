package com.example.clinicflow.presentation;

import android.app.Activity;
import android.widget.Button;

import com.example.clinicflow.R;

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
}
