package com.example.clinicflow.presentation.sharedScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.AppointmentService;
import com.example.clinicflow.business.LookupService;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.adapters.AppointmentAdapter;
import com.example.clinicflow.presentation.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class MyRecords extends AppCompatActivity implements RecyclerViewInterface {
    List<Appointment> appointments;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_records);

        RecyclerView recyclerView = findViewById(R.id.recordsRecyclerView);
        TextView emptyStateText = findViewById(R.id.emptyStateText);
        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        String userEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        String patientEmail = getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL);
        String finalEmail = actEmail(userEmail, patientEmail);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        AppointmentService appointmentService = app.getAppointmentService();
        LookupService lookupService = app.getLookupService();


        List <String> doctorNames = findNames(lookupService);

        boolean showNotes = getIntent().getBooleanExtra(Navigation.NOTES, false);
        // Since we are only showing notes when accessing to view past appointments
        appointments = apptsToShow(showNotes, appointmentService, finalEmail);


        AppointmentAdapter adapter = new AppointmentAdapter(this,appointments, doctorNames, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showDetails(appointments, emptyStateText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private List<Appointment> apptsToShow(boolean showNotes, AppointmentService appointmentService, String finalEmail) {
        List<Appointment> appointments;

        if(showNotes) {
            appointments = appointmentService.getPastAppointmentsForPatient(finalEmail);
        } else {
            appointments = appointmentService.getUpcomingAppointmentsForPatient(finalEmail);
        }

        return appointments;
    }

    private List<String> findNames(LookupService lookupService) {
        List<String> names = new ArrayList<>();

        for(Appointment a : appointments) {
            names.add(lookupService.getFullName(a.getDoctorEmail()));
        }
            return names;
    }

    private void showDetails(List<Appointment> appointments, TextView emptyStateText) {
        if (appointments == null || appointments.isEmpty()) {
            emptyStateText.setVisibility(TextView.VISIBLE);
        } else {
            emptyStateText.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onRecordClick(int position) {
        Intent intent = new Intent(MyRecords.this, AppointmentDetail.class);

        intent.putExtra(Navigation.EXTRA_APPT, appointments.get(position));
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL));
        intent.putExtra(Navigation.NOTES, getIntent().getBooleanExtra(Navigation.NOTES, false));
        if(getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL) != null){
            intent.putExtra(Navigation.EXTRA_PATIENT_EMAIL, getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL));
        }

        startActivity(intent);
    }

    public String actEmail(String userEmail, String patientEmail){
        if(patientEmail != null){
            return patientEmail;
        }
        return userEmail;
    }
}
