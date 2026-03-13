package com.example.clinicflow.presentation.authScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.business.auth.AuthExceptions;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.admin.UserSignUp;

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
        setEvents();

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents() {
        loginBtn.setOnClickListener(v -> onLoginClick());
        signupBtn.setOnClickListener(v -> onSignUpClick());
    }

    private void onSignUpClick() {
        Intent intent = new Intent(MainActivity.this, UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        startActivity(intent);
    }

    private void onLoginClick() {
        String enteredEmail = email.getText().toString().trim();
        String enteredPW = password.getText().toString().trim();

        Users currUser;
        try {
            currUser = authService.authenticateOrThrow(enteredEmail, enteredPW);
            Intent intent = loginNav.sendToLanding(currUser, enteredEmail);
            if (intent != null) {
                startActivity(intent);
            }
        } catch (AuthExceptions.AuthException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setViews() {
        email = findViewById(R.id.EmailAddressEditText);
        password = findViewById(R.id.PasswordEditText);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signUpButton);
    }
}