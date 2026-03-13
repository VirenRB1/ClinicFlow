package com.example.clinicflow.presentation.admin;

import static com.example.clinicflow.presentation.Navigation.navigateWithUserEmail;
import static com.example.clinicflow.presentation.Navigation.logoutToMain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class AdminScreen extends AppCompatActivity {
    private Button logout;
    private ImageButton profile;
    private Button addDoctor;
    private Button addStaff;
    private Button addPatient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_landing);

        setViews();
        String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> logoutToMain(this));
        profile.setOnClickListener(v -> navigateWithUserEmail(this, Profile.class, email));
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
        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        addDoctor = findViewById(R.id.addDoctorButton);
        addStaff = findViewById(R.id.addStaffButton);
        addPatient = findViewById(R.id.addPatientButton);
    }
}
