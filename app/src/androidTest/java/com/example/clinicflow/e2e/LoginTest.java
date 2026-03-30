package com.example.clinicflow.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.persistence.real.AppDbHelper;
import com.example.clinicflow.presentation.authScreens.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void clearDb() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(AppDbHelper.DATABASE_NAME);
    }

    @Test
    public void loginAsAdmin_navigatesToAdminScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("admin@clinic.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.addDoctorButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addStaffButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addPatientButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginAsDoctor_navigatesToDoctorScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("johndoe@clinicdoc.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.myAppointmentButton)).check(matches(isDisplayed()));
        onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
        onView(withId(R.id.patientRecordsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.myScheduleButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginAsPatient_navigatesToPatientScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("alicebrown@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass4"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.myAppointmentsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.bookAppointmentButton)).check(matches(isDisplayed()));
        onView(withId(R.id.myRecordsButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginAsStaff_navigatesToStaffScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("evemiller@clinicstaff.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass7"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.manageAppointmentsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPatientsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPhysiciansButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithWrongPassword_staysOnLoginScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("admin@clinic.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("wrongpassword"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithUnknownEmail_staysOnLoginScreen() {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText("nobody@nowhere.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithEmptyFields_staysOnLoginScreen() {
        onView(withId(R.id.loginButton)).perform(click());

        SystemClock.sleep(500);

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSignUp_navigatesToUserSignUpAsPatient() {
        onView(withId(R.id.signUpButton)).perform(click());

        SystemClock.sleep(500);

        // Common fields always visible
        onView(withId(R.id.FirsNameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.LastNameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.EmailAddressEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.PasswordEditText)).check(matches(isDisplayed()));

        // Patient-specific fields visible since sign up defaults to PATIENT role
        onView(withId(R.id.HealthCardEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.PhoneEditText)).check(matches(isDisplayed()));
    }
}