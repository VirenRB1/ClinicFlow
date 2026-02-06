import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AppointmentService {
    private final AccountService accounts;
    private final Random rng = new Random();

    public AppointmentService(AccountService accounts) {
        this.accounts = accounts;
    }

    public Appointment bookWithPreferredDoctor(
            String patientUsername,
            String doctorUsername,
            LocalDate date,
            int startHour,
            int endHour
    ) {
        validateRequest(date, startHour, endHour);

        User u = accounts.getByUsername(doctorUsername);
        if (u == null || u.getAuthority() != Authority.DOCTOR) {
            throw new IllegalArgumentException("Preferred doctor does not exist.");
        }

        Doctor d = (Doctor) u;
        if (!d.getSchedule().isFree(date, startHour, endHour)) {
            throw new IllegalArgumentException("That doctor is not free for the selected time.");
        }

        Appointment appt = new Appointment(patientUsername, doctorUsername, date, startHour, endHour);
        d.getSchedule().add(appt);
        return appt;
    }

    public Appointment bookWithRandomFreeDoctor(
            String patientUsername,
            LocalDate date,
            int startHour,
            int endHour
    ) {
        validateRequest(date, startHour, endHour);

        List<Doctor> freeDoctors = findFreeDoctors(date, startHour, endHour);
        if (freeDoctors.isEmpty()) {
            throw new IllegalArgumentException("No free doctors available for that time.");
        }

        Doctor chosen = freeDoctors.get(rng.nextInt(freeDoctors.size()));
        Appointment appt = new Appointment(patientUsername, chosen.getUsername(), date, startHour, endHour);
        chosen.getSchedule().add(appt);
        return appt;
    }

    public List<Doctor> findFreeDoctors(LocalDate date, int startHour, int endHour) {
        List<Doctor> result = new ArrayList<>();
        for (User u : accounts.getAllUsers()) {
            if (u.getAuthority() == Authority.DOCTOR) {
                Doctor d = (Doctor) u;
                if (d.getSchedule().isFree(date, startHour, endHour)) {
                    result.add(d);
                }
            }
        }
        return result;
    }

    private void validateRequest(LocalDate date, int startHour, int endHour) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Cannot make appointments on Sunday.");
        }
        // work hours: 9..20, appointments must be within and start < end
        if (startHour < 9 || endHour > 20 || startHour >= endHour) {
            throw new IllegalArgumentException("Appointment must be within 9:00 to 20:00 and start < end.");
        }
        // hour boundary only (you can keep it simple like this)
    }
}
