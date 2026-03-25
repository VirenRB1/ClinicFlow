package com.example.clinicflow.presentation.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

public class AdminScreen extends AppCompatActivity {
    private BasicBinds binds;
    private Button addDoctor;
    private Button addStaff;
    private Button addPatient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_landing);

        setViews();
        String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        binds.setBasicEvents(this, email);
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        addDoctor.setOnClickListener(v -> navigationWithRole(UserRole.DOCTOR, email));
        addStaff.setOnClickListener(v -> navigationWithRole(UserRole.STAFF, email));
        addPatient.setOnClickListener(v -> navigationWithRole(UserRole.PATIENT, email));
    }

    private void navigationWithRole(UserRole role, String email) {
        Intent intent = new Intent(this, AddOrDeleteScreen.class);
        intent.putExtra(NavigationExtras.USER_ROLE, role);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, email);
        startActivity(intent);
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
        addDoctor = findViewById(R.id.addDoctorButton);
        addStaff = findViewById(R.id.addStaffButton);
        addPatient = findViewById(R.id.addPatientButton);
    }
}
