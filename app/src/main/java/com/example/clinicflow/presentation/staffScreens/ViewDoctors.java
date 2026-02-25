package com.example.clinicflow.presentation.staffScreens;

import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.authScreens.MainActivity;

public class ViewDoctors extends AppCompatActivity{

    private ImageButton profile;
    private Button logout;
    private Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_physicians);

        setViews();

        final String email = getIntent().getStringExtra(MainActivity.EXTRA_USER_EMAIL);

        setEvents(email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents(String email) {
        logout.setOnClickListener(v -> onLogoutClick(this));
        back.setOnClickListener(v -> finish());
        profile.setOnClickListener(v -> onClickEmail(this, StaffProfile.class, email));
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        profile = findViewById(R.id.profileButton);
        back = findViewById(R.id.backButton);
    }
}
