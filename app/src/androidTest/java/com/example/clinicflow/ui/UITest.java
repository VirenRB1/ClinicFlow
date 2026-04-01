package com.example.clinicflow.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.application.ClinicFlowApp;
import com.example.clinicflow.business.exceptions.ValidationExceptions;
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.models.AppointmentStatus;
import com.example.clinicflow.models.Specialization;
import com.example.clinicflow.models.UserRole;
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

@RunWith(AndroidJUnit4.class)
public class UITest {

    private static final String PATIENT_EMAIL = "alicebrown@gmail.com";
    private static final String PATIENT_PASSWORD = "pass4";
    private static final String DOCTOR_EMAIL = "johndoe@clinicdoc.com";
    private static final String DOCTOR_PASSWORD = "pass1";

    private ClinicFlowApp app;

    @Before
    public void setup() throws ValidationExceptions.ValidationException {
        Context context = ApplicationProvider.getApplicationContext();
        app = (ClinicFlowApp) context;

        app.getObjectCreation().deleteUser(PATIENT_EMAIL);
        app.getObjectCreation().deleteUser(DOCTOR_EMAIL);

        app.getObjectCreation().addPatientToDatabase(
                "Alice", "Brown", PATIENT_EMAIL, PATIENT_PASSWORD, PATIENT_PASSWORD,
                "Female", LocalDate.of(2000, 1, 1), "123456789", "2045551234"
        );

        app.getObjectCreation().addDoctorToDatabase(
                "John", "Doe", DOCTOR_EMAIL, DOCTOR_PASSWORD, DOCTOR_PASSWORD,
                "Male", LocalDate.of(1999, 3, 12), Specialization.CARDIOLOGY, "LIC12345"
        );
    }

    @After
    public void cleanup() {
        app.getObjectCreation().deleteUser(PATIENT_EMAIL);
        app.getObjectCreation().deleteUser(DOCTOR_EMAIL);
    }

    // Login

    @Test
    public void login_emptyFields_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_wrongPassword_staysOnLoginScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(PATIENT_EMAIL), closeSoftKeyboard());
            onView(withId(R.id.PasswordEditText)).perform(replaceText("wrongpassword"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validPatient_navigatesToPatientScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(PATIENT_EMAIL), closeSoftKeyboard());
            onView(withId(R.id.PasswordEditText)).perform(replaceText(PATIENT_PASSWORD), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.bookAppointmentButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_validDoctor_navigatesToDoctorScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.EmailAddressEditText)).perform(replaceText(DOCTOR_EMAIL), closeSoftKeyboard());
            onView(withId(R.id.PasswordEditText)).perform(replaceText(DOCTOR_PASSWORD), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void login_signUpButton_opensSignUpScreen() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.FirsNameEditText)).check(matches(isDisplayed()));
        }
    }

    // UserSignUp

    @Test
    public void signup_patientRole_showsPatientFieldsOnly() {
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
    public void signup_doctorRole_showsDoctorFieldsOnly() {
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
    public void signup_staffRole_showsStaffFieldOnly() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.STAFF);

        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.positionEditText)).check(matches(isDisplayed()));
            onView(withId(R.id.specializationEditText)).check(matches(not(isDisplayed())));
            onView(withId(R.id.PhoneEditText)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void signup_emptySubmit_staysOnSignUpScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserSignUp.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.PATIENT);

        try (ActivityScenario<UserSignUp> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signUpButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }

    // AppointmentDetail

    private Appointment makeAppointment(AppointmentStatus status) {
        return new Appointment(
                DOCTOR_EMAIL,
                PATIENT_EMAIL,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                status,
                "Checkup",
                "All clear."
        );
    }

    @Test
    public void appointmentDetail_patientView_cancelVisibleCompleteHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT, makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES, false);

        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(isDisplayed()));
            onView(withId(R.id.completeButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_doctorView_completeVisibleCancelHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT, makeAppointment(AppointmentStatus.CONFIRMED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, true);
        intent.putExtra(NavigationExtras.NOTES, true);

        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.completeButton)).check(matches(isDisplayed()));
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void appointmentDetail_completedStatus_cancelAlwaysHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppointmentDetail.class);
        intent.putExtra(NavigationExtras.EXTRA_APPT, makeAppointment(AppointmentStatus.COMPLETED));
        intent.putExtra(NavigationExtras.DOCTOR_VIEW, false);
        intent.putExtra(NavigationExtras.NOTES, true);

        try (ActivityScenario<AppointmentDetail> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())));
        }
    }

    // SearchUserCard

    @Test
    public void search_knownPatient_cardBecomesVisible() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE, NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);

        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText(PATIENT_EMAIL), closeSoftKeyboard());
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void search_unknownEmail_cardRemainsHidden() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchUserCard.class);
        intent.putExtra(NavigationExtras.SEARCH_MODE, NavigationExtras.MODE_VIEW_PATIENT);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);

        try (ActivityScenario<SearchUserCard> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmailAddress)).perform(replaceText("nobody@nowhere.com"), closeSoftKeyboard());
            onView(withId(R.id.searchButton)).perform(click());
            onView(withId(R.id.patientCard)).check(matches(not(isDisplayed())));
        }
    }

    // Admin navigation

    @Test
    public void admin_addDoctorButton_opensAddOrDeleteScreen() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminScreen.class);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);

        try (ActivityScenario<AdminScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addDoctorButton)).perform(click());
            onView(withId(R.id.addButton)).check(matches(isDisplayed()));
            onView(withId(R.id.deleteButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addOrDelete_addButton_opensUserSignUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddOrDeleteScreen.class);
        intent.putExtra(NavigationExtras.USER_ROLE, UserRole.DOCTOR);
        intent.putExtra(NavigationExtras.EXTRA_USER_EMAIL, DOCTOR_EMAIL);

        try (ActivityScenario<AddOrDeleteScreen> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.addButton)).perform(click());
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        }
    }
}