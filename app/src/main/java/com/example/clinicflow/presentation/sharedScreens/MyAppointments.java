package com.example.clinicflow.presentation.sharedScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.adapters.AppointmentAdapter;
import com.example.clinicflow.presentation.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class MyAppointments extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private Button backButton;

    private AppointmentService appointmentService;
    private LookupService lookupService;

    private List<Appointment> appointments;
    private String finalEmail;
    private boolean showNotes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.appointments);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        appointmentService = app.getAppointmentService();
        lookupService = app.getLookupService();

        setViews();
        setEvents();

        String userEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        String patientEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_PATIENT_EMAIL);
        finalEmail = resolveEmail(userEmail, patientEmail);
        showNotes = getIntent().getBooleanExtra(NavigationExtras.NOTES, false);

        loadAppointments();
        BasicBinds.setWindowInsets(this);
    }

    private void setViews() {
        recyclerView = findViewById(R.id.recordsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        backButton = findViewById(R.id.backButton);
    }

    private void setEvents() {
        backButton.setOnClickListener(v -> finish());
    }

    private void loadAppointments() {
        appointments = fetchAppointments();

        List<String> doctorNames = new ArrayList<>();
        for (Appointment a : appointments) {
            doctorNames.add(lookupService.getFullName(a.getDoctorEmail()));
        }

        recyclerView.setAdapter(new AppointmentAdapter(this, appointments, doctorNames, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyStateText.setVisibility(appointments.isEmpty() ? TextView.VISIBLE : TextView.GONE);
    }

    private List<Appointment> fetchAppointments() {
        if (showNotes) {
            return appointmentService.getPastAppointmentsForPatient(finalEmail);
        }
        return appointmentService.getUpcomingAppointmentsForPatient(finalEmail);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT, appointments.get(position));
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL,
                getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL));
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL,
                getIntent().getStringExtra(NavigationExtras.EXTRA_PATIENT_EMAIL));
        intent.putExtra(NavigationExtras.NOTES, showNotes);
        startActivity(intent);
    }

    private String resolveEmail(String userEmail, String patientEmail) {
        return patientEmail != null ? patientEmail : userEmail;
    }
}
