package com.example.clinicflow.presentation.authScreens;


import android.content.Context;
import android.content.Intent;

import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.admin.AdminScreen;
import com.example.clinicflow.presentation.doctorScreens.DoctorScreen;
import com.example.clinicflow.presentation.patientScreens.PatientScreen;
import com.example.clinicflow.presentation.staffScreens.StaffScreen;

//move to business layer once business layer refactoring is done
public class LoginNav {

    private final Context context;

    public LoginNav(Context context) {
        this.context = context;
    }

    public Intent sendToLanding(Users user, String email) {
        Intent intent = null;
        if (user.getRole() == UserRole.PATIENT) {
            intent = new Intent(context, PatientScreen.class);
        } else if (user.getRole() == UserRole.DOCTOR) {
            intent = new Intent(context, DoctorScreen.class);
        } else if (user.getRole() == UserRole.STAFF) {
            intent = new Intent(context, StaffScreen.class);
        } else if (user.getRole() == UserRole.ADMIN) {
            intent = new Intent(context, AdminScreen.class);
        }

        if (intent != null) {
            intent.putExtra(Navigation.EXTRA_USER_EMAIL, email);
        }
        return intent;
    }
}
