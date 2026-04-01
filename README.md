# GROUP 14 PROJECT - Physician's Office System

Please find our group work agreement at the link below
[Group-Work-Agreement.md](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g14-booleanhooligans/-/tree/main/Docs)
## Group Members
1. Viren Ravji Bhanderi - 7976692
2. Najma Mohamed - 7934223
3. Gurwinder Khandal - 7909492
4. Hoang Phuc - 7900499
5. Israel Ijiekhuamen - 7909801

## Project Information
Project Summary: This project delivers a secure, role-based Physician's Office System that manages patient records, appointments, and clinic workflows for physicians, office staff, and patients.

This system keeps accurate, current records of physicians' schedules, patient data, staff information, and clinical visits. This helps office staff and physicians manage their work more efficiently and leads to a more organized approach to scheduling and data management.

This app is designed for physicians, patients, and office staff, and it allows the office to go paperless in its approach to storing data and handling its day-to-day activity. Physicians use the system to set and manage their weekly availability, view their upcoming appointments, mark appointments as completed, and add clinical notes for completed visits. Office staff use the system to book and cancel appointments on behalf of patients, and to look up patient and doctor records. Patients can create their own accounts, book appointments with available doctors by choosing from open time slots, view their upcoming and past appointments along with any clinical notes from the doctor, and cancel appointments they no longer need.

The system maintains patient records that include personal information, appointment history, and clinical notes from completed visits. It supports appointment scheduling while preventing conflicts in physician availability. The system automatically generates available time slots based on each doctor's weekly schedule and prevents double-booking, booking outside availability windows, and booking beyond a two-week window. When a doctor updates their availability and it overlaps with existing hours, the system detects the conflict and allows the doctor to replace the old availability, automatically cancelling any affected appointments. Access to information is controlled through role-based security, ensuring that data is only accessible to authorized users. This role-based access is given at the start via login and authentication. Patients can create an account freely, but only the admin can add physician and office staff accounts to the system. This system does not provide diagnostic or treatment decision support. Its focus is on improving workflow efficiency within a physician's office rather than supporting complex hospital environments.

The value this system adds is to reduce administrative overhead, minimize scheduling errors, and improve access to accurate patient information by centralizing all clinic operations in a secure digital platform. Physicians spend less time on paperwork and can manage their own availability directly, office staff can manage appointments on behalf of patients more reliably, and patients experience faster scheduling and better continuity of care through access to their appointment history and doctor notes.

This project will be considered successful if:
1. Office staff can schedule or cancel patient appointments in under one minute.
2. The system prevents double-booking of physicians 100% of the time.
3. Physicians can view their upcoming appointments and add clinical notes within five clicks after logging in.
4. Patients can book appointments and view appointment details without staff intervention.
5. Access to system features and data is restricted by user role, and unauthorized users cannot view or edit protected information.