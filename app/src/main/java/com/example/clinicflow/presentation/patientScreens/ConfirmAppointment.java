package com.example.clinicflow.presentation.patientScreens;

import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.AppointmentService;
import com.example.clinicflow.business.LookupService;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.Navigation;

import java.time.LocalDate;

public class ConfirmAppointment extends AppCompatActivity {

    private Button backButton;
    private Button confirmButton;
    private TextView name;
    private TextView email;
    private TextView startTime;
    private TextView endTime;
    private TextView dateView;
    private Button logoutButton;
    private EditText purpose;

    private String patientEmail;
    private String doctorEmail;
    private LocalDate date;
    private TimeSlot slot;

    ClinicFlowApp app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_appointment);

        app = (ClinicFlowApp) getApplication();


        setViews();

        patientEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        doctorEmail = getIntent().getStringExtra(Navigation.EXTRA_DOCTOR_EMAIL);
        String dateString = getIntent().getStringExtra(BookAppointment.DATE);
        if (dateString != null) {
            date = LocalDate.parse(dateString);
        }
        slot = (TimeSlot) getIntent().getSerializableExtra(Navigation.EXTRA_SLOT);

        setTexts();
        setEvents();

    }

    private void setTexts() {
        LookupService lookupService = app.getLookupService();
        Doctor doctor = lookupService.findDoctorByEmail(doctorEmail);
        name.setText(doctor.getFullName());
        email.setText(doctorEmail);
        startTime.setText(slot.getStartTime().toString());
        endTime.setText(slot.getEndTime().toString());
        dateView.setText(date.toString());
    }

    private void setEvents() {
        backButton.setOnClickListener(v -> finish());
        logoutButton.setOnClickListener(v -> onLogoutClick(this));
        confirmButton.setOnClickListener(v -> onConfirmClick());
    }

    private void onConfirmClick() {
        if(purpose.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter a purpose", Toast.LENGTH_LONG).show();
            return;
        }

        String STATUS = "Confirmed";
        String DEFAULT_NOTES = "";
        Appointment appointment = new Appointment(doctorEmail, patientEmail, date, slot.getStartTime(), slot.getEndTime(), STATUS, purpose.getText().toString().trim(), DEFAULT_NOTES);

        AppointmentService appointmentService = app.getAppointmentService();

        try {
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
        backButton = findViewById(R.id.backButton);
        confirmButton = findViewById(R.id.confirmButton);
        name = findViewById(R.id.nameActual);
        email = findViewById(R.id.emailActual);
        startTime = findViewById(R.id.startTimeActual);
        endTime = findViewById(R.id.endTimeActual);
        dateView = findViewById(R.id.dateActual);
        logoutButton = findViewById(R.id.logoutButton);
        purpose = findViewById(R.id.purposeEditText);
    }

}
