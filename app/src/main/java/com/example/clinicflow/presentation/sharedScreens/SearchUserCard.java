package com.example.clinicflow.presentation.sharedScreens;

import static com.example.clinicflow.presentation.BasicBinds.setBasicBinds;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.presentation.BasicBinds;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.sharedScreens.searchModeHandlers.SearchUserModeHandler;

public class SearchUserCard extends AppCompatActivity {

    private BasicBinds binds;
    private SearchUserCardView view;
    private SearchUserModeHandler modeHandler;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_card);

        ClinicFlowApp app = (ClinicFlowApp) getApplication();
        LookupService lookupService = app.getLookupService();
        ObjectCreation objectCreation = app.getObjectCreation();

        userEmail = getIntent().getStringExtra(NavigationExtras.EXTRA_USER_EMAIL);
        String mode = getIntent().getStringExtra(NavigationExtras.SEARCH_MODE);

        binds = setBasicBinds(this);
        view = new SearchUserCardView(this);

        modeHandler = SearchUserModeFactory.create(
                mode,
                this,
                userEmail,
                lookupService,
                objectCreation,
                view
        );

        modeHandler.setup();

        view.setOnSearchClick(v -> modeHandler.onSearchClick(view.getEnteredEmail()));
        view.setOnActionClick(v -> modeHandler.onActionClick(view.getDisplayedEmail()));
        binds.setBasicEvents(this, userEmail);
        BasicBinds.setWindowInsets(this);
    }
}