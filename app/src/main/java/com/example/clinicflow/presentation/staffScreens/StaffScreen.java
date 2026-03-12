package com.example.clinicflow.presentation.staffScreens;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.sharedScreens.Profile;
import com.example.clinicflow.presentation.sharedScreens.ViewPatients;

public class StaffScreen extends AppCompatActivity {

    private Button logout;
    private ImageButton profile;
    private Button manage;
    private Button viewPatients;
    private Button viewDocs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_landing);

        setViews();

        final String email = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> onLogoutClick(this));
        profile.setOnClickListener(v -> onClickEmail(this, Profile.class, email));
        manage.setOnClickListener(v -> onClickEmail(this, ManageAppointments.class, email));
        viewPatients.setOnClickListener(v -> onClickEmail(this, ViewPatients.class, email));
        viewDocs.setOnClickListener(v -> onClickEmail(this, ViewDoctors.class, email));
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        manage = findViewById(R.id.manageAppointmentsButton);
        viewPatients = findViewById(R.id.viewPatientsButton);
        viewDocs = findViewById(R.id.viewPhysiciansButton);
    }
}
