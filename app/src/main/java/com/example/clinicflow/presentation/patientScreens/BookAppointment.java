package com.example.clinicflow.presentation.patientScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.business.validators.AppointmentValidator;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.time.LocalDate;
import java.util.List;

public class BookAppointment extends AppCompatActivity {

    private BasicBinds binds;
    private EditText doctorEditText;
    private EditText dateEditText;
    private Button findSlotsButton;

    private LocalDate actualDate;

    private String patientEmail;

    private List<Doctor> doctors;

    private static final String DOCTOR = "Select Doctor";
    public static final String DATE = "date";

    private Doctor selectedDoctor;

    private AppointmentValidator validator;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_appointment);

        setViews();

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        LookupService lookupService = app.getLookupService();

        doctors = lookupService.getDoctors();
        validator = new AppointmentValidator();

        patientEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        if (patientEmail == null) {
            Toast.makeText(this, "No Patient Email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setEvents();
        BasicBinds.setWindowInsets(this);
    }

    private void setEvents() {
        binds.setBasicEvents(this, patientEmail);
        doctorEditText.setOnClickListener(v -> showDoctors());
        dateEditText.setOnClickListener(v -> showDates());
        findSlotsButton.setOnClickListener(v -> findSlots());
    }

    private void findSlots() {
        try {
            validator.validateDoctorAndDate(selectedDoctor, actualDate);
            Intent intent = new Intent(this, Slots.class);
            intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, patientEmail);
            intent.putExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL, selectedDoctor.getEmail());
            intent.putExtra(DATE, actualDate.toString());
            startActivity(intent);
        } catch (ValidationExceptions.ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDoctors() {
        String[] labels = makeLabels();

        new android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle(DOCTOR)
                .setItems(labels, (d, which) -> {
                    selectedDoctor = doctors.get(which);
                    doctorEditText.setText(selectedDoctor.getFullName());
                }).show();
    }

    private String[] makeLabels() {
        String[] labels = new String[doctors.size()];
        for (int i = 0; i < doctors.size(); i++) {
            labels[i] = doctors.get(i).getFullName();
        }
        return labels;
    }

    private void showDates() {
        LocalDate curr = LocalDate.now();

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    actualDate = LocalDate.of(year, month + 1, dayOfMonth);
                    dateEditText.setText(actualDate.toString());
                },
                curr.getYear(), curr.getMonthValue() - 1, curr.getDayOfMonth());
        dialog.show();
    }

    private void setViews() {
        binds = setBasicBinds(this);
        doctorEditText = findViewById(R.id.doctorEditText);
        dateEditText = findViewById(R.id.dateEditText);
        findSlotsButton = findViewById(R.id.findSlotsButton);
    }
}
