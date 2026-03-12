# ClinicFlow Architecture

## Major Packages and Source Code Components

### `com.example.clinicflow`
**Application / Composition Root**
- **`ClinicFlowApp`** (`android.app.Application`)
    - Creates and owns a single `UserRepository` instance for the lifetime of the app.
    - Creates and initializes a series of business services (AuthService, LoopupService, AppointmentService, DocAvailabilityService) and a ObjectCreation class which is needed for user signup and deletion
    - Allows files in presentation to use `get[Service]` so Activities can retrieve the necessary service that is initialized at app start.

---

### `Presentation`
**Responsibility:** Handles navigation between screens. Handles what to display and for whom.

**Key components**
- **subpackages**
    - **authScreens**
        - `LoginNav`, `MainActivity`
    - **adminScreens**
        - `AddOrDeleteScreen`, `AdminScreen`, `UserDelete`, `UserSignUp`
    - **patientScreens**
        - `PatientScreen`, `ConfirmAppointment`, `BookAppointment`, ` Slots `
    - **doctorScreens**
        - `DoctorScreen`, `MySchedule`, `SetAvailability`
    - **staffScreens**
        - `StaffScreen`, `ViewDoctors`, `ManageAppointments`
    - **sharedScreens**
        - `MyAppointments`, `AppointmentDetail`, `Profile`, `ViewPatients`
    - **adapters**
        - `AppointmentAdapter`, `TimeSlotAdapter`
- **Files Not in Subpackages**
    - `Navigation`, `BasicBinds`

**How it works**
- Any Activity that needs can retrieve any shared service from ClinicFlowApp that they need.
- When an Activity needs to use business logic, it calls the necessary method through that shared service it retrieved from ClinicFlowApp
- Navigation assists in navigation from one screen to another
- BasicBinds binds the logout, back, and profile buttons to their respective IDs and then sets the onClickListeners for them. Only screens that have all 3 of these buttons use this

---

### `Business`
**Responsibility:** Implements application use-cases and rules using the UserRepository interface.

**Key components**
- **subpackages**
    - **validators**
        - `UserAuthenticator Interface`
        - `UniversalAuthenticator`
        - `CredentialsValidator`
        - `AvailabilityValidator`
        - `UserSignupValidator`
    - **services**
        - `AppointmentService`
        - `DocAvailabilityService`
        - `LookupService`
        - `AuthService`
    - **exceptions**
        - `AuthExceptions`, `ValidationExceptions`
    - **creation**
        - `ObjectCreation`
---

### `Persistence`(`Fake` & `Real`)
**Responsibility:** Stores different users (`Patients`, `Doctors`, `Staff`), `Appointments` (Past and Future), `DoctorAvailability`

**Key components**
- **`UserRepository` (interface)**
    - Defines data operations.
- **subpackages**
    - **fake**
        - **`FakeUserRepository` (implementation)**
            - Stores data using arraylists and hashmaps
    - **real**
        - **`RealUserRepository` (implementation)**
            - Stores data using SQL tables
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
    - `Patient`, `Doctor`, `Staff`, `Admin`
- `Appointment`
- `DoctorAvailability`
- `TimeSlot`

---

## High-Level Overview of Component Interaction

### Dependency
- `presentation` → `business`
- `business` → `persistence` (depends on `UserRepository` interface)
- `FakeUserRepository` → implements `UserRepository`
- `RealUserRepository` → implements `UserRepository`
- `presentation`, `business`, `persistence` → use `models`
- `ClinicFlowApp` → provides shared services instead of direct access to persistence layer
---
