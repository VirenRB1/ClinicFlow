import java.util.Objects;

public abstract class User {
    private final String username;
    private String password;
    private final Authority authority;

    protected User(String username, String password, Authority authority) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.authority = Objects.requireNonNull(authority);
    }

    public String getUsername() { return username; }
    public Authority getAuthority() { return authority; }

    public boolean checkPassword(String rawPassword) {
        return password.equals(rawPassword);
    }

    public void changePassword(String oldPass, String newPass) {
        if (!checkPassword(oldPass)) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        this.password = Objects.requireNonNull(newPass);
    }

    @Override
    public String toString() {
        return authority + "{username='" + username + "'}";
    }
}
