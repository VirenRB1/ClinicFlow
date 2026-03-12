package com.example.clinicflow.presentation.patientScreens;

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
import com.example.clinicflow.presentation.sharedScreens.MyRecords;
import com.example.clinicflow.presentation.sharedScreens.Profile;

public class PatientScreen extends AppCompatActivity {

    private Button logout;
    private Button myApts;
    private Button bookApt;
    private Button myRecs;
    private ImageButton profile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_landing);

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
        logout.setOnClickListener(v -> onLogoutClick(this));
        myApts.setOnClickListener(v -> onRecordClick(email, false));
        bookApt.setOnClickListener(v -> onClickEmail(this, BookAppointment.class, email));
        myRecs.setOnClickListener(v -> onRecordClick(email, true));
        profile.setOnClickListener(v -> onClickEmail(this, Profile.class, email));
    }

    private void onRecordClick(String email, boolean showNotes) {
        Intent intent = new Intent(this, MyRecords.class);
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, email);
        intent.putExtra(Navigation.NOTES, showNotes);
        startActivity(intent);
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        myApts = findViewById(R.id.myAppointmentsButton);
        bookApt = findViewById(R.id.bookAppointmentButton);
        myRecs = findViewById(R.id.myRecordsButton);
        profile = findViewById(R.id.profileButton);
    }
}
