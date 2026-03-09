package com.example.clinicflow.presentation.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.presentation.Navigation;

import java.time.LocalDate;

public class UserSignUp extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText gender;
    private EditText dob;
    private LocalDate actDob = null;
    private EditText healthCard;
    private EditText phoneNumber;

    private EditText specialization;
    private EditText licenseNumber;

    private EditText position;
    private Button signUpButton;
    private Button backButton;

    private ObjectCreation objectCreation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_user);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        objectCreation = app.getObjectCreation();

        setViews();

        UserRole role = (UserRole) getIntent().getSerializableExtra(Navigation.USER_ROLE);

        makeVisible(role);
        setEvents(role);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void makeVisible(UserRole role) {
        if (role == UserRole.DOCTOR) {
            specialization.setVisibility(View.VISIBLE);
            licenseNumber.setVisibility(View.VISIBLE);
        } else if (role == UserRole.STAFF) {
            position.setVisibility(View.VISIBLE);
        } else if (role == UserRole.PATIENT) {
            phoneNumber.setVisibility(View.VISIBLE);
            healthCard.setVisibility(View.VISIBLE);
        }
    }

    private void setEvents(UserRole role) {
        signUpButton.setOnClickListener(v -> onSignUpClick(role));
        backButton.setOnClickListener(v -> finish());
        dob.setOnClickListener(v -> onDobClick());
    }

    private void onDobClick() {
        LocalDate curr = LocalDate.now();

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (view, year, month, dayOfMonth) -> {
                    actDob = LocalDate.of(year, month + 1, dayOfMonth);
                    dob.setText(actDob.toString());
                },
                curr.getYear(),curr.getMonthValue() - 1, curr.getDayOfMonth());
        dialog.show();
    }

    private void onSignUpClick(UserRole role) {
        String first = cleanText(firstName);
        String last = cleanText(lastName);
        String emailAdd = cleanText(email);
        String pass = cleanText(password);
        String confirmPass = cleanText(confirmPassword);
        String genderStr = cleanText(gender);
        String hCardStr = null;
        String phoneStr = null;
        Specialization specializationEnum = null;
        String licenseNumberStr = null;
        String positionStr = null;


        if(!pass.equals(confirmPass)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        if (role == UserRole.PATIENT) {
            hCardStr = cleanText(healthCard);
            phoneStr = cleanText(phoneNumber);
        } else if (role == UserRole.DOCTOR) {
            specializationEnum = parseSpecialization(cleanText(specialization));
            licenseNumberStr = cleanText(licenseNumber);

            if (specializationEnum == null && !cleanText(specialization).isEmpty()) {
                Toast.makeText(this, "Invalid Specialization", Toast.LENGTH_LONG).show();
                return;
            }
        } else if (role == UserRole.STAFF) {
            positionStr = cleanText(position);
        }


        try {
            boolean added = false;

            if (role == UserRole.PATIENT) {
                added = objectCreation.addPatientToDatabase(first, last, emailAdd, pass, genderStr, actDob, hCardStr, phoneStr);
            } else if (role == UserRole.DOCTOR) {
                added = objectCreation.addDoctorToDatabase(first, last, emailAdd, pass, genderStr, actDob, specializationEnum, licenseNumberStr);
            } else if (role == UserRole.STAFF) {
                added = objectCreation.addStaffToDatabase(first, last, emailAdd, pass, genderStr, actDob, positionStr);
            }
            if(!added){
                Toast.makeText(this, "User could not be added", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(this, role + " added successfully", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Specialization parseSpecialization(String spec) {
        if (spec == null || spec.isEmpty()) return null;
        try {
            return Specialization.valueOf(spec.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String cleanText(EditText editText){
        return editText.getText().toString().trim();
    }

    private void setViews(){
        firstName = findViewById(R.id.FirsNameEditText);
        lastName = findViewById(R.id.LastNameEditText);
        email = findViewById(R.id.EmailAddressEditText);
        password = findViewById(R.id.PasswordEditText);
        confirmPassword = findViewById(R.id.PasswordConfirmEditText);
        gender = findViewById(R.id.GenderEditText);
        dob = findViewById(R.id.DobEditText);

        healthCard = findViewById(R.id.HealthCardEditText);
        phoneNumber = findViewById(R.id.PhoneEditText);

        signUpButton = findViewById(R.id.signUpButton);
        backButton = findViewById(R.id.backButton);

        specialization = findViewById(R.id.specializationEditText);
        licenseNumber = findViewById(R.id.licenseNumberEditText);
        position = findViewById(R.id.positionEditText);
    }
}
