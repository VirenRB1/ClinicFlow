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
import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.validators.AppointmentValidator;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.time.LocalDate;

public class ConfirmAppointment extends AppCompatActivity {

    private static final AppointmentStatus STATUS = AppointmentStatus.CONFIRMED;
    private static final String DEFAULT_NOTES = "";
    private BasicBinds binds;
    private Button confirmButton;
    private TextView name;
    private TextView email;
    private TextView startTime;
    private TextView endTime;
    private TextView dateView;
    private EditText purpose;

    private String patientEmail;
    private String doctorEmail;
    private LocalDate date;
    private TimeSlot slot;

    private ClinicFlowApp app;
    private AppointmentValidator validator;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_appointment);

        app = (ClinicFlowApp) getApplication();
        validator = new AppointmentValidator();

        setViews();

        patientEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        doctorEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL);
        String dateString = getIntent().getStringExtra(BookAppointment.DATE);
        slot = getIntent().getSerializableExtra(NavigationExtras.EXTRA_SLOT, TimeSlot.class);

        if (patientEmail == null || doctorEmail == null || dateString == null || slot == null) {
            Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        date = LocalDate.parse(dateString);

        setTexts();
        binds.setBasicEvents(this, patientEmail);
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
            validator.validateAppointmentConfirmation(patientEmail, doctorEmail, date, slot, purpose.getText().toString().trim());
            Appointment appointment = new Appointment(doctorEmail, patientEmail, date, slot.getStartTime(),
                    slot.getEndTime(), STATUS, purpose.getText().toString().trim(), DEFAULT_NOTES);
            AppointmentService appointmentService = app.getAppointmentService();

            appointmentService.bookAppointment(appointment);
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
