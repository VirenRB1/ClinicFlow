# ClinicFlow Architecture

## Major Packages and Source Code Components
## Architecture Diagram

![ClinicFlow Architecture](ArchitectureDiagram.jpg)
### `com.example.clinicflow`

**Application / Composition Root**

* **`ClinicFlowApp`**

    * Initializes shared application-level objects.

---

### `Presentation`

**Responsibility:** Presentation-layer classes and navigation-related helpers.

**Subpackages**

* **`admin`**

    * `AddOrDeleteScreen`
    * `AdminScreen`
    * `UserSignUp`

* **`authScreens`**

    * `LoginNav`
    * `MainActivity`

* **`doctorScreens`**

    * `DoctorScreen`
    * `SetAvailability`

* **`patientScreens`**

    * `BookAppointment`
    * `ConfirmAppointment`
    * `PatientScreen`
    * `Slots`

* **`staffScreens`**

    * `ManageAppointments`
    * `StaffScreen`

* **`sharedScreens`**

  * `AppointmentDetail`
  * `MyAppointments`
  * `MySchedule`
  * `Profile`
  * `ProfileView`
  * `SearchUserCard`
  * `SearchUserCardView`
  * `SearchUserModeFactory`

  * **`searchModeHandlers`**

    * `BaseSearchUserModeHandler`
    * `BookAppointmentModeHandler`
    * `CancelAppointmentModeHandler`
    * `DeleteUserModeHandler`
    * `ViewDoctorModeHandler`
    * `ViewPatientRecordsModeHandler`
    * **Interface:** `SearchUserModeHandler`

* **`adapters`**

    * `AppointmentAdapter`
    * `TimeSlotAdapter`

**Files not in subpackages**

* `BasicBinds`
* `Navigation`
* `NavigationExtras`
* **Interface:** `RecyclerViewInterface`

---

### `Business`

**Responsibility:** Business-layer classes.

**Subpackages**

* **`services`**

    * `AppointmentService`
    * `AuthService`
    * `DocAvailabilityService`
    * `LookupService`
    * `TimeSlotService`

* **`validators`**

    * `AppointmentValidator`
    * `AvailabilityValidator`
    * `CredentialsValidator`
    * `UniversalAuthenticator`
    * `UserSignupValidator`
    * **Interface:** `UserAuthenticator`

* **`exceptions`**

    * `AuthExceptions`
    * `ValidationExceptions`

* **`creation`**

    * `ObjectCreation`

---

### `Persistence`

**Responsibility:** Persistence-layer classes and interfaces.

**Interfaces**

* `UserRepository`
* `UserPersistence`
* `AppointmentPersistence`
* `DoctorAvailabilityPersistence`

**Subpackages**

* **`fake`**

    * `FakeUserRepository`

* **`real`**

    * `SqlRepository`
    * `AppDbHelper`
    * `UserSqlHelper`
    * `AppointmentSqlHelper`
    * `AvailabilitySqlHelper`
    * `DbFactory`
    * `DbContract`

**Other classes**

* `UserFactory`

---

### `Models`

**Responsibility:** Shared model objects and enums.

**Classes / enums**

* `Users`
* `Admin`
* `Appointment`
* `AppointmentStatus`
* `Doctor`
* `DoctorAvailability`
* `Patient`
* `Specialization`
* `Staff`
* `TimeSlot`
* `UserRole`

