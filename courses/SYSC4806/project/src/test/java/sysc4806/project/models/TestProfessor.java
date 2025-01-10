package sysc4806.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestProfessor {
    private Professor professor;

    @BeforeEach
    public void setUp() {
        professor = new Professor();
    }

    @Test
    public void testGetProjects() {
        assertEquals(new ArrayList<>(), professor.getProjects());
    }

    @Test
    public void testRemoveProject() {
        Project p = new Project();
        professor.getProjects().add(p);
        professor.removeProject(p);
        assertEquals(new ArrayList<>(), professor.getProjects());
    }

}
