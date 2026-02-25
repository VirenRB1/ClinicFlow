package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.authScreens.MainActivity;
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

        final String email = getIntent().getStringExtra(MainActivity.EXTRA_USER_EMAIL);

        setEvents(email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> onLogoutClick(this));
        profile.setOnClickListener(v -> onClickEmail(this, DoctorProfile.class, email));
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
