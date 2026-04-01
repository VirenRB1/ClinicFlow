package com.example.clinicflow.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.NoMatchingViewException;

import android.widget.DatePicker;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.clinicflow.R;
import com.example.clinicflow.presentation.authScreens.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class PatientFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void patientLandingScreenTest() {
        login("alicebrown@gmail.com", "pass4");

        onView(withId(R.id.myAppointmentsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.bookAppointmentButton)).check(matches(isDisplayed()));
        onView(withId(R.id.myRecordsButton)).check(matches(isDisplayed()));
    }

    @Test
    public void patientBookAppointmentTest() {
        LocalDate nextMonday = ensureDoctorAvailability();

        login("alicebrown@gmail.com", "pass4");

        onView(withId(R.id.bookAppointmentButton)).perform(click());

        onView(withId(R.id.doctorEditText)).perform(click());
        onView(withText("John Doe")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.doctorEditText)).check(matches(withText("John Doe")));

        onView(withId(R.id.dateEditText)).perform(click());
        onView(isAssignableFrom(DatePicker.class))
                .perform(setDate(nextMonday.getYear(), nextMonday.getMonthValue(), nextMonday.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.findSlotsButton)).perform(click());
        onView(withId(R.id.slotsRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.slotsRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.nameActual)).check(matches(withText("John Doe")));
        onView(withId(R.id.emailActual)).check(matches(withText("johndoe@clinicdoc.com")));

        onView(withId(R.id.purposeEditText))
                .perform(replaceText("Annual checkup"), closeSoftKeyboard());
        onView(withId(R.id.confirmButton)).perform(click());
    }

    @Test
    public void patientCancelAppointmentTest() {
        LocalDate nextMonday = ensureDoctorAvailability();
        createPatientAppointment(nextMonday, "Cancel checkup");

        onView(withId(R.id.myAppointmentsButton)).perform(click());
        onView(withId(R.id.recordsRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.recordsRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.cancelButton)).perform(click());
    }

    @Test
    public void patientViewRecordsTest() {
        login("alicebrown@gmail.com", "pass4");

        onView(withId(R.id.myRecordsButton)).perform(click());
        onView(withId(R.id.recordsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void patientViewProfileTest() {
        login("alicebrown@gmail.com", "pass4");

        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.userCard)).check(matches(isDisplayed()));
        onView(withId(R.id.emailActual)).check(matches(withText("alicebrown@gmail.com")));
    }

    private LocalDate ensureDoctorAvailability() {
        LocalDate nextMonday = getNextMonday();

        login("johndoe@clinicdoc.com", "pass1");

        onView(withId(R.id.setAvailabilityButton)).perform(click());

        onView(withId(R.id.dayEditText)).perform(click());
        onView(withText("Monday")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.startTimeEditText)).perform(click());
        onView(withText("08:00")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.endTimeEditText)).perform(click());
        onView(withText("12:00")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.submitButton)).perform(click());

        // If availability already exists, the Replace dialog will appear — click Replace
        try {
            onView(withText("Replace")).inRoot(isDialog()).perform(click());
        } catch (NoMatchingViewException ignored) {
            // No overlap dialog — availability was added fresh
        }

        pressBack();
        pressBack();

        return nextMonday;
    }

    private void createPatientAppointment(LocalDate date, String purpose) {
        login("alicebrown@gmail.com", "pass4");

        onView(withId(R.id.bookAppointmentButton)).perform(click());

        onView(withId(R.id.doctorEditText)).perform(click());
        onView(withText("John Doe")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.dateEditText)).perform(click());
        onView(isAssignableFrom(DatePicker.class))
                .perform(setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.findSlotsButton)).perform(click());
        onView(withId(R.id.slotsRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.purposeEditText))
                .perform(replaceText(purpose), closeSoftKeyboard());
        onView(withId(R.id.confirmButton)).perform(click());
        pressBack();
        pressBack();
    }

    private void login(String email, String password) {
        onView(withId(R.id.EmailAddressEditText))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.PasswordEditText))
                .perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    private LocalDate getNextMonday() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            date = date.plusDays(1);
        }
        return date;
    }
}