package com.example.clinicflow.presentation.sharedScreens;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.services.DocAvailabilityService;
import com.example.clinicflow.models.DoctorAvailability;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;

import java.util.List;

public class MySchedule extends AppCompatActivity {

    private BasicBinds binds;
    private String actingUserEmail;
    private String doctorEmail;

    private TextView mondayTime;
    private TextView tuesdayTime;
    private TextView wednesdayTime;
    private TextView thursdayTime;
    private TextView fridayTime;
    private TextView saturdayTime;
    private TextView sundayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_schedule);

        setViews();

        actingUserEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        doctorEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL);

        if (doctorEmail == null) {
            doctorEmail = actingUserEmail;
        }

        if (actingUserEmail == null) {
            Toast.makeText(this, "Missing doctor information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binds.setBasicEvents(this, actingUserEmail);
        loadSchedule();
        BasicBinds.setWindowInsets(this);
    }

    private void loadSchedule() {
        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        DocAvailabilityService availabilityService = app.getDoctorAvailabilityService();

        setDayText(mondayTime, availabilityService.getDoctorAvailability(doctorEmail, 1));
        setDayText(tuesdayTime, availabilityService.getDoctorAvailability(doctorEmail, 2));
        setDayText(wednesdayTime, availabilityService.getDoctorAvailability(doctorEmail, 3));
        setDayText(thursdayTime, availabilityService.getDoctorAvailability(doctorEmail, 4));
        setDayText(fridayTime, availabilityService.getDoctorAvailability(doctorEmail, 5));
        setDayText(saturdayTime, availabilityService.getDoctorAvailability(doctorEmail, 6));
        setDayText(sundayTime, availabilityService.getDoctorAvailability(doctorEmail, 7));
    }

    private void setDayText(TextView textView, List<DoctorAvailability> availabilities) {
        StringBuilder text = new StringBuilder();

        for (DoctorAvailability availability : availabilities) {
            text.append(availability.getStartTime())
                    .append(" - ")
                    .append(availability.getEndTime())
                    .append("\n");
        }

        textView.setText(text.toString().trim());
    }

    private void setViews() {
        binds = BasicBinds.setBasicBinds(this);

        mondayTime = findViewById(R.id.mondayTime);
        tuesdayTime = findViewById(R.id.tuesdayTime);
        wednesdayTime = findViewById(R.id.wednesdayTime);
        thursdayTime = findViewById(R.id.thursdayTime);
        fridayTime = findViewById(R.id.fridayTime);
        saturdayTime = findViewById(R.id.saturdayTime);
        sundayTime = findViewById(R.id.sundayTime);
    }
}