package com.example.clinicflow.presentation;

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

import com.example.clinicflow.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.UserRepository;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginBtn;

    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_screen);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        UserRepository userRepository = app.getUserRepository();
        AuthService authService = new AuthService(userRepository);

        email = findViewById(R.id.EmailAddressEditText);
        password = findViewById(R.id.PasswordEditText);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signUpButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();
                String enteredPW = password.getText().toString().trim();

                Users currUser = authService.authenticate(enteredEmail, enteredPW);

                if (currUser == null) {
                    Toast.makeText(getApplicationContext(), "No Such Account", Toast.LENGTH_LONG).show();
                }

                Intent intent = identifyType(currUser, enteredEmail);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Intent is null", Toast.LENGTH_LONG).show();
                }

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Intent identifyType(Users currUser, String email) {
        Intent intent = null;
        if (currUser instanceof Patient) {
            intent = new Intent(MainActivity.this, PatientScreen.class);
        } else if (currUser instanceof Doctor) {
            intent = new Intent(MainActivity.this, DoctorScreen.class);
        } else if (currUser instanceof Staff) {
            intent = new Intent(MainActivity.this, StaffScreen.class);
        }

        if (intent != null) {
            intent.putExtra("user_email", email);
        }
        return intent;
    }
}