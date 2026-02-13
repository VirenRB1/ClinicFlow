package com.example.clinicflow.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clinicflow.models.MedicalRecord;
import com.example.clinicflow.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Unit tests for MedicalHistory
public class MedicalHistoryTest {

    private UserRepository mockRepo;
    private MedicalHistory medicalHistory;

    // create a fresh mock and service before each test
    @Before
    public void setup() {
        mockRepo = mock(UserRepository.class);
        medicalHistory = new MedicalHistory(mockRepo);
    }

    // records should come back sorted by date (newest first)
    @Test
    public void testGetSortedMedicalHistoryForPatient_SortsByDateDescending() {
        // Arrange
        String patientName = "Alice Brown";

        MedicalRecord record1 = new MedicalRecord(
                patientName,
                " Dr. Israel",
                "",
                "",
                getDate(2001, 6, 10)
        );
        MedicalRecord record2 = new MedicalRecord(
                patientName,
                " Dr. Israel",
                "L",
                "",
                getDate(2005, 5, 20)
        );
        MedicalRecord record3 = new MedicalRecord(patientName,
                " Dr. Rex",
                "_",
                "",
                getDate(2006, 10, 26)
        );

        List<MedicalRecord> unsortedList = new ArrayList<>();
        unsortedList.add(record1);
        unsortedList.add(record2);
        unsortedList.add(record3);

        when(mockRepo.getMedicalRecords(patientName)).thenReturn(unsortedList);

        // Act
        List<MedicalRecord> result = medicalHistory.getSortedMedicalHistoryForPatient(patientName);

        // check order
        assertEquals(3, result.size());
        assertEquals(record3, result.get(0));
        assertEquals(record2, result.get(1));
        assertEquals(record1, result.get(2));
        verify(mockRepo).getMedicalRecords(patientName);
    }

    // repo returns null → should handle it and give empty list
    @Test
    public void testGetSortedMedicalHistoryForPatient_NullReturnsEmpty() {
        when(mockRepo.getMedicalRecords(anyString())).thenReturn(null);

        List<MedicalRecord> result = medicalHistory.getSortedMedicalHistoryForPatient("jefferey");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // repo returns empty list → result should stay empty
    @Test
    public void testGetSortedMedicalHistoryForPatient_EmptyListReturnsEmpty() {
        // Arrange
        String patientName = "Bob Davis";

        when(mockRepo.getMedicalRecords(patientName)).thenReturn(new ArrayList<>());

        // Act
        List<MedicalRecord> result = medicalHistory.getSortedMedicalHistoryForPatient(patientName);

        // still empty
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(mockRepo).getMedicalRecords(patientName);
    }

    // helper to make dates easier to read in tests
    private Date getDate(int year, int month, int day) {
        var ins = (LocalDate.of(year, month, day).atStartOfDay().toInstant(ZoneOffset.UTC));
        return Date.from(ins);
    }
}
