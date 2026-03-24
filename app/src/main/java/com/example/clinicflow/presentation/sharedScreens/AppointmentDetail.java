package com.example.clinicflow.presentation.sharedScreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppointmentDetail extends AppCompatActivity {
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault());
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());

    private TextView doctorName;
    private TextView date;
    private TextView patientName;
    private TextView purpose;
    private TextView noteTitle;
    private EditText doctorNote;
    private TextView startTime;
    private TextView endTime;
    private Button cancelBtn;
    private Button backBtn;
    private Button completeBtn;
    private boolean doctorView;
    private boolean showNotes;
    private ScrollView doctorNoteContainer;
    private Appointment appointment;
    private LookupService lookupService;
    private AppointmentService appointmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.appointment_detail);
        BasicBinds.setWindowInsets(this);

        getServices();
        setDetailViews();

        appointment = (Appointment) getIntent().getSerializableExtra(NavigationExtras.EXTRA_APPT);
        doctorView = getIntent().getBooleanExtra(NavigationExtras.DOCTOR_VIEW, false);
        showNotes = getIntent().getBooleanExtra(NavigationExtras.NOTES, false);

        setEvents();

        if (appointment != null) {
            setDetails(appointment, showNotes, doctorView);
            setButtonsAndFields();
        }
    }

    private void setButtonsAndFields() {
        if (doctorView) {
            cancelBtn.setVisibility(Button.GONE);
            completeBtn.setVisibility(Button.VISIBLE);
            doctorNote.setEnabled(true);
        }

        if(AppointmentStatus.COMPLETED.equals(appointment.getStatus())){
            cancelBtn.setVisibility(Button.GONE);
        }
    }

    private void getServices() {
        lookupService = ((ClinicFlowApp) getApplication()).getLookupService();
        appointmentService = ((ClinicFlowApp) getApplication()).getAppointmentService();
    }

    private void setEvents() {
        cancelBtn.setOnClickListener(v -> cancelOnClick());
        backBtn.setOnClickListener(v -> finish());
        completeBtn.setOnClickListener(v -> completeOnClick());
    }

    private void completeOnClick() {
        String notes = doctorNote.getText().toString();
        appointmentService.completeAppointment(appointment, notes);
        Toast.makeText(this, "Appointment Completed", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void cancelOnClick() {
        try {
            appointmentService.cancelAppointment(appointment);
            Toast.makeText(this, "Appointment Cancelled", Toast.LENGTH_SHORT).show();
            finish();
        } catch (ValidationExceptions.ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserName(String email){
        return lookupService.getFullName(email);
    }

    private void setDetails(Appointment record, boolean show, boolean doctorView) {
        doctorName.setText(getUserName(record.getDoctorEmail()));
        startTime.setText(timeFmt.format(record.getStartTime()));
        endTime.setText(timeFmt.format(record.getEndTime()));
        date.setText(dateFmt.format(record.getAppointmentDate()));
        patientName.setText(getUserName(record.getPatientEmail()));
        purpose.setText(record.getPatientPurpose());

        if (doctorView) {
            noteTitle.setVisibility(TextView.VISIBLE);
            doctorNoteContainer.setVisibility(ScrollView.VISIBLE);
            doctorNote.setVisibility(TextView.VISIBLE);
            doctorNote.setText(record.getDoctorNotes());
            doctorNote.setEnabled(true);
            doctorNote.setFocusable(true);
            doctorNote.setFocusableInTouchMode(true);
            doctorNote.setCursorVisible(true);
            doctorNote.setLongClickable(true);
            return;
        }

        if (!show) {
            noteTitle.setVisibility(TextView.GONE);
            doctorNoteContainer.setVisibility(ScrollView.GONE);
            doctorNote.setVisibility(TextView.GONE);
        } else {
            noteTitle.setVisibility(TextView.VISIBLE);
            doctorNoteContainer.setVisibility(ScrollView.VISIBLE);
            doctorNote.setVisibility(TextView.VISIBLE);
            doctorNote.setText(record.getDoctorNotes());
            doctorNote.setEnabled(true);
            doctorNote.setFocusable(false);
            doctorNote.setFocusableInTouchMode(false);
            doctorNote.setCursorVisible(false);
            doctorNote.setLongClickable(false);
        }
    }

    private void setDetailViews() {
        doctorName = findViewById(R.id.detailDoctorName);
        date = findViewById(R.id.detailDate);
        patientName = findViewById(R.id.detailPatientName);
        purpose = findViewById(R.id.detailPurpose);
        doctorNote = findViewById(R.id.detailDoctorNote);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        noteTitle = findViewById(R.id.detailDoctorNoteTitle);
        cancelBtn = findViewById(R.id.cancelButton);
        backBtn = findViewById(R.id.backButton);
        completeBtn = findViewById(R.id.completeButton);
        doctorNoteContainer = findViewById(R.id.doctorNoteContainer);
    }

}
