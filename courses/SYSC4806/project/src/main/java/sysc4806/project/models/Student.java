package sysc4806.project.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Student model
 */
@Entity
public class Student extends ApplicationUser {

    private String reminder;

    private Program program;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PresentationAvailability> oralPresentationAvailability;

    @ManyToOne
    private Project project;

    public Student() {
        this.oralPresentationAvailability = new ArrayList<>();
    }

    public Student(String name, String email, String password, Program program) {
        super(name, email, password);
        this.oralPresentationAvailability = new ArrayList<>();
        this.program = program;
        this.reminder = "";
    }

    /**
     * Get students project
     * @return Project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Set students project
     * @param project Project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Get student's program
     * @return Program
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Set student's program
     * @param program Program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Get student's oral presentation availability
     * @return Date
     */
    public List<PresentationAvailability> getOralPresentationAvailability() {
        return oralPresentationAvailability;
    }

    /**
     * Adds date/time to student's oral presentation availability
     * @param oralPresentationAvailability date/time availability
     */
    public void addOralPresentationAvailability(PresentationAvailability oralPresentationAvailability) {
        this.oralPresentationAvailability.add(oralPresentationAvailability);
    }

    /**
     * Get student's reminder
     * @return String
     */
    public String getReminder() {
        return reminder;
    }

    /**
     * Set student's reminder
     * @param reminder String
     */
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
