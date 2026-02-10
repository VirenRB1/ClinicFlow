package com.example.clinicflow.presentation;

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

import com.example.clinicflow.R;
import com.example.clinicflow.business.MedicalHistory;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import java.util.List;

public class MyRecords extends AppCompatActivity implements RecyclerViewInterface{

    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String EXTRA_DB = "fakeDB";

    FakeUserRepository repo = (FakeUserRepository) getIntent().getSerializableExtra(EXTRA_DB);
    MedicalHistory history = new MedicalHistory(repo);

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
                Intent intent = new Intent(MyRecords.this, PatientScreen.class);
                intent.putExtra(EXTRA_USER_EMAIL, getIntent().getStringExtra(EXTRA_USER_EMAIL));
                startActivity(intent);
            }
        });
        String email = getIntent().getStringExtra(EXTRA_USER_EMAIL);
        //email check is not done here because no way to get to this screen without logging in
        //All checks regarding email validation done in MainActivity
        String patientFullName = history.getPatientNameByEmail(email);
        //Null check?

        records = history.getSortedMedicalHistoryForPatient(patientFullName);


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

        startActivity(intent);
    }
}
