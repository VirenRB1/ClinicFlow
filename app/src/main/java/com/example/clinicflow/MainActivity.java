package com.example.clinicflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.DoctorScreen;
import com.example.clinicflow.presentation.PatientScreen;
import com.example.clinicflow.presentation.StaffScreen;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_screen);

        AuthService authService = new AuthService();

        email = findViewById(R.id.EmailAddressEditText);
        password = findViewById(R.id.PasswordEditText);
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString();
                String enteredPW = password.getText().toString();

                Users currUser = authService.authenticate(enteredEmail, enteredPW);

                Intent intent = identifyType(currUser);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Intent identifyType(Users currUser){
        if (currUser instanceof Patient) {
            return new Intent(MainActivity.this, PatientScreen.class);
        } else if (currUser instanceof Doctor) {
            return new Intent(MainActivity.this, DoctorScreen.class);
        } else if (currUser instanceof Staff) {
            return new Intent(MainActivity.this, StaffScreen.class);
        }
        return null;
    }
}