package com.example.clinicflow.presentation.sharedScreens;

import android.app.Activity;

import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.BookAppointmentModeHandler;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.CancelAppointmentModeHandler;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.DeleteUserModeHandler;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.SearchUserModeHandler;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.ViewDoctorModeHandler;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.ViewPatientRecordsModeHandler;

public final class SearchUserModeFactory {

    private SearchUserModeFactory() {
    }

    public static SearchUserModeHandler create(
            String mode,
            Activity activity,
            String userEmail,
            LookupService lookupService,
            ObjectCreation objectCreation,
            SearchUserCardView view
    ) {
        if (NavigationExtras.MODE_DELETE_USER.equals(mode)) {
            return new DeleteUserModeHandler(activity, userEmail, lookupService, objectCreation, view);
        }

        if (NavigationExtras.MODE_BOOK_APPOINTMENT.equals(mode)) {
            return new BookAppointmentModeHandler(activity, userEmail, lookupService, objectCreation, view);
        }

        if (NavigationExtras.MODE_CANCEL_APPOINTMENT.equals(mode)) {
            return new CancelAppointmentModeHandler(activity, userEmail, lookupService, objectCreation, view);
        }

        if (NavigationExtras.MODE_VIEW_DOCTOR.equals(mode)) {
            return new ViewDoctorModeHandler(activity, userEmail, lookupService, objectCreation, view);
        }

        return new ViewPatientRecordsModeHandler(activity, userEmail, lookupService, objectCreation, view);
    }
}