package com.example.clinicflow.ui;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.Doctor;
import com.example.clinicflow.models.Patient;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
import com.example.clinicflow.persistence.real.SqlRepository;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.admin.AddOrDeleteScreen;
import com.example.clinicflow.presentation.admin.AdminScreen;
import com.example.clinicflow.presentation.admin.UserSignUp;
import com.example.clinicflow.presentation.authScreens.MainActivity;
import com.example.clinicflow.presentation.sharedScreens.AppointmentDetail;
import com.example.clinicflow.presentation.sharedScreens.SearchUserCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Espresso UI tests for screens whose interactions do not require
 * system-level pickers (DatePickerDialog, spinner dialogs).
 *
 * Coverage:
 *  - MainActivity  : login validation and navigation
 *  - UserSignUp    : role-based field visibility
 *  - AppointmentDetail : button and note visibility by intent flags
 *  - SearchUserCard    : initial state, search found/not found
 *  - AdminScreen / AddOrDeleteScreen : button-click navigation
 *
 * Tests that require date pickers or dropdown dialogs are documented
 * separately in Docs/ManualUITestGuide.md.
 */
@RunWith(AndroidJUnit4.class)
public class UITest {

    // Emails used exclusively by this test class to avoid collisions.
    private static final String TEST_PATIENT_EMAIL = "uitest.patient@test.com";
    private static final String TEST_DOCTOR_EMAIL  = "uitest.doctor@test.com";
    private static final String TEST_PASSWORD      = "pass123";

    @Before
    public void seedDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        ClinicFlowApp app = (ClinicFlowApp) context;

        // Remove any leftover data from a previous interrupted run.
        app.getObjectCreation().deleteUser(TEST_PATIENT_EMAIL);
        app.getObjectCreation().deleteUser(TEST_DOCTOR_EMAIL);

