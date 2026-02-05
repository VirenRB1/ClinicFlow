package com.example.clinicflow.business;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.persistence.fake.FakeUserRepository;

import java.util.List;

public class AuthService {
    FakeUserRepository db = new FakeUserRepository();

    public Users authenticate(String email, String password){
        Users myUser;
        if(!formatCheck(email, password)){
            myUser = null;
        }else {
            if (email.contains("@clinicstaff.com")) {
                myUser = validateStaff(email, password);
            } else if (email.contains("@clinicdoc.com")) {
                myUser = validateDoctor(email, password);
            } else {
                myUser = validatePatient(email, password);
            }
        }
        return myUser;
    }

    public Users validatePatient(String email, String password){
        List<Patient> patients = db.getAllPatients();
        for(Patient patient : patients){
            if(patient.getEmail().equalsIgnoreCase(email) && patient.getPassword().equals(password)){
                return patient;
            }
        }
        return null;
    }

    public Users validateStaff(String email, String password){
        List<Staff> staffs = db.getAllStaffs();
        for(Staff staff : staffs){
            if(staff.getEmail().equalsIgnoreCase(email) && staff.getPassword().equals(password)){
                return staff;
            }
        }
        return null;
    }

    public Users validateDoctor(String email, String password){
        List<Doctor> doctors = db.getAllDoctors();
        for(Doctor doc : doctors){
            if(doc.getEmail().equalsIgnoreCase(email) && doc.getPassword().equals(password)){
                return doc;
            }
        }
        return null;
    }

    public boolean formatCheck(String email, String password){
        if(email == null || email.isEmpty() || !email.contains("@") || !email.contains(".")){
            return false;
        }
        if(password == null || password.isEmpty()){
            return false;
        }
        return true;
    }
}
