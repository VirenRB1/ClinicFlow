import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClinicFlowApp {
    private final Scanner in = new Scanner(System.in);

    private final AccountService accounts = new AccountService();
    private final AppointmentService apptService = new AppointmentService(accounts);

    private User currentUser = null;

    public static void main(String[] args) {
        new ClinicFlowApp().run();
    }

    private void run() {
        while (true) {
            if (currentUser == null) showWelcomeMenu();
            else showUserMenu();
        }
    }

    private void showWelcomeMenu() {
        System.out.println("\nWelcome to Clinicflow , please press the number:");
        System.out.println("1: Create Account (Patient)");
        System.out.println("2: Login");
        System.out.println("0: Exit");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> handlePatientSignup();
            case 2 -> handleLogin();
            case 0 -> {
                System.out.println("Goodbye.");
                System.exit(0);
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private void showUserMenu() {
        System.out.println("\nLogged in as: " + currentUser.getUsername() + " (" + currentUser.getAuthority() + ")");
        switch (currentUser.getAuthority()) {
            case PATIENT -> patientMenu();
            case DOCTOR -> doctorMenu();
            case STAFF -> staffMenu();
            case ADMIN -> adminMenu();
        }
    }

    // -------- Welcome handlers --------

    private void handlePatientSignup() {
        String username = readLine("New patient username: ");
        String password = readLine("New patient password: ");

        try {
            accounts.createPatient(username, password);
            System.out.println("Patient account created. You can now login.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleLogin() {
        String username = readLine("Username: ");
        String password = readLine("Password: ");

        try {
            currentUser = accounts.login(username, password);
            System.out.println("Login successful.");
        } catch (Exception e) {
            // prints: username not exist / password not match
            System.out.println(e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    // -------- Menus per role --------

    private void patientMenu() {
        Patient p = (Patient) currentUser;

        System.out.println("1: View Medical Notes");
        System.out.println("2: Add Medical Note (demo)");
        System.out.println("3: Make Appointment");
        System.out.println("9: Logout");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> {
                if (p.getMedicalNotes().isEmpty()) System.out.println("(No medical notes)");
                else {
                    System.out.println("Medical Notes:");
                    for (int i = 0; i < p.getMedicalNotes().size(); i++) {
                        System.out.println((i + 1) + ". " + p.getMedicalNotes().get(i));
                    }
                }
            }
            case 2 -> {
                String note = readLine("Enter note: ");
                p.addMedicalNote(note);
                System.out.println("Added.");
            }
            case 3 -> handleMakeAppointment(p);
            case 9 -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void doctorMenu() {
        Doctor d = (Doctor) currentUser;

        System.out.println("1: View Schedule (by date)");
        System.out.println("2: View Today Schedule");
        System.out.println("9: Logout");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> {
                LocalDate date = readDate("Date");
                List<Appointment> appts = d.getSchedule().getAppointments(date);
                if (appts.isEmpty()) System.out.println("(No appointments)");
                else appts.forEach(System.out::println);
            }
            case 2 -> {
                LocalDate today = LocalDate.now();
                List<Appointment> appts = d.getSchedule().getAppointments(today);
                System.out.println("Today: " + today);
                if (appts.isEmpty()) System.out.println("(No appointments)");
                else appts.forEach(System.out::println);
            }
            case 9 -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void staffMenu() {
        System.out.println("1: Staff placeholder action");
        System.out.println("9: Logout");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> System.out.println("Staff action (not implemented yet).");
            case 9 -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void adminMenu() {
        System.out.println("1: Create Doctor Account");
        System.out.println("2: Create Staff Account");
        System.out.println("3: Create Admin Account");
        System.out.println("9: Logout");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> handleAdminCreate(Authority.DOCTOR);
            case 2 -> handleAdminCreate(Authority.STAFF);
            case 3 -> handleAdminCreate(Authority.ADMIN);
            case 9 -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private void handleAdminCreate(Authority type) {
        String username = readLine("New " + type + " username: ");
        String password = readLine("New " + type + " password: ");

        try {
            accounts.createByAdmin(currentUser, type, username, password);
            System.out.println(type + " account created.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // -------- Appointment booking --------

    private void handleMakeAppointment(Patient p) {
        LocalDate date = readDate("Appointment date");
        int start = readInt("Start hour (9-19): ");
        int end = readInt("End hour (10-20): ");

        System.out.println("Choose doctor option:");
        System.out.println("1: Preferred doctor");
        System.out.println("2: Random free doctor");
        System.out.println("3: Show free doctors list (then choose)");
        int opt = readInt("Choice: ");

        try {
            Appointment booked;

            if (opt == 1) {
                String doc = readLine("Doctor username: ");
                booked = apptService.bookWithPreferredDoctor(p.getUsername(), doc, date, start, end);

            } else if (opt == 2) {
                booked = apptService.bookWithRandomFreeDoctor(p.getUsername(), date, start, end);

            } else if (opt == 3) {
                List<Doctor> free = apptService.findFreeDoctors(date, start, end);
                if (free.isEmpty()) {
                    System.out.println("No free doctors available for that time.");
                    return;
                }
                System.out.println("Free doctors:");
                for (int i = 0; i < free.size(); i++) {
                    System.out.println((i + 1) + ": " + free.get(i).getUsername());
                }
                int pick = readInt("Pick number: ");
                if (pick < 1 || pick > free.size()) {
                    System.out.println("Invalid pick.");
                    return;
                }
                String chosenDoc = free.get(pick - 1).getUsername();
                booked = apptService.bookWithPreferredDoctor(p.getUsername(), chosenDoc, date, start, end);

            } else {
                System.out.println("Invalid option.");
                return;
            }

            System.out.println("Booked: " + booked);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // -------- Input helpers --------

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String s = readLine(prompt + " (YYYY-MM-DD): ");
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date format.");
            }
        }
    }
}
