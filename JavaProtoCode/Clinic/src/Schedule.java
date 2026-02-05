import java.time.LocalDate;
import java.util.*;

public class Schedule {
    private final Map<LocalDate, List<Appointment>> byDate = new HashMap<>();

    public List<Appointment> getAppointments(LocalDate date) {
        return List.copyOf(byDate.getOrDefault(date, List.of()));
    }

    public boolean isFree(LocalDate date, int startHour, int endHour) {
        List<Appointment> list = byDate.getOrDefault(date, List.of());
        for (Appointment a : list) {
            if (startHour < a.getEndHour() && a.getStartHour() < endHour) return false;
        }
        return true;
    }

    public void add(Appointment appt) {
        byDate.computeIfAbsent(appt.getDate(), d -> new ArrayList<>()).add(appt);
        byDate.get(appt.getDate()).sort(Comparator.comparingInt(Appointment::getStartHour));
    }
}
