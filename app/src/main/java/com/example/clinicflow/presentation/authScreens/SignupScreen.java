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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        UserRepository userRepository = app.getUserRepository();
        ObjectCreation objectCreation = new ObjectCreation(userRepository);

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstName.getText().toString().trim();
                String last = lastName.getText().toString().trim();
                String emailAdd = email.getText().toString().trim();
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                String genderStr = gender.getText().toString().trim();
                String ageStr = age.getText().toString().trim();
                String hCardStr = healthCard.getText().toString().trim();
                String phoneStr = phoneNumber.getText().toString().trim();

                if (!pass.equals(confirmPass)) {
                    Toast.makeText(SignupScreen.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                int ageNum;
                int hCardNum;
                int phoneNum;

                try {
                    ageNum = Integer.parseInt(ageStr);
                    hCardNum = Integer.parseInt(hCardStr);
                    phoneNum = Integer.parseInt(phoneStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(SignupScreen.this, "Age, healthcard number, and phone number must be numbers", Toast.LENGTH_LONG).show();
                    return;
                }

                Boolean added = objectCreation.addPatientToDatabase(first, last, emailAdd, pass, genderStr, ageNum, hCardNum, phoneNum);
                if (added) {
                    Toast.makeText(SignupScreen.this, "Patient added successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupScreen.this, PatientScreen.class);
                    intent.putExtra("user_email", emailAdd);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupScreen.this, "Patient could not be added", Toast.LENGTH_LONG).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}
