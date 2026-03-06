package com.example.clinicflow.presentation.admin;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;
import static com.example.clinicflow.presentation.Navigation.onClickEmail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.Navigation;

public class AddOrDeleteScreen extends AppCompatActivity {
    private Button addButton;
    private Button deleteButton;
    private BasicBinds binds;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_or_delete);

        String role = getIntent().getStringExtra(Navigation.USER_ROLE);
        setViews();
        String email = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        setEvents(role, email);
    }

    private void setEvents(String role, String email) {
        binds.setBasicEvents(this, email);
        addButton.setOnClickListener(v -> onClickAdd(role));
        deleteButton.setOnClickListener(v -> onClickEmail(this, UserDelete.class, email));
    }

    private void onClickAdd(String role) {
        Intent intent = new Intent(this, UserSignUp.class);
        intent.putExtra(Navigation.USER_ROLE, role);
        startActivity(intent);
    }

    private void setViews() {
        binds = setBasicBinds(this);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

}
