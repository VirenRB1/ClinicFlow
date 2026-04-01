# Manual UI Test Guide (Simplified)

---

## 1. Patient — Book Appointment

Steps:
1. Login as patient  
2. Book Appointment  
3. Select doctor  
4. Pick date (tomorrow)  
5. Find Slots  

Expected:
- Time slots appear  
- No “No slots” message  

6. Select a slot  

Expected:
- Confirm screen shows correct info  

7. Confirm with empty purpose  

Expected:
- Stay on screen (error)  

8. Enter purpose → Confirm  

Expected:
- Back to previous screen  
- Shows in My Appointments  

---

## 2. Patient — No Slots

Steps:
1. Login  
2. Book Appointment  
3. Select doctor (no availability)  
4. Pick date → Find Slots  

Expected:
- No slots shown  
- “No slots available” visible  

---

## 3. Doctor — Set Availability

Steps:
1. Login  
2. Set Availability  
3. Select day  
4. Start: 09:00  
5. End: 17:00  
6. Submit  

Expected:
- Success message  

Check:
- My Schedule shows correct time  

---

## 4. Doctor — Validation

Invalid time:
- Start 14:00, End 09:00  

Expected:
- Error  
- Not saved  

Overlap:
- Add overlapping time  

Expected:
- Error  
- Not saved  

---

## 5. Staff — Book for Patient

Steps:
1. Login  
2. Manage → Book Appointment  
3. Search patient  

Expected:
- Patient card appears  

4. Book  
5. Complete booking  

Expected:
- Appointment created under patient  

---

## 6. Staff — Cancel Appointment

Steps:
1. Login  
2. Manage → Cancel Appointment  
3. Search patient  
4. Open → Cancel  

Expected:
- Appointment removed  

---

## 7. Doctor — Complete Appointment

Steps:
1. Login  
2. Open appointment  
3. Add note  
4. Complete  

Expected:
- Success message  
- Removed from upcoming  

Check:
- Notes visible to patient (read-only)  

---

## 8. Admin — Add / Delete User

Add:
1. Login  
2. Add Patient → Add  
3. Fill form → Sign Up  

Expected:
- Success message  

Delete:
1. Add Patient → Delete  
2. Search user  
3. Delete  

Expected:
- Success message  

---

## 9. Toast Checks

Check manually:
- availability saved  
- overlap error  
- booking / cancel / complete  
- add / delete user  
- search not found  

---

## 10. Visual Checks

Check:
- layout not broken  
- buttons aligned  
- slots scroll properly  
- notes field readable  
- schedule shows all days  