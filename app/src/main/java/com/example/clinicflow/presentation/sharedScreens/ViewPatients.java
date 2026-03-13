package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.LookupService;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.google.android.material.card.MaterialCardView;

public class ViewPatients extends AppCompatActivity {

    private MaterialCardView patientCard;
    private BasicBinds binds;
    private Button search;
    private Button viewRecords;
    private EditText emailAddress;
    private TextView email;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView healthCard;
    private TextView phone;

    private String userEmail;
    private LookupService lookupService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_patients);

        lookupService = ((ClinicFlowApp) getApplication()).getLookupService();

        setViews();
        userEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        setEvents();

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents() {
        search.setOnClickListener(v -> onClickSearch());
        viewRecords.setOnClickListener(v -> onClickView());
        binds.setBasicEvents(this, userEmail);
    }

    private void onClickSearch() {
        Patient patient = lookupService.findPatientByEmail(emailAddress.getText().toString().trim());
        if (patient != null) {
            setPatient(patient);
        } else {
            hide();
        }
    }

    private void onClickView() {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, email.getText().toString());
        intent.putExtra(NavigationExtras.NOTES, true);
        startActivity(intent);
    }

    private void setPatient(Patient patient) {
        email.setText(patient.getEmail());
        name.setText(patient.getFullName());
        gender.setText(patient.getGender());
        age.setText(String.valueOf(patient.getAge()));
        healthCard.setText(patient.getHealthCardNumber());
        phone.setText(patient.getPhoneNumber());
        viewRecords.setVisibility(View.VISIBLE);
        patientCard.setVisibility(View.VISIBLE);
    }

    private void hide() {
        patientCard.setVisibility(View.GONE);
        viewRecords.setVisibility(View.GONE);
    }

    private void setViews() {
        patientCard = findViewById(R.id.patientCard);
        viewRecords = findViewById(R.id.viewRecordsButton);
        search = findViewById(R.id.searchButton);
        binds = setBasicBinds(this);
        email = findViewById(R.id.emailActual);
        name = findViewById(R.id.nameActual);
        gender = findViewById(R.id.genderActual);
        age = findViewById(R.id.ageActual);
        healthCard = findViewById(R.id.healthCardActual);
        phone = findViewById(R.id.phoneActual);
        emailAddress = findViewById(R.id.editTextEmailAddress);
    }
}
