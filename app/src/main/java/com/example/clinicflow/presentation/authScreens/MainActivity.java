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
import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.models.Users;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginBtn;

    private Button signupBtn;

    private AuthService authService;
    private LoginNav loginNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_screen);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        authService = app.getAuthService();
        loginNav = new LoginNav(this);

        setViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();
                String enteredPW = password.getText().toString().trim();

                Users currUser = authService.authenticate(enteredEmail, enteredPW);

                if (currUser == null) {
                    Toast.makeText(getApplicationContext(), "No Such Account", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = loginNav.sendToLanding(currUser, enteredEmail);
                if (intent == null) {
                    Toast.makeText(getApplicationContext(), "Incorrect Account Type", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(intent);

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

    private void setViews() {
        email = findViewById(R.id.EmailAddressEditText);
        password = findViewById(R.id.PasswordEditText);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signUpButton);
    }
}