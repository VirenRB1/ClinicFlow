package com.example.clinicflow.presentation.staffScreens;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

public class ViewDoctors extends AppCompatActivity {

    private BasicBinds binds;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_physicians);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        binds.setBasicEvents(this, email);
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
    }
}
