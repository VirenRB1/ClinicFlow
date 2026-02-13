package com.example.clinicflow.presentation;

import static com.example.clinicflow.presentation.ViewPatients.EXTRA_PATIENT_EMAIL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;

import java.util.List;

public class MyRecords extends AppCompatActivity implements RecyclerViewInterface{

    public static final String EXTRA_USER_EMAIL = "user_email";

    List<MedicalRecord> records;
    Button back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_records);

        RecyclerView recyclerView = findViewById(R.id.recordsRecyclerView);
        TextView emptyStateText = findViewById(R.id.emptyStateText);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String userEmail = getIntent().getStringExtra(EXTRA_USER_EMAIL);
        String patientEmail = getIntent().getStringExtra(EXTRA_PATIENT_EMAIL);

        String finalEmail;

        if(patientEmail != null) {
            finalEmail = patientEmail;
        } else {
            finalEmail = userEmail;
        }

        //email check is not done here because no way to get to this screen without logging in
        //All checks regarding email validation done in MainActivity
        UserRepository repo = ((ClinicFlowApp) getApplication()).getUserRepository();

        String patientFullName = getPatientName(repo, finalEmail);
        //Null check?

        records = repo.getMedicalRecords(patientFullName);

        MedicalRecordAdapter adapter = new MedicalRecordAdapter(this,records, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (records == null || records.isEmpty()) {
            emptyStateText.setVisibility(TextView.VISIBLE);
        } else {
            emptyStateText.setVisibility(TextView.GONE);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String getPatientName(UserRepository repo, String email) {
        String fullName = null;
        for (Patient p : repo.getAllPatients()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                fullName = p.getFullName();
            }
        }
        return fullName;
    }

    @Override
    public void onRecordClick(int position) {
        Intent intent = new Intent(MyRecords.this, MedicalRecordDetail.class);

        intent.putExtra("Record", records.get(position));
        intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
        if(getIntent().getStringExtra(EXTRA_PATIENT_EMAIL) != null){
            intent.putExtra(EXTRA_PATIENT_EMAIL, getIntent().getStringExtra(EXTRA_PATIENT_EMAIL));
        }

        startActivity(intent);
    }
}
