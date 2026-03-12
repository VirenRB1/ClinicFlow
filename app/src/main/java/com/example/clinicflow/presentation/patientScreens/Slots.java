package com.example.clinicflow.presentation.patientScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.AppointmentService;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.RecyclerViewInterface;
import com.example.clinicflow.presentation.adapters.TimeSlotAdapter;

import java.time.LocalDate;
import java.util.List;

public class Slots extends AppCompatActivity implements RecyclerViewInterface {

    private List<TimeSlot> slots;
    private RecyclerView recyclerView;
    private TextView emptyStateText;

    private String patientEmail;
    private String doctorEmail;
    private LocalDate actualDate;
    private AppointmentService appointmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.time_slots_page);

        recyclerView = findViewById(R.id.slotsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        patientEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        doctorEmail = getIntent().getStringExtra(Navigation.EXTRA_DOCTOR_EMAIL);
        String date = getIntent().getStringExtra(BookAppointment.DATE);
        if (date != null) {
            actualDate = LocalDate.parse(date);
        }

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        appointmentService = app.getAppointmentService();

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
        if (doctorEmail != null && actualDate != null) {
            slots = appointmentService.getAvailableTimeSlots(doctorEmail, actualDate);

            TimeSlotAdapter adapter = new TimeSlotAdapter(this, slots, this);
            recyclerView.setAdapter(adapter);

            showDetails(slots, emptyStateText);
        }
    }

    private void showDetails(List<TimeSlot> slots, TextView emptyStateText) {
        if (slots == null || slots.isEmpty()) {
            emptyStateText.setVisibility(TextView.VISIBLE);
        } else {
            emptyStateText.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onRecordClick(int position) {
        Intent intent = new Intent(Slots.this, ConfirmAppointment.class);

        intent.putExtra(Navigation.EXTRA_SLOT, slots.get(position));
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, patientEmail);
        intent.putExtra(Navigation.EXTRA_DOCTOR_EMAIL, doctorEmail);
        intent.putExtra(BookAppointment.DATE, getIntent().getStringExtra(BookAppointment.DATE));

        startActivity(intent);
    }
}
