# Manual UI Test Guide

Tests in this document require **in-person observation on a running device or emulator**. They cannot be automated with Espresso because they depend on Android system dialogs (DatePickerDialog, spinner/dropdown dialogs) or require multi-step flows where intermediate state is controlled by those dialogs.

Before each session: install a fresh build, or clear app storage via Settings → Apps → ClinicFlow → Clear Storage to start with a clean database.

---

## Test Accounts to Create First

Use the Admin account to seed the required test data before running the flows below. Log in as Admin and create:

| Role    | Email                        | Password  | Extra fields                          |
|---------|------------------------------|-----------|---------------------------------------|
| Doctor  | testdoc@clinic.com           | pass123   | Specialization: Cardiology, License: LIC10001 |
| Patient | testpatient@clinic.com       | pass123   | Phone: 4165550001, Health Card: 100000001 |
| Staff   | teststaff@clinic.com         | pass123   | Position: Receptionist                |

---

## 1. Patient — Book Appointment Flow

**Goal:** Verify the full booking journey from PatientScreen to confirmation.

**Pre-condition:** Doctor (`testdoc@clinic.com`) has availability set for tomorrow's day of the week (set this up via Doctor flow in Section 3 first).

**Steps:**
1. Log in as `testpatient@clinic.com` / `pass123`.
2. Tap **Book Appointment**.
3. Tap the Doctor field — a dropdown list of doctors should appear. Select `testdoc@clinic.com`.
4. Tap the Date field — the system DatePicker should open. Select **tomorrow's date**.
5. Tap **Find Slots**.

**Expected:** A list of 30-minute time slots appears. "No slots available" text is **not** shown.

6. Tap any available time slot.

**Expected:** The Confirm Appointment screen opens. Doctor name, email, date, start time, and end time are pre-filled and correct.

7. Leave the Purpose field **empty** and tap **Confirm**.

**Expected:** The app does **not** navigate away. An error message (Toast or inline) indicates the purpose is required.

8. Type a purpose (e.g., "Routine checkup") and tap **Confirm**.

**Expected:** The app returns to the previous screen. The booked appointment appears when navigating to **My Appointments**.

---

## 2. Patient — No Slots Available State

**Goal:** Verify the empty state when a doctor has no availability.

**Pre-condition:** A doctor with **no** availability set up exists in the system.

**Steps:**
1. Log in as `testpatient@clinic.com` / `pass123`.
2. Tap **Book Appointment**.
3. Select a doctor that has no availability.
4. Select any date.
5. Tap **Find Slots**.

**Expected:** The slots list is empty and "No slots available" (or equivalent) text is visible.

---

## 3. Doctor — Set Availability Flow

**Goal:** Verify a doctor can set working hours for a day.

**Steps:**
1. Log in as `testdoc@clinic.com` / `pass123`.
2. Tap **Set Availability**.
3. Tap the **Day** dropdown — a list (Monday–Sunday) should appear. Select **tomorrow's day of the week**.
4. Tap the **Start Time** dropdown — an hourly list (08:00–20:00) should appear. Select **09:00**.
5. Tap the **End Time** dropdown. Select **17:00**.
6. Tap **Submit**.

**Expected:** A success message (Toast) confirms availability was saved.

7. Navigate back and tap **My Schedule**.

**Expected:** Tomorrow's day shows "09:00 – 17:00" (or equivalent format). All other days remain blank or unchanged.

---

## 4. Doctor — Set Availability Validation

**Goal:** Verify the form rejects invalid time combinations.

**Steps (End time before start time):**
1. Log in as `testdoc@clinic.com` / `pass123` and navigate to **Set Availability**.
2. Select any day, set Start Time to **14:00**, End Time to **09:00**.
3. Tap **Submit**.

**Expected:** An error message is shown. Availability is **not** saved.

**Steps (Overlapping slot):**
1. After a successful availability save (e.g., Mon 09:00–17:00 from Section 3), open **Set Availability** again.
2. Select the same day, set Start Time **12:00**, End Time **15:00**.
3. Tap **Submit**.

**Expected:** An error message about overlapping availability is shown. The duplicate slot is **not** saved.

---

## 5. Staff — Book Appointment for Patient

**Goal:** Verify staff can search for a patient and initiate a booking on their behalf.

**Pre-condition:** Doctor has availability set (Section 3).

