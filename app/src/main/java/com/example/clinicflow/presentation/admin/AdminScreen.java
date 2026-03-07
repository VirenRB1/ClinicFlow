package com.example.clinicflow.presentation.admin;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.Navigation;
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
        String email = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        setEvents(email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> onLogoutClick(this));
        profile.setOnClickListener(v -> onClickEmail(this, Profile.class, email));
        addDoctor.setOnClickListener(v -> onClickRole(Navigation.DOCTOR, email));
        addStaff.setOnClickListener(v -> onClickRole(Navigation.STAFF, email));
        addPatient.setOnClickListener(v -> onClickRole(Navigation.PATIENT, email));
    }

    private void onClickRole(String role, String email) {
        Intent intent = new Intent(this, AddOrDeleteScreen.class);
        intent.putExtra(Navigation.USER_ROLE, role);
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, email);
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
