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
public class StaffFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void staffLandingScreenTest() {
        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.manageAppointmentsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPatientsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPhysiciansButton)).check(matches(isDisplayed()));
    }

    @Test
    public void staffBookAppointmentForPatientTest() {
        LocalDate nextMonday = ensureDoctorAvailability();

        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.manageAppointmentsButton)).perform(click());
        onView(withId(R.id.bookAppointmentButton)).perform(click());

        onView(withId(R.id.editTextEmailAddress))
                .perform(replaceText("alicebrown@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        onView(withId(R.id.actionButton)).perform(click());

        onView(withId(R.id.doctorEditText)).perform(click());
        onView(withText("John Doe")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.dateEditText)).perform(click());
        onView(isAssignableFrom(DatePicker.class))
                .perform(setDate(nextMonday.getYear(), nextMonday.getMonthValue(), nextMonday.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.findSlotsButton)).perform(click());
        onView(withId(R.id.slotsRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.purposeEditText))
                .perform(replaceText("Follow-up visit"), closeSoftKeyboard());
        onView(withId(R.id.confirmButton)).perform(click());
    }

    @Test
    public void staffCancelAppointmentForPatientTest() {
        LocalDate nextMonday = ensureDoctorAvailability();
        createStaffAppointment(nextMonday, "Staff cancel visit");

        onView(withId(R.id.cancelAppointmentButton)).perform(click());

        onView(withId(R.id.editTextEmailAddress))
                .perform(replaceText("alicebrown@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        onView(withId(R.id.actionButton)).perform(click());

        onView(withId(R.id.recordsRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.recordsRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.cancelButton)).perform(click());
    }

    @Test
    public void staffViewPatientTest() {
        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.viewPatientsButton)).perform(click());

        onView(withId(R.id.editTextEmailAddress))
                .perform(replaceText("alicebrown@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.patientCard)).check(matches(isDisplayed()));
        onView(withId(R.id.nameActual)).check(matches(withText("Alice Brown")));
        onView(withId(R.id.healthCardActual)).check(matches(isDisplayed()));
        onView(withId(R.id.phoneActual)).check(matches(isDisplayed()));
    }

    @Test
    public void staffViewPhysiciansScreenTest() {
        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.viewPhysiciansButton)).perform(click());
        onView(withId(R.id.profileButton)).check(matches(isDisplayed()));
    }

    @Test
    public void staffViewProfileTest() {
        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.userCard)).check(matches(isDisplayed()));
        onView(withId(R.id.emailActual)).check(matches(withText("evemiller@clinicstaff.com")));
        onView(withId(R.id.positionActual)).check(matches(withText("Receptionist")));
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

    private void createStaffAppointment(LocalDate date, String purpose) {
        login("evemiller@clinicstaff.com", "pass7");

        onView(withId(R.id.manageAppointmentsButton)).perform(click());
        onView(withId(R.id.bookAppointmentButton)).perform(click());

        onView(withId(R.id.editTextEmailAddress))
                .perform(replaceText("alicebrown@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.actionButton)).perform(click());

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