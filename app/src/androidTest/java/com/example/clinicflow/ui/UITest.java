package com.example.clinicflow.ui;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.models.*;
import com.example.clinicflow.persistence.UserFactory;
import com.example.clinicflow.persistence.real.SqlRepository;
import com.example.clinicflow.presentation.NavigationExtras;
import com.example.clinicflow.presentation.admin.*;
import com.example.clinicflow.presentation.authScreens.MainActivity;
import com.example.clinicflow.presentation.sharedScreens.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;

/**
 * Espresso UI tests using the same accounts defined in UserFactory —
 * the same credentials that FakeUserRepository pre-seeds.
 */
@RunWith(AndroidJUnit4.class)
public class UITest {

    // Credentials from UserFactory — must match exactly.
    private static final String PATIENT_EMAIL    = "alicebrown@gmail.com";
    private static final String PATIENT_PASSWORD = "pass4";

    private static final String DOCTOR_EMAIL     = "johndoe@clinicdoc.com";
    private static final String DOCTOR_PASSWORD  = "pass1";

    @Before
    public void seedDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        ClinicFlowApp app = (ClinicFlowApp) context;

        // Clean any leftover rows from a previous interrupted run.
        for (Patient p : UserFactory.getDefaultPatients()) {
            app.getObjectCreation().deleteUser(p.getEmail());
        }
        for (Doctor d : UserFactory.getDefaultDoctors()) {
            app.getObjectCreation().deleteUser(d.getEmail());
        }

        // Insert the same objects UserFactory produces so passwords match.
        SqlRepository repo = new SqlRepository(context);
        for (Patient p : UserFactory.getDefaultPatients()) {
            repo.addPatient(p);
        }
        for (Doctor d : UserFactory.getDefaultDoctors()) {
            repo.addDoctor(d);
        }
    }

    @After
    public void cleanDatabase() {
        ClinicFlowApp app = (ClinicFlowApp) ApplicationProvider.getApplicationContext();
        for (Patient p : UserFactory.getDefaultPatients()) {
            app.getObjectCreation().deleteUser(p.getEmail());
        }
        for (Doctor d : UserFactory.getDefaultDoctors()) {
            app.getObjectCreation().deleteUser(d.getEmail());
        }
    }

    // ===== Login =====

    @Test
    public void login_emptyFields_staysOnLogin() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_wrongPassword_staysOnLogin() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(PATIENT_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText("wrongpassword"));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validPatient_goesToPatientScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(PATIENT_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(PATIENT_PASSWORD));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.bookAppointmentButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validDoctor_goesToDoctorScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(DOCTOR_EMAIL));
            onView(withId(R.id.PasswordEditText)).perform(replaceText(DOCTOR_PASSWORD));
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void signUpButton_opensSignUp() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.FirsNameEditText)).check(matches(isDisplayed()));
        }
    }

    // ===== Sign Up field visibility =====

    @Test
    public void signup_patientRole_showsPatientFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.PhoneEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.HealthCardEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.positionEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void signup_doctorRole_showsDoctorFields() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.DOCTOR);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.specializationEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.licenseNumberEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.PhoneEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.positionEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void signup_staffRole_showsStaffField() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.STAFF);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.positionEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.PhoneEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void signup_emptySubmit_staysOnScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);
        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }

    // ===== Appointment Detail button visibility =====

    private Appointment makeAppointment(AppointmentStatus status) {
        // Uses seeded doctor/patient emails so lookupService resolves their names.
        return new Appointment(
                DOCTOR_EMAIL, PATIENT_EMAIL,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(10, 30),
                status, "Checkup", "All clear.");
    }

    @Test
    public void appointmentDetail_patientView_showsCancelHidesComplete() {
        // XML: cancelButton visible, completeButton gone by default.
        // doctorView=false + CONFIRMED → code does not override those defaults.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES,       false);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(isDisplayed()));
            onView(withId(R.id.completeButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_doctorView_showsCompleteHidesCancel() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, true);
        intent.putExtra(NavigationExtras.NOTES,       true);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.completeButton)).check(matches(isDisplayed()));
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_completedStatus_hidesCancelButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT,  makeAppointment(AppointmentStatus.COMPLETED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES,       true);
        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    // ===== Search =====

    @Test
    public void search_knownPatient_showsCard() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,     NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText(PATIENT_EMAIL));
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void search_unknownEmail_cardHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE,     NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);
        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText("nobody@nowhere.com"));
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(not(isDisplayed())));
        }
    }

    // ===== Admin =====

    @Test
    public void admin_addDoctor_opensAddOrDeleteScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminScreen.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);
        try (ActivityScenario<AdminScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addDoctorButton)).perform(click());
            onView(withId(R.id.addButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addOrDelete_addButton_opensSignUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddOrDeleteScreen.class);
        intent.putExtra(NavigationExtras.USER_ROLE,        UserRole.DOCTOR);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);
        try (ActivityScenario<AddOrDeleteScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }
}
