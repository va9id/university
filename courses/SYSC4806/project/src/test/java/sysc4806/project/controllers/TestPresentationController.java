package sysc4806.project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockCustomProfessorUser;
import sysc4806.project.WithMockCustomStudentUser;
import sysc4806.project.WithMockProfessor;
import sysc4806.project.WithMockStudent;
import sysc4806.project.models.PresentationAvailability;
import sysc4806.project.models.Project;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.repositories.ProjectRepository;
import sysc4806.project.services.ApplicationUserService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestPresentationController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private PresentationController controller;

    @MockBean
    private ApplicationUserRepository userRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ApplicationUserService userService;

    @BeforeEach
    public void setup() {
        // Empty availability
        WithMockCustomStudentUser.STUDENT.getOralPresentationAvailability().clear();

        //Empty projects
        WithMockCustomProfessorUser.PROFESSOR.getProjects().clear();
    }

    @Test
    @WithMockStudent
    public void testHandleGetPresentationPage() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/presentations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("presentations"))
                .andExpect(model().attribute("role", STUDENT_ROLE))
                .andExpect(model().attribute("oralPresentationTimes", PresentationAvailability.getOralPresentationDates()))
                .andExpect(model().attribute("studentProject", WithMockCustomStudentUser.STUDENT.getProject()))
                .andExpect(model().attribute("currentAvailability", new ArrayList<>()));
    }

    @Test
    @WithMockStudent
    public void testAddOralPresentationAvailability() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 12, 12, 0);
        List<LocalDateTime> availability = List.of(dateTime);
        this.mockMvc.perform(post("/presentations/oralPresentation/availability")
                        .param("oralPresentationAvailability", availability.stream().map(Object::toString).toArray(String[]::new)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/presentations"));

        assertTrue(WithMockCustomStudentUser.STUDENT.getOralPresentationAvailability().contains(new PresentationAvailability(dateTime)));
    }

    @Test
    @WithMockProfessor
    public void testSetOralPresentationDate() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomProfessorUser.PROFESSOR);
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 12, 12, 0);
        long projectId = 1;
        Project project = new Project();
        project.setId(projectId);
        WithMockCustomProfessorUser.PROFESSOR.getProjects().add(project);
        Map<Long, List<LocalDateTime>> expectedMap = new HashMap<>();
        expectedMap.put(projectId, List.of(dateTime));

        // Test with invalid project ID
        this.mockMvc.perform(post("/presentations/oralPresentation/setDate")
                        .param("oralPresentationDate", String.valueOf(dateTime))
                        .param("projectId", String.valueOf(123)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"));

        // Test with valid project ID
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        this.mockMvc.perform(post("/presentations/oralPresentation/setDate")
                        .param("oralPresentationDate", String.valueOf(dateTime))
                        .param("projectId", String.valueOf(projectId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/presentations"));

        assertEquals(dateTime, project.getOralPresentationDateTime());
    }

}
