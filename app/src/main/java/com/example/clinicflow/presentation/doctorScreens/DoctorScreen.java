package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.sharedScreens.Profile;
import com.example.clinicflow.presentation.sharedScreens.ViewPatients;

public class DoctorScreen extends AppCompatActivity {

    private Button logout;
    private ImageButton profile;
    private Button mySchd;
    private Button setAvail;
    private Button patientRecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doctor_landing);

        setViews();

        final String email = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> onLogoutClick(this));
        profile.setOnClickListener(v -> onClickEmail(this, Profile.class, email));
        mySchd.setOnClickListener(v -> onClickEmail(this, MySchedule.class, email));
        setAvail.setOnClickListener(v -> onClickEmail(this, SetAvailability.class, email));
        patientRecs.setOnClickListener(v -> onClickEmail(this, ViewPatients.class, email));
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        mySchd = findViewById(R.id.myScheduleButton);
        setAvail = findViewById(R.id.setAvailabilityButton);
        patientRecs = findViewById(R.id.patientRecordsButton);
    }
}
