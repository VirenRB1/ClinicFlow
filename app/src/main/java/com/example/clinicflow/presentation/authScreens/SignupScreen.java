package com.example.clinicflow.presentation.authScreens;

import android.content.Intent;
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
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.presentation.patientScreens.PatientScreen;

public class SignupScreen extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;

    EditText gender;
    EditText age;
    EditText healthCard;
    EditText phoneNumber;

    Button signUpButton;
    Button backButton;

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
        String ageStr = cleanText(age);
        String hCardStr = cleanText(healthCard);
        String phoneStr = cleanText(phoneNumber);

        if (!pass.equals(confirmPass)) {
            Toast.makeText(SignupScreen.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        Integer ageNum = parse(ageStr);
        Integer hCardNum = parse(hCardStr);
        Integer phoneNum = parse(phoneStr);

        if(ageNum == null || hCardNum == null || phoneNum == null){
            Toast.makeText(SignupScreen.this, "Age, health card number, and phone number must be numbers", Toast.LENGTH_LONG).show();
            return;
        }

        boolean added = objectCreation.addPatientToDatabase(first, last, emailAdd, pass, genderStr, ageNum, hCardNum, phoneNum);

        if(!added){
            Toast.makeText(SignupScreen.this, "Patient could not be added", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(SignupScreen.this, "Patient added successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignupScreen.this, PatientScreen.class);
        intent.putExtra(MainActivity.EXTRA_USER_EMAIL, emailAdd);
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
        age = findViewById(R.id.AgeEditText);
        healthCard = findViewById(R.id.HealthCardEditText);
        phoneNumber = findViewById(R.id.PhoneEditText);
        signUpButton = findViewById(R.id.signUpButton);
        backButton = findViewById(R.id.backButton);
    }
}
