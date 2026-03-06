package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;
import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.PatientLookupService;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;
import com.google.android.material.card.MaterialCardView;

public class ViewPatients extends AppCompatActivity{

    private MaterialCardView patientCard;
    private BasicBinds binds;
    private Button search;
    private Button viewRecords;
    private EditText emailAddress;
    private TextView email;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView hc;
    private TextView phone;

    private String userEmail;
    private PatientLookupService patientLookupService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_patients);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        patientLookupService = app.getPatientLookupService();

        setViews();

        userEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);

        setEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents() {
        search.setOnClickListener(v -> onClickSearch());
        viewRecords.setOnClickListener(v -> onClickView());
        binds.profile.setOnClickListener(v -> onClickEmail(this, Profile.class, userEmail));
        binds.back.setOnClickListener(v -> finish());
        binds.logout.setOnClickListener(v -> onLogoutClick(this));
    }
    private void onClickView() {
        Navigation.openRecords(this, emailAddress.getText().toString().trim(), userEmail);
    }

    private void onClickSearch() {
        String enteredEmail = emailAddress.getText().toString().trim();

        if(enteredEmail.isEmpty()) {
            Toast.makeText(this, "Please enter a Patient email", Toast.LENGTH_LONG).show();
            hide();
            return;
        }

        Patient patient = patientLookupService.findPatientByEmail(enteredEmail);

        if(patient == null) {
            Toast.makeText(this, "No Such Account", Toast.LENGTH_LONG).show();
            hide();
            return;
        }

        setPatient(patient);
    }

    private void setPatient(Patient patient) {
        email.setText(patient.getEmail());
        name.setText(patient.getFullName());
        gender.setText(patient.getGender());
        age.setText(String.valueOf(patient.getAge()));
        hc.setText(patient.getHealthCardNumber());
        phone.setText(patient.getPhoneNumber());

        viewRecords.setVisibility(View.VISIBLE);
        patientCard.setVisibility(View.VISIBLE);
    }

    private void hide() {
        patientCard.setVisibility(View.INVISIBLE);
        viewRecords.setVisibility(View.INVISIBLE);
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
        hc = findViewById(R.id.healthCardActual);
        phone = findViewById(R.id.phoneActual);
        emailAddress = findViewById(R.id.editTextEmailAddress);
    }
}
