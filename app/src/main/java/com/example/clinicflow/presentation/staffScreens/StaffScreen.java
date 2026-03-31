package com.example.clinicflow.presentation.staffScreens;

import static com.example.clinicflow.presentation.Navigation.navigateToSearchCard;
import static com.example.clinicflow.presentation.Navigation.navigateWithUserEmail;
import static com.example.clinicflow.presentation.NavigationExtras.MODE_VIEW_DOCTOR;
import static com.example.clinicflow.presentation.NavigationExtras.MODE_VIEW_PATIENT;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

public class StaffScreen extends AppCompatActivity {

    private BasicBinds binds;
    private Button manage;
    private Button viewPatients;
    private Button viewDocs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_landing);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        binds.setBasicEvents(this, email);
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        manage.setOnClickListener(v -> navigateWithUserEmail(this, ManageAppointments.class, email));
        viewPatients.setOnClickListener(v -> navigateToSearchCard(this, email, MODE_VIEW_PATIENT));
        viewDocs.setOnClickListener(v -> navigateToSearchCard(this, email, MODE_VIEW_DOCTOR));
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
        manage = findViewById(R.id.manageAppointmentsButton);
        viewPatients = findViewById(R.id.viewPatientsButton);
        viewDocs = findViewById(R.id.viewPhysiciansButton);
    }
}
