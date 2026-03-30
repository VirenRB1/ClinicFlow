package com.example.clinicflow.presentation.patientScreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.business.validators.AppointmentValidator;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.time.LocalDate;

public class ConfirmAppointment extends AppCompatActivity {
    private BasicBinds binds;
    private Button confirmButton;
    private TextView name;
    private TextView email;
    private TextView startTime;
    private TextView endTime;
    private TextView dateView;
    private EditText purpose;

    private String actingUserEmail;
    private String patientEmail;
    private String doctorEmail;
    private LocalDate date;
    private TimeSlot slot;

    private ClinicFlowApp app;
    private AppointmentValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_appointment);

        app = (ClinicFlowApp) getApplication();
        validator = new AppointmentValidator();

        setViews();

        actingUserEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        patientEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_PATIENT_EMAIL);
        doctorEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL);
        String dateString = getIntent().getStringExtra(BookAppointment.DATE);
        slot = getIntent().getSerializableExtra(NavigationExtras.EXTRA_SLOT, TimeSlot.class);

        if (patientEmail == null) {
            patientEmail = actingUserEmail;
        }

        if (actingUserEmail == null || doctorEmail == null || dateString == null || slot == null) {
            Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        date = LocalDate.parse(dateString);

        setTexts();
        binds.setBasicEvents(this, actingUserEmail);
        setEvents();
        BasicBinds.setWindowInsets(this);
    }

    private void setTexts() {
        LookupService lookupService = app.getLookupService();
        Doctor doctor = lookupService.findDoctorByEmail(doctorEmail);

        if (doctor == null) {
            Toast.makeText(this, "Invalid doctor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        name.setText(doctor.getFullName());
        email.setText(doctorEmail);
        startTime.setText(slot.getStartTime().toString());
        endTime.setText(slot.getEndTime().toString());
        dateView.setText(date.toString());
    }

    private void setEvents() {
        confirmButton.setOnClickListener(v -> onConfirmClick());
    }

    private void onConfirmClick() {
        try {
            String purposeText = purpose.getText().toString().trim();

            validator.validateAppointmentConfirmation(patientEmail, doctorEmail, date, slot, purposeText);

            AppointmentService appointmentService = app.getAppointmentService();
            appointmentService.bookAppointment(doctorEmail, patientEmail, date, slot, purposeText);

            Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_LONG).show();
            finish();
        } catch (ValidationExceptions.AppointmentConflictException e) {
            Toast.makeText(this, "Appointment conflicts with existing appointment", Toast.LENGTH_LONG).show();
        } catch (ValidationExceptions.ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);
        confirmButton = findViewById(R.id.confirmButton);
        name = findViewById(R.id.nameActual);
        email = findViewById(R.id.emailActual);
        startTime = findViewById(R.id.startTimeActual);
        endTime = findViewById(R.id.endTimeActual);
        dateView = findViewById(R.id.dateActual);
        purpose = findViewById(R.id.purposeEditText);
    }
}