package com.example.clinicflow.application;

import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;
//Startup app
public class ClinicFlowApp extends android.app.Application {
    private UserRepository userRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new FakeUserRepository();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
