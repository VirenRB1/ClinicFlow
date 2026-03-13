package com.example.clinicflow.presentation.doctorScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.DocAvailabilityService;
import com.example.clinicflow.business.validation.ValidationExceptions;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.time.LocalTime;
import java.util.Locale;

public class SetAvailability extends AppCompatActivity{

    private BasicBinds binds;
    private Button submit;
    private EditText dayOfWeek;
    private EditText startTime;
    private EditText endTime;

    private static final String START_TIME = "Select Start Time";
    private static final String END_TIME = "Select End Time";
    private static final String DAY = "Select Day";
    private static final String[] DAYS = {
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    private static final Integer[] TIME_OPTIONS = {
            8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
    };

    private int selectedDay;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;

    private DocAvailabilityService doctorAvailabilityService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.set_availability);

        doctorAvailabilityService = ((ClinicFlowApp) getApplication()).getDoctorAvailabilityService();

        setViews();
        final String email = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        setSpinners();
        setEvents(email);

        BasicBinds.setWindowInsets(this);
    }

    private void setSpinners() {
        dayOfWeek.setOnClickListener(v -> showDayDropDown());
        startTime.setOnClickListener(v -> showTimeDropDown(START_TIME, startTime, true));
        endTime.setOnClickListener(v -> showTimeDropDown(END_TIME, endTime, false));
    }

    private void showTimeDropDown(String title, EditText time, boolean isStart) {
        String [] labels = makeLabels();
        new android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle(title)
                .setItems(labels, (d, which) -> {
                    LocalTime selected = LocalTime.of(TIME_OPTIONS[which], 0);
                    if (isStart) {
                        selectedStartTime = selected;
                    } else {
                        selectedEndTime = selected;
                    }
                    time.setText(selected.toString());
                }).show();
    }

    private String[] makeLabels() {
        String [] labels = new String[TIME_OPTIONS.length];
        for (int i = 0; i < TIME_OPTIONS.length; i++) {
            labels[i] = String.format(Locale.getDefault(), "%02d:00", TIME_OPTIONS[i]);
        }
        return labels;
    }

    private void showDayDropDown() {
        new android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle(DAY)
                .setItems(DAYS, (dialog, which) -> {
                        selectedDay = which + 1;
                        dayOfWeek.setText(DAYS[which]);
        }).show();
    }

    private void setEvents(String email) {
        binds.setBasicEvents(this, email);
        submit.setOnClickListener(v -> onSubmitClick(email));
    }

    private void onSubmitClick(String email) {
        DoctorAvailability doctorAvailability = new DoctorAvailability(
                email,
                selectedDay,
                selectedStartTime,
                selectedEndTime
        );

        try {
            doctorAvailabilityService.addDoctorAvailability(doctorAvailability);
            Toast.makeText(this, "Availability added successfully", Toast.LENGTH_LONG).show();
        } catch (ValidationExceptions.AvailabilityOverlapException e) {
            Toast.makeText(this, "Availability overlaps with an existing one", Toast.LENGTH_LONG).show();
        } catch (ValidationExceptions.ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setViews() {
        binds = setBasicBinds(this);
        submit = findViewById(R.id.submitButton);
        dayOfWeek = findViewById(R.id.dayEditText);
        startTime = findViewById(R.id.startTimeEditText);
        endTime = findViewById(R.id.endTimeEditText);
    }
}
