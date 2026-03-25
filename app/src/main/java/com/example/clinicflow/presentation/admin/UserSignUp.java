package com.example.clinicflow.presentation.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

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

        UserRole role = getIntent().getSerializableExtra(NavigationExtras.USER_ROLE, UserRole.class);

        makeVisible(role);
        setEvents(role);

        BasicBinds.setWindowInsets(this);
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
                (view, year, month, dayOfMonth) -> {
                    actDob = LocalDate.of(year, month + 1, dayOfMonth);
                    dob.setText(actDob.toString());
                },
                curr.getYear(), curr.getMonthValue() - 1, curr.getDayOfMonth());
        dialog.show();
    }

    private void onSignUpClick(UserRole role) {
        try {
            boolean added = addUserByRole(role);
            if (added) {
                Toast.makeText(this, role + " added successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "User could not be added", Toast.LENGTH_LONG).show();
            }
        } catch (ValidationExceptions.ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean addUserByRole(UserRole role) throws ValidationExceptions.ValidationException {
        String first = cleanText(firstName);
        String last = cleanText(lastName);
        String emailAdd = cleanText(email);
        String pass = cleanText(password);
        String confirm = cleanText(confirmPassword);
        String genderStr = cleanText(gender);

        if (!pass.equals(confirm)) {
            throw new ValidationExceptions.ValidationException("Passwords do not match");
        }

        if (role == UserRole.PATIENT) {
            return objectCreation.addPatientToDatabase(
                    first, last, emailAdd, pass, genderStr, actDob,
                    cleanText(healthCard), cleanText(phoneNumber));
        } else if (role == UserRole.DOCTOR) {
            return objectCreation.addDoctorToDatabase(
                    first, last, emailAdd, pass, genderStr, actDob,
                    parseSpecialization(cleanText(specialization)), cleanText(licenseNumber));
        } else if (role == UserRole.STAFF) {
            return objectCreation.addStaffToDatabase(
                    first, last, emailAdd, pass, genderStr, actDob, cleanText(position));
        }
        return false;
    }

    private Specialization parseSpecialization(String spec) {
        if (spec == null || spec.isEmpty())
            return null;
        try {
            return Specialization.valueOf(spec.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String cleanText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setViews() {
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
