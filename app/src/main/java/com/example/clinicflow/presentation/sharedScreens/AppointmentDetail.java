package com.example.clinicflow.presentation.sharedScreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;

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
    private TextView doctorNote;
    private TextView startTime;
    private TextView endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.appointment_detail);

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> finish());

        Appointment record = (Appointment) getIntent().getSerializableExtra(Navigation.EXTRA_APPT);
        if (record == null) {
            Toast.makeText(this, "No Appointment provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        boolean showNotes = getIntent().getBooleanExtra(Navigation.NOTES, false);
        setDetailViews();
        setDetails(record, showNotes);
        BasicBinds.setWindowInsets(this);
    }
    private String getUserName(String email){
        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        return app.getLookupService().getFullName(email);
    }

    private void setDetails(Appointment record, boolean show) {
        doctorName.setText(getUserName(record.getDoctorEmail()));
        startTime.setText(timeFmt.format(record.getStartTime()));
        endTime.setText(timeFmt.format(record.getEndTime()));
        date.setText(dateFmt.format(record.getAppointmentDate()));
        patientName.setText(getUserName(record.getPatientEmail()));
        purpose.setText(record.getPatientPurpose());

        if (!show) {
            noteTitle.setVisibility(TextView.GONE);
            doctorNote.setVisibility(TextView.GONE);
        } else {
            noteTitle.setVisibility(TextView.VISIBLE);
            doctorNote.setVisibility(TextView.VISIBLE);
            doctorNote.setText(record.getDoctorNotes());
        }
        doctorNote.setText(record.getDoctorNotes());
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
    }

}
