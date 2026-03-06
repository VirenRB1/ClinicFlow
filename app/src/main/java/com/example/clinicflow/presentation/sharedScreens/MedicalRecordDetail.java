package com.example.clinicflow.presentation.sharedScreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.presentation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MedicalRecordDetail extends AppCompatActivity {
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private Button backBtn;
    private TextView doctorName;
    private TextView date;
    private TextView patientName;
    private TextView purpose;
    private TextView doctorNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.medical_record_detail);

        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> finish());

        MedicalRecord record = (MedicalRecord) getIntent().getSerializableExtra(Navigation.EXTRA_RECORD);
        if (record == null) {
            Toast.makeText(this, "No record provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setDetailViews();
        setDetails(record);
    }

    private void setDetails(MedicalRecord record) {
        doctorName.setText(record.getDoctorName());
        date.setText(dateFmt.format(record.getDate()));
        patientName.setText(record.getPatientName());
        purpose.setText(record.getPurpose());
        doctorNote.setText(record.getDoctorNote());
    }

    private void setDetailViews() {
        doctorName = findViewById(R.id.detailDoctorName);
        date = findViewById(R.id.detailDate);
        patientName = findViewById(R.id.detailPatientName);
        purpose = findViewById(R.id.detailPurpose);
        doctorNote = findViewById(R.id.detailDoctorNote);
    }


}
