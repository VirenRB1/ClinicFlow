package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.sharedScreens.ViewPatients.EXTRA_PATIENT_EMAIL;

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

import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.R;
import com.example.clinicflow.business.MedicalHistory;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.presentation.components.MedicalRecordAdapter;
import com.example.clinicflow.presentation.components.RecyclerViewInterface;

import java.util.List;

public class MyRecords extends AppCompatActivity implements RecyclerViewInterface {

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
        MedicalHistory medicalHistory = new MedicalHistory(repo);

        records = medicalHistory.getSortedMedicalHistoryForPatient(finalEmail);

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
