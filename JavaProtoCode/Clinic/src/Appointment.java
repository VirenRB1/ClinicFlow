import java.time.LocalDate;
import java.util.Objects;

public class Appointment {
    private final String patientUsername;
    private final String doctorUsername;
    private final LocalDate date;
    private final int startHour; // 9..19
    private final int endHour;   // 10..20

    public Appointment(String patientUsername, String doctorUsername, LocalDate date, int startHour, int endHour) {
        this.patientUsername = Objects.requireNonNull(patientUsername);
        this.doctorUsername = Objects.requireNonNull(doctorUsername);
        this.date = Objects.requireNonNull(date);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getPatientUsername() { return patientUsername; }
    public String getDoctorUsername() { return doctorUsername; }
    public LocalDate getDate() { return date; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }

    // Half-open interval overlap: [start, end)
    public boolean overlaps(Appointment other) {
        if (!this.date.equals(other.date)) return false;
        if (!this.doctorUsername.equals(other.doctorUsername)) return false;
        return this.startHour < other.endHour && other.startHour < this.endHour;
    }

    @Override
    public String toString() {
        return date + " " + startHour + ":00-" + endHour + ":00 | Dr." + doctorUsername + " | Patient:" + patientUsername;
    }
}
