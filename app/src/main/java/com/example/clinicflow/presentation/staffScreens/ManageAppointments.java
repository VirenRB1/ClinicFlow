package com.example.clinicflow.presentation.staffScreens;

import static com.example.clinicflow.presentation.Navigation.navigateToSearchCard;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

public class ManageAppointments extends AppCompatActivity {

    private BasicBinds binds;
    private Button book;
    private Button cancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.manage_appointments);

        setViews();

        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents(String email) {
        binds.setBasicEvents(this, email);
        book.setOnClickListener(v -> navigateToSearchCard(this, email, NavigationExtras.MODE_BOOK_APPOINTMENT));
        cancel.setOnClickListener(v -> navigateToSearchCard(this, email, NavigationExtras.MODE_CANCEL_APPOINTMENT));
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
        book = findViewById(R.id.bookAppointmentButton);
        cancel = findViewById(R.id.cancelAppointmentButton);
    }
}
