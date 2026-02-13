package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.PatientScreen.EXTRA_USER_EMAIL;

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

public class DoctorScreen extends AppCompatActivity {

    public static final String DOCTOR = "Doctor";
    Button logout;

    ImageButton profile;

    Button mySchd;

    Button setAvail;

    Button patientRecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doctor_landing);

        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        mySchd = findViewById(R.id.myScheduleButton);
        setAvail = findViewById(R.id.setAvailabilityButton);
        patientRecs = findViewById(R.id.patientRecordsButton);

        String email = getIntent().getStringExtra(EXTRA_USER_EMAIL);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, DoctorProfile.class);
                startActivity(intent);
            }
        });

        mySchd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, MySchedule.class);
                startActivity(intent);
            }
        });

        setAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, SetAvailability.class);
                startActivity(intent);
            }
        });

        patientRecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorScreen.this, ViewPatients.class);
                intent.putExtra(EXTRA_USER_EMAIL, email);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
