package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.patientScreens.BookAppointment;
import com.google.android.material.card.MaterialCardView;

public class SearchUserCard extends AppCompatActivity {

    private MaterialCardView patientCard;
    private BasicBinds binds;
    private Button search;
    private Button actionButton;
    private EditText emailAddress;
    private TextView email;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView healthCard;
    private TextView phone;

    private String userEmail;
    private String searchMode;
    private LookupService lookupService;
    private ObjectCreation objectCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_card);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        objectCreation = app.getObjectCreation();
        lookupService = app.getLookupService();

        userEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        searchMode = getIntent().getStringExtra(NavigationExtras.SEARCH_MODE);

        setViews();
        setEvents();
        BasicBinds.setWindowInsets(this);
    }

    private void setEvents() {
        search.setOnClickListener(v -> onClickSearch());
        actionButton.setOnClickListener(v -> onClickAction());
        binds.setBasicEvents(this, userEmail);
    }

    private void onClickAction() {
        switch (searchMode) {
            case NavigationExtras.MODE_DELETE_USER -> delete();
            case NavigationExtras.MODE_BOOK_APPOINTMENT -> bookAppointment();
            case NavigationExtras.MODE_CANCEL_APPOINTMENT -> cancelAppointment();
            default -> viewRecords();
        }
    }

    private void cancelAppointment() {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, email.getText().toString());
        intent.putExtra(NavigationExtras.NOTES, false);   // upcoming, not past
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        startActivity(intent);
    }

    private void bookAppointment() {
        Intent intent = new Intent(this, BookAppointment.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail); // staff - for top bar
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, email.getText().toString()); // patient - for booking
        startActivity(intent);
    }

    private void delete() {
        String enteredEmail = emailAddress.getText().toString().trim();

        boolean deleted = objectCreation.deleteUser(enteredEmail);

        if (!deleted) {
            Toast.makeText(this, "User Could Not Be Deleted", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "User Deleted", Toast.LENGTH_LONG).show();
        emailAddress.setText("");
        hide();
    }

    private void viewRecords() {
        Intent intent = new Intent(this, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, email.getText().toString());
        intent.putExtra(NavigationExtras.NOTES, true);
        startActivity(intent);
    }

    private void onClickSearch() {
        String enteredEmail = emailAddress.getText().toString().trim();

        if (searchMode.equals(NavigationExtras.MODE_DELETE_USER)) {
            Users user = lookupService.findUserByEmail(enteredEmail);
            if (user == null) {
                Toast.makeText(this, "No Such Account", Toast.LENGTH_LONG).show();
                hide();
            } else {
                setUser(user);
            }
        } else {
            Patient patient = lookupService.findPatientByEmail(enteredEmail);
            if (patient == null) {
                Toast.makeText(this, "No Such Patient", Toast.LENGTH_LONG).show();
                hide();
            } else {
                setPatient(patient);
            }
        }
    }

    private void setUser(Users user) {
        email.setText(user.getEmail());
        name.setText(user.getFullName());
        gender.setText(user.getGender());
        age.setText(String.valueOf(user.getAge()));
        show();
    }

    private void setPatient(Patient patient) {
        email.setText(patient.getEmail());
        name.setText(patient.getFullName());
        gender.setText(patient.getGender());
        age.setText(String.valueOf(patient.getAge()));
        healthCard.setText(patient.getHealthCardNumber());
        phone.setText(patient.getPhoneNumber());
        show();
    }

    private void show() {
        patientCard.setVisibility(View.VISIBLE);
        actionButton.setVisibility(View.VISIBLE);
    }

    private void hide() {
        patientCard.setVisibility(View.GONE);
        actionButton.setVisibility(View.GONE);
    }

    private void setViews() {
        patientCard = findViewById(R.id.patientCard);
        actionButton = findViewById(R.id.actionButton);
        search = findViewById(R.id.searchButton);
        binds = setBasicBinds(this);
        email = findViewById(R.id.emailActual);
        name = findViewById(R.id.nameActual);
        gender = findViewById(R.id.genderActual);
        age = findViewById(R.id.ageActual);
        healthCard = findViewById(R.id.healthCardActual);
        phone = findViewById(R.id.phoneActual);
        emailAddress = findViewById(R.id.editTextEmailAddress);

        TextView searchTitle = findViewById(R.id.searchSectionTitle);
        switch (searchMode) {
            case NavigationExtras.MODE_DELETE_USER -> {
                searchTitle.setText(R.string.search_by_email_user);
                actionButton.setText(R.string.delete);
                healthCard.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                findViewById(R.id.healthCard).setVisibility(View.GONE);
                findViewById(R.id.phone).setVisibility(View.GONE);
            }
            case NavigationExtras.MODE_BOOK_APPOINTMENT -> {
                searchTitle.setText(R.string.search_by_email);
                actionButton.setText(R.string.book);
            }
            case NavigationExtras.MODE_CANCEL_APPOINTMENT -> {
                searchTitle.setText(R.string.search_by_email);
                actionButton.setText(R.string.cancelApt);
            }
            default -> {
                searchTitle.setText(R.string.search_by_email);
                actionButton.setText(R.string.view_recs);
            }
        }
    }
}
