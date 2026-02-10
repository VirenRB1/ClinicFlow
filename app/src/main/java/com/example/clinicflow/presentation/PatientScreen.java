package com.example.clinicflow.presentation;

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

import com.example.clinicflow.MainActivity;
import com.example.clinicflow.R;

public class PatientScreen extends AppCompatActivity {

    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String EXTRA_DB = "fakeDB";
    Button logout;

    Button myApts;

    Button bookApt;

    Button myRecs;

    ImageButton profile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_landing);

        logout = findViewById(R.id.logoutButton);
        myApts = findViewById(R.id.myAppointmentsButton);
        bookApt = findViewById(R.id.bookAppointmentButton);
        myRecs = findViewById(R.id.myRecordsButton);
        profile = findViewById(R.id.profileButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        myApts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientScreen.this, MyAppointments.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });

        myRecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientScreen.this, MyRecords.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                intent.putExtra(EXTRA_DB, getIntent().getStringExtra(EXTRA_DB));
                startActivity(intent);
            }
        });

        bookApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientScreen.this, BookAppointment.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientScreen.this, PatientProfile.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
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
