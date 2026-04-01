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
public class DoctorFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void doctorLandingScreenTest() {
        login("johndoe@clinicdoc.com", "pass1");

        onView(withId(R.id.myAppointmentButton)).check(matches(isDisplayed()));
        onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
        onView(withId(R.id.patientRecordsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.myScheduleButton)).check(matches(isDisplayed()));
    }

    @Test
    public void doctorSetAvailabilityTest() {
        login("johndoe@clinicdoc.com", "pass1");

        onView(withId(R.id.setAvailabilityButton)).perform(click());

        onView(withId(R.id.dayEditText)).perform(click());
        onView(withText("Monday")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.dayEditText)).check(matches(withText("Monday")));

        onView(withId(R.id.startTimeEditText)).perform(click());
        onView(withText("08:00")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.startTimeEditText)).check(matches(withText("08:00")));

        onView(withId(R.id.endTimeEditText)).perform(click());
        onView(withText("12:00")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.endTimeEditText)).check(matches(withText("12:00")));

        onView(withId(R.id.submitButton)).perform(click());
        pressBack();

        onView(withId(R.id.setAvailabilityButton)).check(matches(isDisplayed()));
    }

    @Test
    public void doctorViewScheduleTest() {
        login("johndoe@clinicdoc.com", "pass1");

        onView(withId(R.id.myScheduleButton)).perform(click());
        onView(withId(R.id.mondayTime)).check(matches(isDisplayed()));
    }

    @Test
    public void doctorViewProfileTest() {
        login("johndoe@clinicdoc.com", "pass1");

        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.userCard)).check(matches(isDisplayed()));
        onView(withId(R.id.emailActual)).check(matches(withText("johndoe@clinicdoc.com")));
    }

    private void login(String email, String password) {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }
}