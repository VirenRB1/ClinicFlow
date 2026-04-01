package com.example.clinicflow.presentation.sharedScreens.searchModeHandlers;

import android.app.Activity;

import com.example.clinicflow.R;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCardView;

public class DeleteUserModeHandler extends BaseSearchUserModeHandler {

    public DeleteUserModeHandler(
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
        view.hidePatientFields();
        view.hideDoctorFields();
        view.hideCard();
        view.setSearchTitle(R.string.search_by_email_user);
        view.setActionText(R.string.delete);
    }

    @Override
    public void onSearchClick(String enteredEmail) {
        Users user = lookupService.findUserByEmail(enteredEmail);

        if (user != null) {
            view.showUser(user);

        } else {
            showToast("No Such Account");
            view.hideCard();
        }

    }

    @Override
    public void onActionClick(String displayedEmail) {
        boolean deleted = objectCreation.deleteUser(displayedEmail);

        if (deleted) {
            showToast("User Deleted");
            view.clearInput();
            view.hideCard();
        } else {
            showToast("User Could Not Be Deleted");
        }

    }
}