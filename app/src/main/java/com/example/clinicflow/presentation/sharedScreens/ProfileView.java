package com.example.clinicflow.presentation.sharedScreens;

import android.app.Activity;
import android.widget.TextView;

import com.example.clinicflow.R;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.google.android.material.card.MaterialCardView;

public class ProfileView {

    private final MaterialCardView userCard;

    private final TextView email;
    private final TextView name;
    private final TextView gender;
    private final TextView age;

    private final TextView position;
    private final TextView positionActual;

    private final TextView specialization;
    private final TextView specializationActual;

    private final TextView licenseNumber;
    private final TextView licenseNumberActual;

    private final TextView phoneNumber;
    private final TextView phoneNumberActual;

    private final TextView healthCard;
    private final TextView healthCardActual;

    public ProfileView(Activity activity) {

        userCard = activity.findViewById(R.id.userCard);

        email = activity.findViewById(R.id.emailActual);
        name = activity.findViewById(R.id.nameActual);
        gender = activity.findViewById(R.id.genderActual);
        age = activity.findViewById(R.id.ageActual);

        position = activity.findViewById(R.id.position);
        positionActual = activity.findViewById(R.id.positionActual);

        specialization = activity.findViewById(R.id.specialization);
        specializationActual = activity.findViewById(R.id.specializationActual);

        licenseNumber = activity.findViewById(R.id.licenseNumber);
        licenseNumberActual = activity.findViewById(R.id.licenseActual);

        phoneNumber = activity.findViewById(R.id.phone);
        phoneNumberActual = activity.findViewById(R.id.phoneActual);

        healthCard = activity.findViewById(R.id.healthCard);
        healthCardActual = activity.findViewById(R.id.healthCardActual);

        hideFields();
    }

    public void showProfile(Users user) {
        setCommonInfo(user);
        setRoleInfo(user);
        userCard.setVisibility(TextView.VISIBLE);
    }

    private void setCommonInfo(Users user) {
        email.setText(user.getEmail());
        name.setText(user.getFullName());
        gender.setText(user.getGender());
        age.setText(String.valueOf(user.getAge()));
        hideFields();
    }

    private void setRoleInfo(Users user) {

        UserRole role = user.getRole();

        if (role == UserRole.DOCTOR) {
            showDoctor((Doctor) user);
        }

        else if (role == UserRole.STAFF) {
            showStaff((Staff) user);
        }

        else if (role == UserRole.PATIENT) {
            showPatient((Patient) user);
        }
    }

    private void showDoctor(Doctor doctor) {

        specialization.setVisibility(TextView.VISIBLE);
        specializationActual.setVisibility(TextView.VISIBLE);

        licenseNumber.setVisibility(TextView.VISIBLE);
        licenseNumberActual.setVisibility(TextView.VISIBLE);

        specializationActual.setText(
                String.valueOf(doctor.getSpecialization())
        );

        licenseNumberActual.setText(
                doctor.getLicenseNumber()
        );
    }

    private void showStaff(Staff staff) {

        position.setVisibility(TextView.VISIBLE);
        positionActual.setVisibility(TextView.VISIBLE);

        positionActual.setText(
                staff.getPosition()
        );
    }

    private void showPatient(Patient patient) {

        healthCard.setVisibility(TextView.VISIBLE);
        healthCardActual.setVisibility(TextView.VISIBLE);

        phoneNumber.setVisibility(TextView.VISIBLE);
        phoneNumberActual.setVisibility(TextView.VISIBLE);

        healthCardActual.setText(
                patient.getHealthCardNumber()
        );

        phoneNumberActual.setText(
                patient.getPhoneNumber()
        );
    }

    private void hideFields() {

        position.setVisibility(TextView.GONE);
        positionActual.setVisibility(TextView.GONE);

        specialization.setVisibility(TextView.GONE);
        specializationActual.setVisibility(TextView.GONE);

        licenseNumber.setVisibility(TextView.GONE);
        licenseNumberActual.setVisibility(TextView.GONE);

        phoneNumber.setVisibility(TextView.GONE);
        phoneNumberActual.setVisibility(TextView.GONE);

        healthCard.setVisibility(TextView.GONE);
        healthCardActual.setVisibility(TextView.GONE);
    }
}