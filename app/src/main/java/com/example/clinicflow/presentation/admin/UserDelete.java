package com.example.clinicflow.presentation.admin;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.LookupService;
import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.google.android.material.card.MaterialCardView;

public class UserDelete extends AppCompatActivity{

    private MaterialCardView patientCard;
    private BasicBinds binds;
    private Button search;
    private Button delete;
    private EditText emailAddress;
    private TextView email;
    private TextView name;
    private TextView gender;
    private TextView age;
    private String userEmail;
    private LookupService lookupService;
    private ObjectCreation objectCreation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.delete_user);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        lookupService = app.getLookupService();
        objectCreation = app.getObjectCreation();

        setViews();
        userEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);

        setEvents();

        BasicBinds.setWindowInsets(this);
    }

    private void setEvents() {
        search.setOnClickListener(v -> onClickSearch());
        delete.setOnClickListener(v -> onClickDelete());
        binds.setBasicEvents(this, userEmail);
    }
    private void onClickDelete() {
        boolean deleted = objectCreation.deleteUser(emailAddress.getText().toString().trim());
        if(!deleted) {
            Toast.makeText(this, "User could not be deleted", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_LONG).show();
    }

    private void onClickSearch() {
        String enteredEmail = emailAddress.getText().toString().trim();

        if(enteredEmail.isEmpty()) {
            Toast.makeText(this, "Please enter a email", Toast.LENGTH_LONG).show();
            hide();
        } else {
            Users user = lookupService.findUserByEmail(enteredEmail);
            if(user == null) {
                Toast.makeText(this, "No Such Account", Toast.LENGTH_LONG).show();
                hide();
            } else {
                setUser(user);
            }
        }
    }

    private void setUser(Users user) {
        email.setText(user.getEmail());
        name.setText(user.getFullName());
        gender.setText(user.getGender());
        age.setText(String.valueOf(user.getAge()));

        delete.setVisibility(View.VISIBLE);
        patientCard.setVisibility(View.VISIBLE);
    }

    private void hide() {
        patientCard.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
    }

    private void setViews() {
        patientCard = findViewById(R.id.patientCard);
        delete = findViewById(R.id.deleteButton);
        search = findViewById(R.id.searchButton);
        binds = setBasicBinds(this);
        email = findViewById(R.id.emailActual);
        name = findViewById(R.id.nameActual);
        gender = findViewById(R.id.genderActual);
        age = findViewById(R.id.ageActual);
        emailAddress = findViewById(R.id.editTextEmailAddress);
    }
}
