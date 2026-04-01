package com.example.clinicflow.presentation.sharedScreens.searchModeHandlers;

import android.app.Activity;
import android.content.Intent;

import com.example.clinicflow.R;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.MyAppointments;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCardView;

public class CancelAppointmentModeHandler extends BaseSearchUserModeHandler {

    public CancelAppointmentModeHandler(
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
        view.showPatientFields();
        view.setSearchTitle(R.string.search_for_cancel);
        view.setActionText(R.string.cancelApt);
    }

    @Override
    public void onSearchClick(String enteredEmail) {
        Patient patient = lookupService.findPatientByEmail(enteredEmail);

        if (patient == null) {
            showToast("No Such Patient");
            view.hideCard();
            return;
        }

        view.showPatient(patient);
    }

    @Override
    public void onActionClick(String displayedEmail) {
        Intent intent = new Intent(activity, MyAppointments.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(NavigationExtras.EXTRA_PATIENT_EMAIL, displayedEmail);
        intent.putExtra(NavigationExtras.NOTES, false);
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        start(intent);
    }
}