package com.example.clinicflow.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.authScreens.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void adminLandingScreenTest() {
        login("admin@clinic.com", "admin");

        onView(withId(R.id.addDoctorButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addStaffButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addPatientButton)).check(matches(isDisplayed()));
    }

    @Test
    public void adminAddDoctorTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        String doctorEmail = "sarah" + suffix + "@clinicdoc.com";

        login("admin@clinic.com", "admin");

        onView(withId(R.id.addDoctorButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.specializationEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.licenseNumberEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.FirsNameEditText))
                .perform(replaceText("Sarah"), closeSoftKeyboard());
        onView(withId(R.id.LastNameEditText))
                .perform(replaceText("Connor"), closeSoftKeyboard());
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(doctorEmail), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordConfirmEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.GenderEditText))
                .perform(replaceText("Female"), closeSoftKeyboard());

        onView(withId(R.id.DobEditText)).perform(click());
        onView(withText("OK")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.specializationEditText))
                .perform(replaceText("CARDIOLOGY"), closeSoftKeyboard());
        onView(withId(R.id.licenseNumberEditText))
                .perform(replaceText("LIC" + suffix), closeSoftKeyboard());

        onView(withId(R.id.signUpButton)).perform(click());
        pressBack();

        onView(withId(R.id.addDoctorButton)).check(matches(isDisplayed()));
    }

    @Test
    public void adminAddStaffTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        String staffEmail = "tom" + suffix + "@clinicstaff.com";

        login("admin@clinic.com", "admin");

        onView(withId(R.id.addStaffButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.positionEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.FirsNameEditText))
                .perform(replaceText("Tom"), closeSoftKeyboard());
        onView(withId(R.id.LastNameEditText))
                .perform(replaceText("Hanks"), closeSoftKeyboard());
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(staffEmail), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordConfirmEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.GenderEditText))
                .perform(replaceText("Male"), closeSoftKeyboard());

        onView(withId(R.id.DobEditText)).perform(click());
        onView(withText("OK")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.positionEditText))
                .perform(replaceText("Receptionist"), closeSoftKeyboard());

        onView(withId(R.id.signUpButton)).perform(click());
        pressBack();

        onView(withId(R.id.addStaffButton)).check(matches(isDisplayed()));
    }

    @Test
    public void adminAddPatientTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        String patientEmail = "jane" + suffix + "@gmail.com";

        login("admin@clinic.com", "admin");

        onView(withId(R.id.addPatientButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.HealthCardEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.PhoneEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.FirsNameEditText))
                .perform(replaceText("Jane"), closeSoftKeyboard());
        onView(withId(R.id.LastNameEditText))
                .perform(replaceText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(patientEmail), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordConfirmEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.GenderEditText))
                .perform(replaceText("Female"), closeSoftKeyboard());

        onView(withId(R.id.DobEditText)).perform(click());
        onView(withText("OK")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.HealthCardEditText))
                .perform(replaceText("111222333"), closeSoftKeyboard());
        onView(withId(R.id.PhoneEditText))
                .perform(replaceText("2045550000"), closeSoftKeyboard());

        onView(withId(R.id.signUpButton)).perform(click());
        pressBack();

        onView(withId(R.id.addPatientButton)).check(matches(isDisplayed()));
    }

    @Test
    public void adminDeleteDoctorTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        String doctorEmail = "delete" + suffix + "@clinicdoc.com";

        login("admin@clinic.com", "admin");

        onView(withId(R.id.addDoctorButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.FirsNameEditText))
                .perform(replaceText("Delete"), closeSoftKeyboard());
        onView(withId(R.id.LastNameEditText))
                .perform(replaceText("Me"), closeSoftKeyboard());
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(doctorEmail), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordConfirmEditText))
                .perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.GenderEditText))
                .perform(replaceText("Male"), closeSoftKeyboard());

        onView(withId(R.id.DobEditText)).perform(click());
        onView(withText("OK")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.specializationEditText))
                .perform(replaceText("CARDIOLOGY"), closeSoftKeyboard());
        onView(withId(R.id.licenseNumberEditText))
                .perform(replaceText("LICDEL" + suffix), closeSoftKeyboard());

        onView(withId(R.id.signUpButton)).perform(click());
        pressBack();

        onView(withId(R.id.addDoctorButton)).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());

        onView(withId(R.id.editTextEmailAddress))
                .perform(replaceText(doctorEmail), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        onView(withId(R.id.nameActual)).check(matches(withText("Delete Me")));

        onView(withId(R.id.actionButton)).perform(click());
    }

    @Test
    public void adminViewProfileTest() {
        login("admin@clinic.com", "admin");

        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.userCard)).check(matches(isDisplayed()));
        onView(withId(R.id.emailActual)).check(matches(withText("admin@clinic.com")));
    }

    private void login(String email, String password) {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }
}