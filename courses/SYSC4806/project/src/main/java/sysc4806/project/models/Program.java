package sysc4806.project.models;

/**
 * Program enumerator for all possible programs students can be apart of.
 */
public enum Program {
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    CIVIL_ENGINEERING("Civil Engineering"),
    SOFTWARE_ENGINEERING("Software Engineering"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    COMPUTER_ENGINEERING("Computer Engineering"),
    CHEMICAL_ENGINEERING("Chemical Engineering");

    private final String displayName;

    Program(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
