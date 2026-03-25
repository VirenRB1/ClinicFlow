package com.example.clinicflow.presentation.patientScreens;

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

public class PatientScreen extends AppCompatActivity {

    private BasicBinds binds;
    private Button myApts;
    private Button bookApt;
    private Button myRecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_landing);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        binds.setBasicEvents(this, email);
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        myApts.setOnClickListener(v -> onRecordClick(email, false));
        bookApt.setOnClickListener(v -> navigateWithUserEmail(this, BookAppointment.class, email));
        myRecs.setOnClickListener(v -> onRecordClick(email, true));
    }

    private void onRecordClick(String email, boolean showNotes) {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, email);
        intent.putExtra(NavigationExtras.NOTES, showNotes);
        startActivity(intent);
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
        myApts = findViewById(R.id.myAppointmentsButton);
        bookApt = findViewById(R.id.bookAppointmentButton);
        myRecs = findViewById(R.id.myRecordsButton);
    }
}
