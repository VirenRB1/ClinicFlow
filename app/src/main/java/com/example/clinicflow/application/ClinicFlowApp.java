package com.example.clinicflow.application;

import android.content.Context;

import com.example.clinicflow.business.services.AppointmentService;
import com.example.clinicflow.business.services.AuthService;
import com.example.clinicflow.business.services.DocAvailabilityService;
import com.example.clinicflow.business.creation.ObjectCreation;
import com.example.clinicflow.business.services.LookupService;
import com.example.clinicflow.persistence.UserRepository;
import com.example.clinicflow.persistence.real.SqlRepository;

//Startup app
public class ClinicFlowApp extends android.app.Application {

    private UserRepository userRepository;
    private AuthService authService;
    private ObjectCreation objectCreation;
    private LookupService lookupService;
    private DocAvailabilityService doctorAvailabilityService;
    private AppointmentService appointmentService;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        userRepository = new SqlRepository(context);
        //userRepository = new FakeUserRepository(); //for testing uncomment this and comment the code above
        authService = new AuthService(userRepository);
        objectCreation = new ObjectCreation(userRepository);
        lookupService = new LookupService(userRepository);
        doctorAvailabilityService = new DocAvailabilityService(userRepository);
        appointmentService = new AppointmentService(userRepository);
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
    public DocAvailabilityService getDoctorAvailabilityService() {
        return doctorAvailabilityService;
    }

    public LookupService getLookupService() {
        return lookupService;
    }

    public ObjectCreation getObjectCreation() {
        return objectCreation;
    }

    public AuthService getAuthService() {
        return authService;
    }

}
