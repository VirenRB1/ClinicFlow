package com.example.clinicflow.persistence.fake;

import com.example.clinicflow.models.Admin;
import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.persistence.UserRepository;

import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Staff;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.example.clinicflow.models.Users;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.DoctorAvailability;
// Fake temporary database
public class FakeUserRepository implements UserRepository, Serializable {
//Lists of doctors, patients and staffs
    List<Doctor> doctors;
    List<Patient> patients;
    List<Staff> staffs;
    List<Admin> admins;

    HashMap<String, List<MedicalRecord>> medicalRecords;
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<DoctorAvailability> doctorAvailabilities = new ArrayList<>();

    public FakeUserRepository() {
        createFakeData();
    }

    //Create database
    private void createFakeData() {
        initializeFakeAdmins();
        initializeFakeDoctors();
        initializeFakePatients();
        initializeFakeStaffs();
        initializeFakeMedicalRecords();
    }

    private void initializeFakeAdmins() {
        admins = new ArrayList<>();
        admins.add(new Admin("Admin","Admin","admin@clinic.com","admin","Female", LocalDate.of(1990,1,1)));
    }
    //Add doctors
    private void initializeFakeDoctors() {
        doctors = new ArrayList<>();
        doctors.add(new Doctor("John","Doe","johndoe@clinicdoc.com","pass1","Male", LocalDate.of(1999,3,12) , Specialization.CARDIOLOGY,"LIC12345"));
        doctors.add(new Doctor("Jane","Smith","janesmith@clinicdoc.com","pass2","Female", LocalDate.of(2001,7,4),Specialization.NEUROLOGY,"LIC67890"));
        doctors.add(new Doctor("Emily","Johnson","emilyjohnson@clinicdoc.com","pass3","Female", LocalDate.of(2003,3,6) ,Specialization.PEDIATRICS,"LIC54321"));
    }
    // Add patients
    private void initializeFakePatients() {
        patients = new ArrayList<>();
        patients.add(new Patient("Alice","Brown","alicebrown@gmail.com","pass4","Female", LocalDate.of(2000,1,1),"123456","5551234"));
        patients.add(new Patient("Bob","Davis","bobdavis@gmail.com","pass5","Male",LocalDate.of(1999,3,12),"654321","5555678"));
        patients.add(new Patient("Charlie","Wilson","charliewilson@gmail.com","pass6","Male", LocalDate.of(2001,7,4), "789012","5559012"));
    }
    // Add staffs
    private void initializeFakeStaffs() {
        staffs = new ArrayList<>();
        staffs.add(new Staff("Eve","Miller","evemiller@clinicstaff.com","pass7","Female",LocalDate.of(2003,3,6),"Receptionist"));
        staffs.add(new Staff("Frank","Garcia","frankgarcia@clinicstaff.com","pass8","Male",LocalDate.of(2001,7,4),"Receptionist"));
        staffs.add(new Staff("Grace","Martinez","gracemartinez@clinicstaff.com","pass9","Female",LocalDate.of(1999,3,3),"Administrator"));
    }
    // Add medical records
    private void initializeFakeMedicalRecords() {
        medicalRecords = new HashMap<>();
        medicalRecords.put("alicebrown@gmail.com", new ArrayList<>());
        medicalRecords.put("bobdavis@gmail.com", new ArrayList<>());
        medicalRecords.put("charliewilson@gmail.com", new ArrayList<>());

        MedicalRecord record1 = new MedicalRecord(1, "Alice Brown", "John Doe", "alicebrown@gmail.com", "Check-up", "Regular check-up",
                new Date());
        MedicalRecord record2 = new MedicalRecord(2, "Alice Brown", "Jane Smith", "alicebrown@gmail.com", "Follow-up", "Follow-up visit",
                new Date());
        MedicalRecord record3 = new MedicalRecord(3, "Charlie Wilson", "Emily Johnson", "charliewilson@gmail.com", "Prescription",
                "Prescribed medication", new Date());

        medicalRecords.get("alicebrown@gmail.com").add(record1);
        medicalRecords.get("alicebrown@gmail.com").add(record2);
        medicalRecords.get("charliewilson@gmail.com").add(record3);
    }

    public List<MedicalRecord> getMedicalRecords(String patientEmail) {
        return medicalRecords.getOrDefault(patientEmail, new ArrayList<>());
    }
    //  Get methods
    @Override
    public List<Patient> getAllPatients() {
        return Collections.unmodifiableList(patients);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    @Override
    public List<Staff> getAllStaffs() {
        return Collections.unmodifiableList(staffs);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return Collections.unmodifiableList(admins);
    }

    // Add a patient to database
    @Override
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    @Override
    public void addStaff(Staff staff) {
        staffs.add(staff);
    }

    @Override
    public void deleteUser(Users user) {
        if (user instanceof Patient) {
            patients.remove(user);
        } else if (user instanceof Doctor) {
            doctors.remove(user);
        } else if (user instanceof Staff) {
            staffs.remove(user);
        }
    }

    @Override
    public void addMedicalRecord(MedicalRecord record) {
        String email = record.getEmail();
        if (!medicalRecords.containsKey(email)) {
            medicalRecords.put(email, new ArrayList<>());
        }
        medicalRecords.get(email).add(record);
    }

    // Get a patient by email
    @Override
    public Patient getPatientByEmail(String email) {
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                return patient;
            }
        }
        return null;
    }
    // Get user by email
    @Override
    public Users getUserByEmail(String email) {
        for (Doctor doctor : doctors) {
            if (doctor.getEmail().equalsIgnoreCase(email)) {
                return doctor;
            }
        }

        for (Staff staff : staffs) {
            if (staff.getEmail().equalsIgnoreCase(email)) {
                return staff;
            }
        }

        for (Patient patient : patients) {
            if (patient.getEmail().equalsIgnoreCase(email)) {
                return patient;
            }
        }

        for (Admin admin : admins) {
            if (admin.getEmail().equalsIgnoreCase(email)) {
                return admin;
            }
        }

        return null;
    }
    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == appointment.getId()) {
                appointments.set(i, appointment);
                return;
            }
        }
    }

    @Override
    public void addDoctorAvailability(DoctorAvailability availability) {
        doctorAvailabilities.add(availability);
    }

    @Override
    public List<DoctorAvailability> getDoctorAvailability(String doctorEmail, int dayOfWeek) {
        List<DoctorAvailability> result = new ArrayList<>();
        for (DoctorAvailability availability : doctorAvailabilities) {
            if (availability.getDoctorEmail().equals(doctorEmail) && availability.getDayOfWeek() == dayOfWeek) {
                result.add(availability);
            }
        }
        return result;
    }

    @Override
    public List<Appointment> getAppointmentsForDoctorOnDate(String doctorEmail, LocalDate date) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorEmail().equals(doctorEmail)
                    && appointment.getAppointmentDate().equals(date)) {
                result.add(appointment);
            }
        }

        return result;
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(String patientEmail) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientEmail().equalsIgnoreCase(patientEmail)) {
                result.add(appointment);
            }
        }
        return result;
    }

}
