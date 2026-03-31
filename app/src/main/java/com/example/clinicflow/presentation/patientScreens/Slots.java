package com.example.clinicflow.presentation.patientScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.services.TimeSlotService;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.RecyclerViewInterface;
import com.example.clinicflow.presentation.adapters.TimeSlotAdapter;

import java.time.LocalDate;
import java.util.List;

public class Slots extends AppCompatActivity implements RecyclerViewInterface {

    private List<TimeSlot> slots;
    private RecyclerView recyclerView;
    private TextView emptyStateText;

    private String actingUserEmail;
    private String patientEmail;
    private String doctorEmail;
    private LocalDate actualDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.time_slots_page);

        recyclerView = findViewById(R.id.slotsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        actingUserEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        patientEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_PATIENT_EMAIL);

        if (patientEmail == null) {
            patientEmail = actingUserEmail;
        }

        doctorEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL);
        String date = getIntent().getStringExtra(BookAppointment.DATE);

        if (doctorEmail == null || patientEmail == null || date == null) {
            Toast.makeText(this, "Invalid doctor email, patient email, or date", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        actualDate = LocalDate.parse(date);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSlots();
        BasicBinds.setWindowInsets(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSlots();
    }

    private void loadSlots() {
        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        TimeSlotService timeSlotService = app.getTimeSlotService();

        slots = timeSlotService.getAvailableTimeSlots(doctorEmail, actualDate);

        TimeSlotAdapter adapter = new TimeSlotAdapter(this, slots, this);
        recyclerView.setAdapter(adapter);

        showDetails(slots, emptyStateText);
    }

    private void showDetails(List<TimeSlot> slots, TextView emptyStateText) {
        if (slots == null || slots.isEmpty()) {
            emptyStateText.setVisibility(TextView.VISIBLE);
        } else {
            emptyStateText.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Slots.this, ConfirmAppointment.class);

        intent.putExtra(NavigationExtras.EXTRA_SLOT, slots.get(position));
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, actingUserEmail);
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, patientEmail);
        intent.putExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL, doctorEmail);
        intent.putExtra(BookAppointment.DATE, getIntent().getStringExtra(BookAppointment.DATE));

        startActivity(intent);
    }
}
