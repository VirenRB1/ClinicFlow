package com.example.clinicflow.presentation;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.models.MedicalRecord;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MedicalRecordDetail extends AppCompatActivity {

    public static final String EXTRA_RECORD = "Record";
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.medical_record_detail);

        MedicalRecord record = (MedicalRecord) getIntent().getSerializableExtra(EXTRA_RECORD);
        if (record == null) {
            Toast.makeText(this, "No record provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        TextView doctorName = findViewById(R.id.detailDoctorName);
        TextView date = findViewById(R.id.detailDate);
        TextView patientName = findViewById(R.id.detailPatientName);
        TextView purpose = findViewById(R.id.detailPurpose);
        TextView doctorNote = findViewById(R.id.detailDoctorNote);

        doctorName.setText(record.getDoctorName());
        date.setText(dateFmt.format(record.getDate()));
        patientName.setText(record.getPatientName());
        purpose.setText(record.getPurpose());
        doctorNote.setText(record.getDoctorNote());
    }
}
