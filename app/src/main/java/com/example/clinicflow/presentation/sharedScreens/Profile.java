package com.example.clinicflow.presentation.sharedScreens;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

public class Profile extends AppCompatActivity {

    private BasicBinds binds;
    private LookupService lookupService;
    private ProfileView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        lookupService = app.getLookupService();

        binds = BasicBinds.setBasicBinds(this);
        view = new ProfileView(this);

        final String userEmail =
                getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        binds.setBasicEvents(this, userEmail);

        loadProfile(userEmail);

        BasicBinds.setWindowInsets(this);
    }

    private void loadProfile(String userEmail) {
        Users user = lookupService.findUserByEmail(userEmail);

        if (user == null) {
            return;
        }

        view.showProfile(user);
    }
}