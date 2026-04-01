# ClinicFlow - Physician's Office System

A role-based Android application for managing appointments, physician availability, and patient records in a physician's office.

## Group Members
1. Viren Ravji Bhanderi - 7976692
2. Najma Mohamed - 7934223
3. Gurwinder Khandal - 7909492
4. Hoang Phuc - 7900499
5. Israel Ijiekhuamen - 7909801

## Project Overview

ClinicFlow is a clinic management system that supports four user roles: patients, physicians, office staff, and administrators. Patients can self-register, book and cancel appointments, and view their appointment history with doctor notes. Physicians can set their weekly availability, manage appointments, and add clinical notes after completing visits. Office staff can book and cancel appointments on behalf of patients and look up patient and doctor records. Administrators manage user accounts for physicians and staff.

## Requirements

- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: 17 (bundled with Android Studio)
- **Android SDK**: API 34 (compileSdk and targetSdk)
- **Minimum SDK**: API 34
- **Gradle**: 8.10.2 (included via wrapper)

## Build and Run

1. Clone the repository:
   ```
   git clone https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g14-booleanhooligans.git
   ```
2. Open the project in Android Studio.
3. Let Gradle sync complete (Android Studio will download dependencies automatically).
4. Select a device or emulator running API 34+.
5. Click **Run** (Shift+F10) to build and launch the app.

## Running Tests

### Unit Tests
Unit tests are located in `app/src/test/`. Run them from Android Studio or via command line:
```
./gradlew test
```

### Integration Tests
Integration tests are located in `app/src/androidTest/java/.../integration/`. These require a connected device or emulator:
Run it with android studio or via command line:
```
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.clinicflow.integration.AppointmentRepoIT
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.clinicflow.integration.ObjectServiceSQLRepoIT
```

### End-to-End Tests
E2E tests are located in `app/src/androidTest/java/.../e2e/`. These also require a connected device or emulator:
Run it with android studio or via command line:
```
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.clinicflow.e2e.PatientFlowTest
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.clinicflow.e2e.StaffFlowTest
```

### All Instrumented Tests
To run all integration and e2e tests together:
Run it with android studio or via command line:
```
./gradlew connectedAndroidTest
```

## Documentation

- [Vision Statement](Docs/VISION.md)
- [Architecture](Docs/Architecture.md)
- [Retrospective](Docs/retrospectiveIteration0.md)
