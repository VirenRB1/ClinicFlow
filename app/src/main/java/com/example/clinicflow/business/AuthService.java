package com.example.clinicflow.business;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.FakeDatabase;

public class AuthService {
    public Users authenticate(String email, String password){

        if(!formatCheck(email, password)){
            return null;
        }

        FakeDatabase db = new FakeDatabase();

        return patientFinder(db, email, password);
        return doctorFinder(db, email, password);
        return staffFinder(db, email, password);

        return null;
    }

    public Users patientFinder(FakeDatabase db, String email, String password){
        for(Patient p : db.patients){
            if(p.getEmail().equalsIgnoreCase(email) && p.getPassword().equals(password)){
                return p;
            }
        }
        return null;
    }

    public Users staffFinder(FakeDatabase db, String email, String password){
        for(Staff p : db.staff){
            if(p.getEmail().equalsIgnoreCase(email) && p.getPassword().equals(password)){
                return p;
            }
        }
        return null;
    }

    public Users doctorFinder(FakeDatabase db, String email, String password){
        for(Doctor p : db.doctors){
            if(p.getEmail().equalsIgnoreCase(email) && p.getPassword().equals(password)){
                return p;
            }
        }
        return null;
    }

    public boolean formatCheck(String email, String password){
        if(email == null || email.isEmpty()){
            return false;
        }
        if(password == null || password.isEmpty()){
            return false;
        }
        return true;
    }
}
