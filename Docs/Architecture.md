# ClinicFlow Architecture

## Major Packages and Source Code Components

### `com.example.clinicflow`
**Application / Composition Root**
- **`ClinicFlowApp`** (`android.app.Application`)
  - Creates and owns a single `UserRepository` instance for the lifetime of the app.
  - Allows files in presentation to use `getUserRepository()` so Activities can retrieve the shared repository.

---

### `Presentation`
**Responsibility:** Handles navigation between screens. Handles what to display and for whom.

**Key components**
- **Activities/Screens**
    - **Patient Screens**
        - `PatientScreen`, `PatientProfile`, `MyAppointments`, `BookAppointment`, ` MyRecords `
    - **Doctor Screens**
        - `DoctorScreen`, `DoctorProfile`, `MySchedule`, `SetAvailability`, ` Patient Records `
    - **Staff Screens**
        - `StaffScreen`, `StaffProfile`, `ViewDoctors`, `ManageAppointments`, `ViewPatients`
    - **Shared Screens**
        - `MainActivity`
        - ` MyRecords `
            - `MedicalRecordDetail`
            - Doctors and staff can access this page via ` Patient Records` and ` View Patients `
    - **Other**
        - `SignupScreen`
    - **UI helpers**
        - `RecyclerViewInterface`
        - `MedicalRecordAdapter`

**How it works**
- Any Activity that needs can retrieve the shared repository instance by obtaining the `ClinicFlowApp` and calling `getUserRepository()`.
- When an Activity needs to use business logic, it instantiates the required business class (e.g., `AuthService`, `MedicalHistory`, `ObjectCreation`) and passes the repository into it.

---

### `Business`
**Responsibility:** Implements application use-cases and rules using the UserRepository interface.

**Key components**
- **`AuthService`**
  - Uses an `authenticate` method to:
    1. Check basic email format
    2. Determine which role/type of email was entered
    3. Call the appropriate `validate[Role]` method
    4. If the user exists in the repository’s stored data for that role, return the corresponding `User` object
- **`MedicalHistory`**
  - Is responsible for communicating with UserRepository when patient records need to be viewed.
  - Retrieves a sorted list of `MedicalRecord` objects for a patient using the patient’s full name.
- **`ObjectCreation`**
  - Supports patient sign-up.
  - Accepts patient details:
    - `String firstName`, `String lastName`, `String email`, `String password`, `String gender`,
      `int age`, `int healthCardNum`, `int phoneNumber`
  - Checks if the patient already exists in the repository; if not, adds them.
  - Signing up logs the user in. They can logout and log back in so as long as the app is running.

---

### `Persistence` and `Fake`
**Responsibility:** Stores different users (`Patients`, `Doctors`, `Staff`) and the medical records of patients.

**Key components**
- **`UserRepository` (interface)**
  - Defines data operations for Users and MedicalRecords.
- **`FakeUserRepository` (implementation)**
  - Stores Users using an **ArrayList**.
  - Stores MedicalRecords using a **HashMap**.
  - Implements `UserRepository` so the rest of the application can depend on the abstraction.

---

### `Models`
**Responsibility:** Objects/Models that are shared across layers.

**Key components**
- **`Users` (abstract class)** with shared fields:
  - `String firstName`
  - `String lastName`
  - `String email`
  - `String password`
  - `String gender`
  - `int age`
- **Role-specific objects that implement `Users`**
  - `Patient`, `Doctor`, `Staff`
- **Other**
  - `MedicalRecord`

---

## High-Level Overview of Component Interaction

### General Flow
- `presentation` → `business`
- `business` → `persistence` (depends on `UserRepository` interface)
- `FakeUserRepository` → implements `UserRepository`
- `presentation`, `business`, `persistence` → use `models`
- `ClinicFlowApp` → provides shared `UserRepository`

---
