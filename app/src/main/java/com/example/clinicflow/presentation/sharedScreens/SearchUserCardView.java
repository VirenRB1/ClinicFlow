package com.example.clinicflow.presentation.sharedScreens;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clinicflow.R;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Users;
import com.google.android.material.card.MaterialCardView;

public class SearchUserCardView {

    private final MaterialCardView patientCard;
    private final Button searchButton;
    private final Button actionButton;
    private final EditText emailAddress;

    private final TextView email;
    private final TextView name;
    private final TextView gender;
    private final TextView age;
    private final TextView healthCard;
    private final TextView phone;
    private final TextView specialization;
    private final TextView licenseNum;
    private final TextView searchTitle;

    private final View healthCardLabel;
    private final View phoneLabel;
    private final View specializationLabel;
    private final View licenseLabel;

    public SearchUserCardView(Activity activity) {
        patientCard = activity.findViewById(R.id.patientCard);
        actionButton = activity.findViewById(R.id.actionButton);
        searchButton = activity.findViewById(R.id.searchButton);

        email = activity.findViewById(R.id.emailActual);
        name = activity.findViewById(R.id.nameActual);
        gender = activity.findViewById(R.id.genderActual);
        age = activity.findViewById(R.id.ageActual);
        healthCard = activity.findViewById(R.id.healthCardActual);
        phone = activity.findViewById(R.id.phoneActual);
        specialization = activity.findViewById(R.id.specializationActual);
        licenseNum = activity.findViewById(R.id.licenseActual);

        emailAddress = activity.findViewById(R.id.editTextEmailAddress);
        searchTitle = activity.findViewById(R.id.searchSectionTitle);

        healthCardLabel = activity.findViewById(R.id.healthCard);
        phoneLabel = activity.findViewById(R.id.phone);
        specializationLabel = activity.findViewById(R.id.specialization);
        licenseLabel = activity.findViewById(R.id.license);

        hideCard();
        hidePatientFields();
        hideDoctorFields();
    }

    public void setOnSearchClick(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public void setOnActionClick(View.OnClickListener listener) {
        actionButton.setOnClickListener(listener);
    }

    public String getEnteredEmail() {
        return emailAddress.getText().toString().trim();
    }

    public String getDisplayedEmail() {
        return email.getText().toString().trim();
    }

    public void clearInput() {
        emailAddress.setText("");
    }

    public void setSearchTitle(int titleRes) {
        searchTitle.setText(titleRes);
    }

    public void setActionText(int textRes) {
        actionButton.setText(textRes);
    }

    public void showCard() {
        patientCard.setVisibility(View.VISIBLE);
        actionButton.setVisibility(View.VISIBLE);
    }

    public void hideCard() {
        patientCard.setVisibility(View.GONE);
        actionButton.setVisibility(View.GONE);
    }

    public void showPatientFields() {
        hideDoctorFields();
        healthCard.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        healthCardLabel.setVisibility(View.VISIBLE);
        phoneLabel.setVisibility(View.VISIBLE);
    }

    public void hidePatientFields() {
        healthCard.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        healthCardLabel.setVisibility(View.GONE);
        phoneLabel.setVisibility(View.GONE);
    }

    public void showDoctorFields() {
        hidePatientFields();
        specialization.setVisibility(View.VISIBLE);
        licenseNum.setVisibility(View.VISIBLE);
        specializationLabel.setVisibility(View.VISIBLE);
        licenseLabel.setVisibility(View.VISIBLE);
    }

    public void hideDoctorFields() {
        specialization.setVisibility(View.GONE);
        licenseNum.setVisibility(View.GONE);
        specializationLabel.setVisibility(View.GONE);
        licenseLabel.setVisibility(View.GONE);
    }

    public void showUser(Users user) {
        hidePatientFields();
        hideDoctorFields();

        email.setText(user.getEmail());
        name.setText(user.getFullName());
        gender.setText(user.getGender());
        age.setText(String.valueOf(user.getAge()));

        showCard();
    }

    public void showPatient(Patient patient) {
        showPatientFields();

        email.setText(patient.getEmail());
        name.setText(patient.getFullName());
        gender.setText(patient.getGender());
        age.setText(String.valueOf(patient.getAge()));
        healthCard.setText(patient.getHealthCardNumber());
        phone.setText(patient.getPhoneNumber());

        showCard();
    }

    public void showDoctor(Doctor doctor) {
        showDoctorFields();

        email.setText(doctor.getEmail());
        name.setText(doctor.getFullName());
        gender.setText(doctor.getGender());
        age.setText(String.valueOf(doctor.getAge()));
        specialization.setText(String.valueOf(doctor.getSpecialization()));
        licenseNum.setText(doctor.getLicenseNumber());

        showCard();
    }
}