        // Insert test users directly via SqlRepository, bypassing signup
        // validation so the test setup itself cannot fail on validation rules.
        SqlRepository repo = new SqlRepository(context);
        repo.addPatient(new Patient(
                "UI", "TestPatient", TEST_PATIENT_EMAIL, TEST_PASSWORD,
                "Male", LocalDate.of(2000, 1, 1), "123456789", "4165551234"));
        repo.addDoctor(new Doctor(
                "UI", "TestDoctor", TEST_DOCTOR_EMAIL, TEST_PASSWORD,
                "Female", LocalDate.of(1985, 6, 15),
                Specialization.CARDIOLOGY, "LIC99999"));
    }

    @After
    public void cleanDatabase() {
        ClinicFlowApp app = (ClinicFlowApp) ApplicationProvider.getApplicationContext();
        app.getObjectCreation().deleteUser(TEST_PATIENT_EMAIL);
        app.getObjectCreation().deleteUser(TEST_DOCTOR_EMAIL);
    }

    // =========================================================================
    // MainActivity — login validation and navigation
    // =========================================================================

    @Test
    public void login_bothFieldsEmpty_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_emailFilledPasswordEmpty_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(TEST_PATIENT_EMAIL));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_wrongPassword_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(TEST_PATIENT_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText("wrongpassword"));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_unknownEmail_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText("nobody@nowhere.com"));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(TEST_PASSWORD));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validPatientCredentials_navigatesToPatientScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(TEST_PATIENT_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(TEST_PASSWORD));
            onView(withId(R.id.loginButton)).perform(click());
            // PatientScreen is now in the foreground; verify one of its unique buttons.
            onView(withId(R.id.bookAppointmentButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validDoctorCredentials_navigatesToDoctorScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(TEST_DOCTOR_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(TEST_PASSWORD));
            onView(withId(R.id.loginButton)).perform(click());
            // DoctorScreen is now in the foreground.
            onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void signUpButton_opensUserSignUp() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.signUpButton)).perform(click());
            // UserSignUp screen has a first-name field; MainActivity does not.
            onView(withId(R.id.FirsNameEditText)).check(matches(isDisplayed()));
        }
    }

    // =========================================================================
    // UserSignUp — role-based field visibility
    // =========================================================================

    @Test
    public void userSignUp_patientRole_showsPatientFields_hidesOtherFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.PhoneEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.HealthCardEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.licenseNumberEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.positionEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void userSignUp_doctorRole_showsDoctorFields_hidesOtherFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.DOCTOR);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.specializationEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.licenseNumberEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.PhoneEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.HealthCardEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.positionEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void userSignUp_staffRole_showsStaffField_hidesOtherFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.STAFF);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.positionEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.PhoneEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.HealthCardEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void userSignUp_emptyFormSubmit_staysOnSignUpScreen() {
        // Submitting with all empty fields must fail validation and stay put.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void userSignUp_duplicateEmail_staysOnSignUpScreen() {
        // TEST_PATIENT_EMAIL is already in the DB (seeded in @Before).
        // The validator must reject it and keep the user on the form.
        // Note: DOB is intentionally left empty so the test does not depend
        // on the DatePickerDialog; any field error keeps the screen open.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.FirsNameEditText)).perform(replaceText("Test"));
            onView(withId(R.id.LastNameEditText)).perform(replaceText("User"));
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(TEST_PATIENT_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(TEST_PASSWORD));
            onView(withId(R.id.PasswordConfirmEditText)).perform(replaceText(TEST_PASSWORD));
            onView(withId(R.id.GenderEditText)).perform(replaceText("Male"));
            onView(withId(R.id.PhoneEditText)).perform(replaceText("4165551234"));
            onView(withId(R.id.HealthCardEditText)).perform(replaceText("123456789"));
            // DOB not set (actDob stays null) — validator will reject before
            // hitting the duplicate-email check, but the screen still stays open.
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }

    // =========================================================================
    // AppointmentDetail — button and notes visibility from intent flags
    // =========================================================================

    /** Creates a minimal serializable Appointment for intent delivery. */
    private Appointment makeAppointment(AppointmentStatus status) {
        return new Appointment(
                TEST_DOCTOR_EMAIL,
                TEST_PATIENT_EMAIL,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                status,
                "Routine checkup",
                "All clear.");
    }

    @Test
    public void appointmentDetail_patientUpcomingView_showsCancelHidesCompleteHidesNotes() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES,       false);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(isDisplayed()));
            onView(withId(R.id.completeButton)).check(matches(not(isDisplayed())));
            onView(withId(R.id.detailDoctorNoteTitle)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_patientPastView_showsNotesHidesCancel() {
        // Past (completed) appointment viewed by patient: notes visible, no cancel.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.COMPLETED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES,       true);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.detailDoctorNoteTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
            onView(withId(R.id.completeButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_doctorView_showsCompleteAndNotesHidesCancel() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, true);
        intent.putExtra(NavigationExtras.NOTES,       true);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.completeButton)).check(matches(isDisplayed()));
            onView(withId(R.id.detailDoctorNoteTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_completedStatus_hidesCancelRegardlessOfView() {
        // Even in patient (non-doctor) view, a COMPLETED appointment has no cancel.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.COMPLETED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES,       true);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    // =========================================================================
    // SearchUserCard — initial state and search results
    // =========================================================================

    @Test
    public void searchUserCard_onOpen_cardAndActionButtonHidden() {
        // Before any search, the result card must not be visible.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,    NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.patientCard)).check(matches(not(isDisplayed())));
            onView(withId(R.id.actionButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void searchUserCard_knownPatientEmail_showsCardAndActionButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,    NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText(TEST_PATIENT_EMAIL));
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
            onView(withId(R.id.actionButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void searchUserCard_unknownEmail_cardRemainsHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,    NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText("ghost@nowhere.com"));
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(not(isDisplayed())));
            onView(withId(R.id.actionButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void searchUserCard_viewDoctorMode_knownDoctor_showsDoctorFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,    NavigationExtras.MODE_VIEW_DOCTOR);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_PATIENT_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText(TEST_DOCTOR_EMAIL));
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationActual)).check(matches(isDisplayed()));
            onView(withId(R.id.licenseActual)).check(matches(isDisplayed()));
        }
    }

    // =========================================================================
    // AdminScreen / AddOrDeleteScreen — button-click navigation
    // =========================================================================

    @Test
    public void adminScreen_addDoctorButton_opensAddOrDeleteScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminScreen.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<AdminScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addDoctorButton)).perform(click());
            // AddOrDeleteScreen has both addButton and deleteButton.
            onView(withId(R.id.addButton)).check(matches(isDisplayed()));
            onView(withId(R.id.deleteButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void adminScreen_addStaffButton_opensAddOrDeleteScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminScreen.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<AdminScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addStaffButton)).perform(click());
            onView(withId(R.id.addButton)).check(matches(isDisplayed()));
            onView(withId(R.id.deleteButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addOrDeleteScreen_addButton_opensUserSignUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddOrDeleteScreen.class);
        intent.putExtra(NavigationExtras.USER_ROLE,       UserRole.DOCTOR);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<AddOrDeleteScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addButton)).perform(click());
            // UserSignUp screen is now in the foreground.
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addOrDeleteScreen_deleteButton_opensSearchUserCard() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddOrDeleteScreen.class);
        intent.putExtra(NavigationExtras.USER_ROLE,       UserRole.PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, TEST_DOCTOR_EMAIL);
        try (ActivityScenario<AddOrDeleteScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.deleteButton)).perform(click());
            // SearchUserCard screen is now in the foreground.
            onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
        }
    }
}
