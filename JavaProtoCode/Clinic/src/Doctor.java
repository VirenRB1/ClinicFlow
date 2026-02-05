public class Doctor extends User {
    private final Schedule schedule = new Schedule();

    public Doctor(String username, String password) {
        super(username, password, Authority.DOCTOR);
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
