package com.example.clinicflow.presentation.patientScreens;

import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.authScreens.MainActivity;

public class PatientProfile extends AppCompatActivity{

    private Button logout;
    private Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_profile);

        setViews();

        final String email = getIntent().getStringExtra(MainActivity.EXTRA_USER_EMAIL);

        setEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents() {
        logout.setOnClickListener(v -> onLogoutClick(this));
        back.setOnClickListener(v -> finish());
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        back = findViewById(R.id.backButton);
    }
}
