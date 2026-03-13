package com.example.clinicflow.presentation.patientScreens;

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

public class PatientScreen extends AppCompatActivity {

    private Button logout;
    private Button myApts;
    private Button bookApt;
    private Button myRecs;
    private ImageButton profile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_landing);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> logoutToMain(this));
        myApts.setOnClickListener(v -> onRecordClick(email, false));
        bookApt.setOnClickListener(v -> navigateWithUserEmail(this, BookAppointment.class, email));
        myRecs.setOnClickListener(v -> onRecordClick(email, true));
        profile.setOnClickListener(v -> navigateWithUserEmail(this, Profile.class, email));
    }

    private void onRecordClick(String email, boolean showNotes) {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, email);
        intent.putExtra(NavigationExtras.NOTES, showNotes);
        startActivity(intent);
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        myApts = findViewById(R.id.myAppointmentsButton);
        bookApt = findViewById(R.id.bookAppointmentButton);
        myRecs = findViewById(R.id.myRecordsButton);
        profile = findViewById(R.id.profileButton);
    }
}
