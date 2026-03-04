package com.example.clinicflow.application;

import android.content.Context;

import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.business.MedicalHistory;
import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.business.PatientLookupService;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.fake.FakeUserRepository;
import com.example.clinicflow.persistence.real.SqlRepository;
//Startup app
public class ClinicFlowApp extends android.app.Application {

    private UserRepository userRepository;
    private AuthService authService;
    private ObjectCreation objectCreation;
    private MedicalHistory medicalHistory;
    private PatientLookupService patientLookupService;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        userRepository = new SqlRepository(context);
        //userRepository = new FakeUserRepository(); //for testing uncomment this and comment the code above
        authService = new AuthService(userRepository);
        objectCreation = new ObjectCreation(userRepository);
        patientLookupService = new PatientLookupService(userRepository);
        medicalHistory = new MedicalHistory(userRepository);
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public PatientLookupService getPatientLookupService() {
        return patientLookupService;
    }

    public ObjectCreation getObjectCreation() {
        return objectCreation;
    }

    public AuthService getAuthService() {
        return authService;
    }

}
