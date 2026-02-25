package com.example.clinicflow.application;

import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;
//Startup app
public class ClinicFlowApp extends android.app.Application {
    private UserRepository userRepository;

    private AuthService authService;

    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new FakeUserRepository();
        authService = new AuthService(userRepository);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
