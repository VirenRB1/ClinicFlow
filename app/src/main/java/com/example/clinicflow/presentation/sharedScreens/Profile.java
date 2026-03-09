package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.Navigation.onLogoutClick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.LookupService;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.Navigation;
import com.google.android.material.card.MaterialCardView;

public class Profile extends AppCompatActivity {

    private Button logout;
    private Button back;

    private MaterialCardView userCard;
    private TextView email;
    private TextView name;
    private TextView gender;
    private TextView age;

    private TextView position;
    private TextView positionActual;

    private TextView specialization;
    private TextView specializationActual;

    private TextView licenseNumber;
    private TextView licenseNumberActual;

    private TextView phoneNumber;
    private TextView phoneNumberActual;

    private TextView healthCard;
    private TextView healthCardActual;

    private LookupService lookupService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        lookupService = app.getLookupService();

        setViews();

        final String userEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);

        setEvents();
        loadProfile(userEmail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadProfile(String userEmail) {
        if(userEmail == null || userEmail.trim().isEmpty()){
            Toast.makeText(this, "No user email provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Users user = lookupService.findUserByEmail(userEmail);
        if(user == null){
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setCommonInfo(user);
        setRoleInfo(user);

        userCard.setVisibility(TextView.VISIBLE);
    }

    private void setRoleInfo(Users user) {
        UserRole role = user.getRole();

        if(role == UserRole.DOCTOR) {
            Doctor doc = (Doctor) user;

            specialization.setVisibility(TextView.VISIBLE);
            specializationActual.setVisibility(TextView.VISIBLE);
            licenseNumber.setVisibility(TextView.VISIBLE);
            licenseNumberActual.setVisibility(TextView.VISIBLE);

            specializationActual.setText(String.valueOf(doc.getSpecialization()));
            licenseNumberActual.setText(doc.getLicenseNumber());
        } else if (role == UserRole.STAFF) {
            Staff staff = (Staff) user;

            position.setVisibility(TextView.VISIBLE);
            positionActual.setVisibility(TextView.VISIBLE);

            positionActual.setText(staff.getPosition());
        } else if (role == UserRole.PATIENT) {
            Patient patient = (Patient) user;

            healthCard.setVisibility(TextView.VISIBLE);
            healthCardActual.setVisibility(TextView.VISIBLE);
            phoneNumber.setVisibility(TextView.VISIBLE);
            phoneNumberActual.setVisibility(TextView.VISIBLE);

            healthCardActual.setText(patient.getHealthCardNumber());
            phoneNumberActual.setText(patient.getPhoneNumber());
        }
    }

    private void setCommonInfo(Users user) {
        email.setText(user.getEmail());
        name.setText(user.getFullName());
        gender.setText(user.getGender());
        age.setText(String.valueOf(user.getAge()));
        hideFields();
    }

    private void hideFields() {
        position.setVisibility(TextView.GONE);
        positionActual.setVisibility(TextView.GONE);

        specialization.setVisibility(TextView.GONE);
        specializationActual.setVisibility(TextView.GONE);

        licenseNumber.setVisibility(TextView.GONE);
        licenseNumberActual.setVisibility(TextView.GONE);

        phoneNumber.setVisibility(TextView.GONE);
        phoneNumberActual.setVisibility(TextView.GONE);

        healthCard.setVisibility(TextView.GONE);
        healthCardActual.setVisibility(TextView.GONE);
    }

    private void setEvents() {
        logout.setOnClickListener(v -> onLogoutClick(this));
        back.setOnClickListener(v -> finish());
    }

    private void setViews() {
        logout = findViewById(R.id.logoutButton);
        back = findViewById(R.id.backButton);

        userCard = findViewById(R.id.userCard);
        email = findViewById(R.id.emailActual);
        name = findViewById(R.id.nameActual);
        gender = findViewById(R.id.genderActual);
        age = findViewById(R.id.ageActual);

        position = findViewById(R.id.position);
        positionActual = findViewById(R.id.positionActual);

        specialization = findViewById(R.id.specialization);
        specializationActual = findViewById(R.id.specializationActual);

        licenseNumber = findViewById(R.id.licenseNumber);
        licenseNumberActual = findViewById(R.id.licenseActual);

        phoneNumber = findViewById(R.id.phone);
        phoneNumberActual = findViewById(R.id.phoneActual);

        healthCard = findViewById(R.id.healthCard);
        healthCardActual = findViewById(R.id.healthCardActual);
    }
}
