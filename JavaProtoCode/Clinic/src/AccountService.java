import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, User> users = new HashMap<>();

    public AccountService() {
        // default admin
        users.put("admin", new Admin("admin", "admin123"));
    }

    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    public User getByUsername(String username) {
        return users.get(username);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    // Patient self-signup only
    public void createPatient(String username, String password) {
        validateNewUsername(username);
        validatePassword(password);
        users.put(username, new Patient(username, password));
    }

    // Admin-only creation for doctor/staff/admin
    public void createByAdmin(User currentUser, Authority type, String username, String password) {
        if (currentUser == null || currentUser.getAuthority() != Authority.ADMIN) {
            throw new SecurityException("Only admin can create staff/doctor/admin accounts.");
        }
        validateNewUsername(username);
        validatePassword(password);

        User created;
        switch (type) {
            case DOCTOR -> created = new Doctor(username, password);
            case STAFF -> created = new Staff(username, password);
            case ADMIN -> created = new Admin(username, password);
            default -> throw new IllegalArgumentException("Admin cannot create this type here.");
        }

        users.put(username, created);
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u == null) {
            throw new IllegalArgumentException("Error: username does not exist.");
        }
        if (!u.checkPassword(password)) {
            throw new IllegalArgumentException("Error: password does not match.");
        }
        return u;
    }

    private void validateNewUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
    }
}
