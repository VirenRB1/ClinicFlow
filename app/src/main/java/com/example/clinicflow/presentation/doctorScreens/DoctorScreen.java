package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.Navigation.navigateWithUserEmail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.MyAppointments;
import com.example.clinicflow.presentation.sharedScreens.ViewPatients;

public class DoctorScreen extends AppCompatActivity {

    private BasicBinds binds;
    private Button appointments;
    private Button setAvail;
    private Button patientRecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doctor_landing);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        binds.setBasicEvents(this, email);
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
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
        binds = BasicBinds.setBasicBinds(this);
        appointments = findViewById(R.id.myAppointmentButton);
        setAvail = findViewById(R.id.setAvailabilityButton);
        patientRecs = findViewById(R.id.patientRecordsButton);
    }
}
