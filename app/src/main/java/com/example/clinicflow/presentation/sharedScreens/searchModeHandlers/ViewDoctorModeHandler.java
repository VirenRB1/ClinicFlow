package com.example.clinicflow.presentation.sharedScreens.searchModeHandlers;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.R;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.MySchedule;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCardView;

public class ViewDoctorModeHandler extends BaseSearchUserModeHandler {

    public ViewDoctorModeHandler(
            Activity activity,
            String userEmail,
            LookupService lookupService,
            ObjectCreation objectCreation,
            SearchUserCardView view
    ) {
        super(activity, userEmail, lookupService, objectCreation, view);
    }

    @Override
    public void setup() {
        view.hideCard();
        view.showDoctorFields();
        view.setSearchTitle(R.string.search_by_email);
        view.setActionText(R.string.viewSchedule);
    }

    @Override
    public void onSearchClick(String enteredEmail) {
        Doctor doctor = lookupService.findDoctorByEmail(enteredEmail);

        if (doctor == null) {
            showToast("No Such Doctor");
            view.hideCard();
            return;
        }

        view.showDoctor(doctor);
    }

    @Override
    public void onActionClick(String displayedEmail) {
        Intent intent = new Intent(activity, MySchedule.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(NavigationExtras.EXTRA_DOCTOR_EMAIL, displayedEmail);
        start(intent);
    }
}