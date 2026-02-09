package com.example.clinicflow.business;

import com.example.clinicflow.persistence.fake.FakeUserRepository;

public class MedicalHistory {
    private final FakeUserRepository DATABASE;

    public MedicalHistory(FakeUserRepository userRepository) {
        this.DATABASE = userRepository;
    }
}
