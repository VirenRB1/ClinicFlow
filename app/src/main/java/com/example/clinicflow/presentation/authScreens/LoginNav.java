package com.example.clinicflow.presentation.authScreens;


import android.content.Context;
import android.content.Intent;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.Navigation;
import com.example.clinicflow.presentation.doctorScreens.DoctorScreen;
import com.example.clinicflow.presentation.patientScreens.PatientScreen;
import com.example.clinicflow.presentation.staffScreens.StaffScreen;

public class LoginNav {

    private final Context context;

    public LoginNav(Context context) {
        this.context = context;
    }

    public Intent sendToLanding(Users user, String email) {
        Intent intent = null;
        if (user instanceof Patient) {
            intent = new Intent(context, PatientScreen.class);
        } else if (user instanceof Doctor) {
            intent = new Intent(context, DoctorScreen.class);
        } else if (user instanceof Staff) {
            intent = new Intent(context, StaffScreen.class);
        }

        if (intent != null) {
            intent.putExtra(Navigation.EXTRA_USER_EMAIL, email);
        }
        return intent;
    }
}
