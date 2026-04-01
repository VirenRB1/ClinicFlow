package com.example.clinicflow.presentation.sharedScreens.searchModeHandlers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCardView;

public abstract class BaseSearchUserModeHandler implements SearchUserModeHandler {

    protected final Activity activity;
    protected final String userEmail;
    protected final LookupService lookupService;
    protected final ObjectCreation objectCreation;
    protected final SearchUserCardView view;

    protected BaseSearchUserModeHandler(
            Activity activity,
            String userEmail,
            LookupService lookupService,
            ObjectCreation objectCreation,
            SearchUserCardView view
    ) {
        this.activity = activity;
        this.userEmail = userEmail;
        this.lookupService = lookupService;
        this.objectCreation = objectCreation;
        this.view = view;
    }

    protected void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    protected void start(Intent intent) {
        activity.startActivity(intent);
    }
}