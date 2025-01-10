package sysc4806.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestProject {

    private Project project;
    private Project emptyProject;
    private final String TITLE = "TITLE";
    private final String DESCRIPTION = "TITLE";
    private final int MAX_STUDENTS = 3;
    private final Professor prof = new Professor();

    @BeforeEach
    public void setUp() {
        project = new Project(TITLE, DESCRIPTION, MAX_STUDENTS, prof);
        emptyProject = new Project();

    }

    @Test
    void testGetTitle() {
        assertEquals(project.getTitle(), TITLE);
        assertNull(emptyProject.getTitle());
    }

    @Test
    void testSetTitle() {
        emptyProject.setTitle("test title");
        assertEquals(emptyProject.getTitle(), "test title");
    }

    @Test
    void testId() {
        project.setId(-1);
        assertEquals(project.getId(), -1);
    }

    @Test
    void testGetDescription() {
        assertEquals(project.getDescription(), DESCRIPTION);
        assertNull(emptyProject.getDescription());
    }

    @Test
    void testSetDescription() {
        emptyProject.setDescription("test description");
        assertEquals("test description", emptyProject.getDescription());
    }

    @Test
    void testGetProfessor() {
        assertEquals(project.getProfessor(), prof);
        assertNull(emptyProject.getProfessor());
    }

    @Test
    void testSetProfessor() {
        Professor newProf = new Professor();
        emptyProject.setProfessor(newProf);
        assertEquals(newProf, emptyProject.getProfessor());
    }

    @Test
    void testGetMaxStudents() {
        assertEquals(MAX_STUDENTS, project.getMaxStudents());
        assertEquals(0, emptyProject.getMaxStudents());
    }

    @Test
    void testSetMaxStudents() {
        emptyProject.setMaxStudents(10);
        assertEquals(10, emptyProject.getMaxStudents());
    }

    @Test
    void testRestrictions() {
        assertEquals(new ArrayList<>(), project.getRestrictions());
        List<Program> restrictions = new ArrayList<>(Arrays.asList(Program.ELECTRICAL_ENGINEERING, Program.CIVIL_ENGINEERING));
        project.setRestrictions(restrictions);
        assertEquals(restrictions, project.getRestrictions());
    }

    @Test
    void testGetStudents() {
        assertEquals(project.getStudents(), new ArrayList<>());
    }

    @Test
    void testAddStudent() {
        Student s = new Student();
        project.addStudent(s);
        assertEquals(new ArrayList<>(List.of(s)), project.getStudents());
    }

    @Test
    void testRemoveStudent() {
        Student s = new Student();
        project.addStudent(s);
        project.removeStudent(s);
        assertEquals(new ArrayList<>(), project.getStudents());
    }

    @Test
    void testSetReportFile() {
        ReportFile file = new ReportFile();
        project.setReport(file);
        assertEquals(file, project.getReport());
    }

    @Test
    void testSetDeadline() {
        Calendar calendar = new GregorianCalendar(2023, Calendar.DECEMBER, 12);
        project.setDeadline(calendar);
        assertEquals(calendar, project.getDeadline());
    }
}