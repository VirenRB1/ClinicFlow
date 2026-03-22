package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.Navigation.navigateWithUserEmail;
import static com.example.clinicflow.presentation.Navigation.logoutToMain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.MyAppointments;
import com.example.clinicflow.presentation.sharedScreens.Profile;
import com.example.clinicflow.presentation.sharedScreens.ViewPatients;

public class DoctorScreen extends AppCompatActivity {

    private Button logout;
    private ImageButton profile;
    private Button appointments;
    private Button setAvail;
    private Button patientRecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doctor_landing);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> logoutToMain(this));
        profile.setOnClickListener(v -> navigateWithUserEmail(this, Profile.class, email));
        appointments.setOnClickListener(v -> navigateWithDoctorView(email));
        setAvail.setOnClickListener(v -> navigateWithUserEmail(this, SetAvailability.class, email));
        patientRecs.setOnClickListener(v -> navigateWithUserEmail(this, ViewPatients.class, email));
    }

    private void navigateWithDoctorView(String email) {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, email);
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, true);
        startActivity(intent);
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        appointments = findViewById(R.id.myAppointmentButton);
        setAvail = findViewById(R.id.setAvailabilityButton);
        patientRecs = findViewById(R.id.patientRecordsButton);
    }
}
