package com.example.clinicflow.presentation.sharedScreens.searchModeHandlers;

public interface SearchUserModeHandler {
    void setup();
    void onSearchClick(String enteredEmail);
    void onActionClick(String displayedEmail);
}