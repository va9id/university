package sysc4806.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class TestStudent {

    private Student student;

    @BeforeEach
    public void setUp() {
        student  = new Student();
    }

    @Test
    public void testGetAndSetProject() {
        Project project = new Project();
        student.setProject(project);
        assertEquals(student.getProject(), project);
    }

    @Test
    public void testGetAndSetProgram() {
        student.setProgram(Program.SOFTWARE_ENGINEERING);
        assertEquals(student.getProgram(), Program.SOFTWARE_ENGINEERING);
    }

    @Test
    public void testGetAndSetOralPresentationAvailability() {
        PresentationAvailability oralPresentationAvailability = new PresentationAvailability(LocalDateTime.of(2023, 12, 12, 10, 0));
        student.addOralPresentationAvailability(oralPresentationAvailability);
        assertEquals(student.getOralPresentationAvailability(), List.of(oralPresentationAvailability));
    }

    @Test
    public void testGetAndSetReminder() {
        String reminder = "Please join the group";
        student.setReminder(reminder);
        assertEquals(student.getReminder(), reminder);
    }

}
