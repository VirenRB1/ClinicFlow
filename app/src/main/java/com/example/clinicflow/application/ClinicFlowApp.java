package com.example.clinicflow.application;

import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;
//Startup app
public class ClinicFlowApp extends android.app.Application {
    private UserRepository userRepository;

    private AuthService authService;

    private ObjectCreation objectCreation;


    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new FakeUserRepository();
        authService = new AuthService(userRepository);
        objectCreation = new ObjectCreation(userRepository);
    }

    public ObjectCreation getObjectCreation() {
        return objectCreation;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
