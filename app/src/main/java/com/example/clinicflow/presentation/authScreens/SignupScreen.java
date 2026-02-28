package com.example.clinicflow.presentation.authScreens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.patientScreens.PatientScreen;

import java.time.LocalDate;

public class SignupScreen extends AppCompatActivity {

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
    private Button signUpButton;
    private Button backButton;

    private ObjectCreation objectCreation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        objectCreation = app.getObjectCreation();

        setViews();
        setEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void setEvents() {
        signUpButton.setOnClickListener(v -> onSignUpClick());
        backButton.setOnClickListener(v -> onBackClick());
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

    private void onBackClick() {
        finish();
    }

    private void onSignUpClick() {
        String first = cleanText(firstName);
        String last = cleanText(lastName);
        String emailAdd = cleanText(email);
        String pass = cleanText(password);
        String confirmPass = cleanText(confirmPassword);
        String genderStr = cleanText(gender);
        String hCardStr = cleanText(healthCard);
        String phoneStr = cleanText(phoneNumber);

        if (!pass.equals(confirmPass)) {
            Toast.makeText(SignupScreen.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        if(actDob == null) {
            Toast.makeText(SignupScreen.this, "Date of birth must be set", Toast.LENGTH_LONG).show();
            return;
        }

        Integer hCardNum = parse(hCardStr);
        Integer phoneNum = parse(phoneStr);

        if(hCardNum == null || phoneNum == null) {
            Toast.makeText(SignupScreen.this, "Health card number, and Phone number must be numbers", Toast.LENGTH_LONG).show();
            return;
        }

        boolean added = objectCreation.addPatientToDatabase(first, last, emailAdd, pass, genderStr, actDob, hCardNum, phoneNum);

        if(!added){
            Toast.makeText(SignupScreen.this, "Patient could not be added", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(SignupScreen.this, "Patient added successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignupScreen.this, PatientScreen.class);
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, emailAdd);
        startActivity(intent);
    }

    private String cleanText(EditText editText){
        return editText.getText().toString().trim();
    }

    private Integer parse(String text){
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
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
    }
}
