package com.example.clinicflow.presentation.authScreens;

import android.content.Context;
import android.content.Intent;

import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.admin.AdminScreen;
import com.example.clinicflow.presentation.doctorScreens.DoctorScreen;
import com.example.clinicflow.presentation.patientScreens.PatientScreen;
import com.example.clinicflow.presentation.staffScreens.StaffScreen;

import java.util.HashMap;
import java.util.Map;

public class LoginNav {

    private final Context context;
    private final Map<UserRole, Class<?>> landingPages;

    public LoginNav(Context context) {
        this.context = context;
        this.landingPages = createLandingPagesMap();
    }

    private Map<UserRole, Class<?>> createLandingPagesMap() {
        Map<UserRole, Class<?>> landingPages = new HashMap<>();
        landingPages.put(UserRole.PATIENT, PatientScreen.class);
        landingPages.put(UserRole.DOCTOR, DoctorScreen.class);
        landingPages.put(UserRole.STAFF, StaffScreen.class);
        landingPages.put(UserRole.ADMIN, AdminScreen.class);
        return landingPages;
    }

    public Intent sendToLanding(Users user, String email) {
        Class<?> destination = landingPages.get(user.getRole());

        if (destination == null) {
            return null;
        }

        Intent intent = new Intent(context, destination);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, email);
        return intent;
    }
}
