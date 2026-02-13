package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.PatientScreen.EXTRA_USER_EMAIL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;
import com.google.android.material.card.MaterialCardView;

public class ViewPatients extends AppCompatActivity{

    public static final String EXTRA_PATIENT_EMAIL = "patient_email";

    MaterialCardView patientCard;

    ImageButton profile;

    Button back;

    Button search;

    Button viewRecords;

    EditText emailAddress;

    TextView email;
    TextView name;
    TextView gender;
    TextView age;
    TextView hc;
    TextView phone;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_patients);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        UserRepository userRepository = app.getUserRepository();

        patientCard = findViewById(R.id.patientCard);
        viewRecords = findViewById(R.id.viewRecordsButton);
        search = findViewById(R.id.searchButton);
        profile = findViewById(R.id.profileButton);
        back = findViewById(R.id.backButton);

        email = findViewById(R.id.emailActual);
        name = findViewById(R.id.nameActual);
        gender = findViewById(R.id.genderActual);
        age = findViewById(R.id.ageActual);
        hc = findViewById(R.id.healthCardActual);
        phone = findViewById(R.id.phoneActual);

        emailAddress = findViewById(R.id.editTextEmailAddress);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = emailAddress.getText().toString().trim();

                Patient patient = userRepository.getPatientByEmail(enteredEmail);

                if (patient == null) {
                    Toast.makeText(getApplicationContext(), "No Such Account", Toast.LENGTH_LONG).show();
                    viewRecords.setVisibility(View.INVISIBLE);
                    patientCard.setVisibility(View.INVISIBLE);
                } else {
                    email.setText(patient.getEmail());
                    name.setText(patient.getFullName());
                    gender.setText(patient.getGender());
                    age.setText(String.valueOf(patient.getAge()));
                    hc.setText(String.valueOf(patient.getHealthCardNumber()));
                    phone.setText(String.valueOf(patient.getPhoneNumber()));

                    viewRecords.setVisibility(View.VISIBLE);
                    patientCard.setVisibility(View.VISIBLE);
                }
            }
        });

        viewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatients.this, MyRecords.class);
                intent.putExtra(EXTRA_PATIENT_EMAIL, emailAddress.getText().toString().trim());
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatients.this, StaffProfile.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatients.this, StaffScreen.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