**Steps:**
1. Log in as `teststaff@clinic.com` / `pass123`.
2. Tap **Manage Appointments** → **Book Appointment**.
3. In the Search field, enter `testpatient@clinic.com` and tap **Search**.

**Expected:** The patient card appears showing the patient's name, email, gender, age, phone, and health card.

4. Tap **Book**.

**Expected:** The Book Appointment screen opens. The flow continues identically to Section 1 (select doctor → select date → select slot → confirm). The appointment is booked under the patient's account.

5. Log out and log in as `testpatient@clinic.com`. Navigate to **My Appointments**.

**Expected:** The appointment booked by the staff member is visible.

---

## 6. Staff — Cancel Appointment for Patient

**Goal:** Verify staff can cancel a patient's upcoming appointment.

**Pre-condition:** `testpatient@clinic.com` has at least one upcoming appointment.

**Steps:**
1. Log in as `teststaff@clinic.com` / `pass123`.
2. Tap **Manage Appointments** → **Cancel Appointment**.
3. Search for `testpatient@clinic.com` and tap **Search**.
4. Tap **Cancel Appointment**.

**Expected:** The patient's upcoming appointments list is shown. Tap the appointment to open its detail view, then tap **Cancel**.

**Expected:** The appointment is cancelled and no longer appears in the upcoming list.

---

## 7. Doctor — Complete an Appointment with Notes

**Goal:** Verify a doctor can mark an appointment complete and add notes.

**Pre-condition:** `testpatient@clinic.com` has an upcoming confirmed appointment with `testdoc@clinic.com`.

**Steps:**
1. Log in as `testdoc@clinic.com` / `pass123`.
2. Tap **My Appointments**.
3. Tap the appointment with `testpatient@clinic.com`.

**Expected:** The detail screen shows the **Complete** button. The **Cancel** button is **not** visible. The doctor notes field is editable.

4. Type a note: "Patient is healthy, no follow-up required."
5. Tap **Complete**.

**Expected:** A success Toast ("Appointment Completed") appears and the screen closes. The appointment no longer appears in the upcoming list.

6. Log out and log in as `testpatient@clinic.com`. Navigate to **My Records** (past appointments).

**Expected:** The completed appointment appears. Opening it shows the notes written by the doctor. The notes field is **read-only** (not editable for the patient).

---

## 8. Admin — Add and Delete a User

**Goal:** Verify the full admin user management cycle.

**Steps (Add):**
1. Log in as Admin.
2. Tap **Add Patient** → **Add**.
3. Fill in all required fields. For DOB, tap the date field and use the DatePicker to select a valid past date.
4. Tap **Sign Up**.

**Expected:** A success Toast appears and the screen closes. The new patient can log in with the credentials just created.

**Steps (Delete):**
1. Still as Admin, tap **Add Patient** → **Delete**.
2. Enter the email of the patient just created and tap **Search**.

**Expected:** The patient's card appears.

3. Tap **Delete**.

**Expected:** A success Toast ("User Deleted") appears. The card disappears. Attempting to log in as the deleted user fails.

---

## 9. Toast Message Verification

The following success/error toasts must be checked in-person because Espresso toast assertions are unreliable on API 30+:

| Action                                   | Expected Toast text                    |
|------------------------------------------|----------------------------------------|
| Successful login with wrong role         | (error message from AuthService)       |
| Availability saved                       | (success confirmation)                 |
| Availability overlap                     | (overlap error message)                |
| Appointment booked (confirm screen)      | (success or navigates back)            |
| Appointment cancelled                    | "Appointment Cancelled"                |
| Appointment completed                    | "Appointment Completed"                |
| User added by Admin                      | "[ROLE] added successfully"            |
| User deleted by Admin                    | "User Deleted"                         |
| Search with unknown email                | "No Such Patient" / "No Such Doctor"   |

---

## 10. Visual / Layout Spot Checks

These require human eyes to judge correctness and cannot be asserted with Espresso:

- **All screens:** Confirm the top bar (profile icon, logout button) renders without overlap on different screen sizes.
- **PatientScreen / DoctorScreen / StaffScreen / AdminScreen:** Landing page buttons are correctly labelled and evenly spaced.
- **Slots screen:** Time slots scroll smoothly when there are many available slots.
- **AppointmentDetail:** Doctor notes field is visually distinct (multi-line, scrollable) and not clipped.
- **My Schedule:** All seven days are displayed. Days with no availability show a blank or placeholder, not an error.
- **Profile screen:** All role-specific fields align consistently with common fields.
