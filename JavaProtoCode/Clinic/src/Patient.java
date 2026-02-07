import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private final List<String> medicalNotes = new ArrayList<>();

    public Patient(String username, String password) {
        super(username, password, Authority.PATIENT);
    }

    public void addMedicalNote(String note) {
        if (note == null || note.isBlank()) return;
        medicalNotes.add(note.trim());
    }

    public List<String> getMedicalNotes() {
        return List.copyOf(medicalNotes);
    }
}
