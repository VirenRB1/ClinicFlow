package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;
import static com.example.clinicflow.presentation.Navigation.onClickEmail;
import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class MySchedule extends AppCompatActivity{

    private BasicBinds binds;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_schedule);

        setViews();
        final String email = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        setEvents(email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setEvents(String email) {
        binds.logout.setOnClickListener(v -> onLogoutClick(this));
        binds.back.setOnClickListener(v -> finish());
        binds.profile.setOnClickListener(v -> onClickEmail(this, Profile.class, email));
    }

    private void setViews() {
        binds = setBasicBinds(this);
    }
}
