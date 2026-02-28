package com.example.clinicflow.presentation.sharedScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.MedicalHistory;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.components.MedicalRecordAdapter;
import com.example.clinicflow.presentation.components.RecyclerViewInterface;

import java.util.List;

public class MyRecords extends AppCompatActivity implements RecyclerViewInterface {
    List<MedicalRecord> records;
    Button back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_records);

        RecyclerView recyclerView = findViewById(R.id.recordsRecyclerView);
        TextView emptyStateText = findViewById(R.id.emptyStateText);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        String userEmail = getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL);
        String patientEmail = getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL);
        String finalEmail = actEmail(userEmail, patientEmail);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        MedicalHistory medicalHistory = app.getMedicalHistory();

        records = medicalHistory.getSortedMedicalHistoryForPatient(finalEmail);

        MedicalRecordAdapter adapter = new MedicalRecordAdapter(this,records, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showDetails(records, emptyStateText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDetails(List<MedicalRecord> records, TextView emptyStateText) {
        if (records == null || records.isEmpty()) {
            emptyStateText.setVisibility(TextView.VISIBLE);
        } else {
            emptyStateText.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onRecordClick(int position) {
        Intent intent = new Intent(MyRecords.this, MedicalRecordDetail.class);

        intent.putExtra(Navigation.EXTRA_RECORD, records.get(position));
        intent.putExtra(Navigation.EXTRA_USER_EMAIL, getIntent().getStringExtra(Navigation.EXTRA_USER_EMAIL));
        if(getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL) != null){
            intent.putExtra(Navigation.EXTRA_PATIENT_EMAIL, getIntent().getStringExtra(Navigation.EXTRA_PATIENT_EMAIL));
        }

        startActivity(intent);
    }

    public String actEmail(String userEmail, String patientEmail){
        if(patientEmail != null){
            return patientEmail;
        }
        return userEmail;
    }
}
