package com.example.clinicflow.application;

import android.content.Context;

import com.example.clinicflow.business.AppointmentService;
import com.example.clinicflow.business.AuthService;
import com.example.clinicflow.business.DocAvailabilityService;
import com.example.clinicflow.business.ObjectCreation;
import com.example.clinicflow.business.LookupService;
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
