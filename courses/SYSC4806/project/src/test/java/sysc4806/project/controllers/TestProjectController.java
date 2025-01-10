package sysc4806.project.controllers;

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
import sysc4806.project.models.ApplicationUser;
import sysc4806.project.models.Professor;
import sysc4806.project.models.Project;
import sysc4806.project.models.Student;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestProjectController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private ProjectController controller;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Test
    @WithMockProfessor
    public void testProfessorShowProjectForm() throws Exception {
        this.mockMvc.perform(get("/createProject")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("createProject"))
                .andExpect(model().attributeExists("project"));
    }

    @Test
    @WithMockProfessor
    public void testProfessorViewProjectPage() throws Exception {
        Project proj1 = new Project();
        proj1.setProfessor(new Professor());
        List<Project> projectList  = new ArrayList<>(Arrays.asList(proj1));
        when(projectRepository.findAll()).thenReturn(projectList);
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(WithMockCustomProfessorUser.PROFESSOR);
        this.mockMvc.perform(get("/viewProjects").with(csrf())).andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("projects", projectList))
                .andExpect(model().attribute("isProfessor", true))
                .andExpect(model().attribute("professorUser", WithMockCustomProfessorUser.PROFESSOR))
                .andExpect(view().name("viewProjects"));
    }

    @Test
    @WithMockStudent
    public void testStudentViewProjectPage() throws Exception {
        Project proj = new Project();
        proj.setProfessor(new Professor());
        List<Project> projectList  = new ArrayList<>(Arrays.asList(proj));
        when(projectRepository.findAll()).thenReturn(projectList);
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/viewProjects").with(csrf())).andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("projects", projectList))
                .andExpect(model().attribute("isProfessor", false))
                .andExpect(model().attribute("studentUser", WithMockCustomStudentUser.STUDENT))
                .andExpect(view().name("viewProjects"));
    }

    @Test
    @WithMockProfessor
    public void testDeleteProject() throws Exception {
        this.mockMvc.perform(delete("/deleteProject/"+100)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));

        Project project = new Project();
        Professor professor = new Professor();
        professor.getProjects().add(project);
        project.setProfessor(professor);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        this.mockMvc.perform(delete("/deleteProject/"+project.getId()).with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("viewProjects"));
        assertEquals(professor.getProjects(), new ArrayList<>());
    }

    @Test
    @WithMockStudent
    public void testAddStudentToProject() throws Exception {
        this.mockMvc.perform(patch("/project/{projectId}/addStudent/{studentId}", 100, 25)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));

        Project project = new Project();
        ApplicationUser user = new Student();
        doReturn(Optional.of(project)).when(projectRepository).findById(any());
        doReturn(Optional.of(user)).when(applicationUserRepository).findById(any());
        this.mockMvc.perform(patch("/project/{projectId}/addStudent/{studentId}",
                        project.getId(), user.getId())
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("viewProjects"));
    }


    @Test
    @WithMockStudent
    public void testAddStudentToProjectWithReminder() throws Exception {
        this.mockMvc.perform(patch("/project/{projectId}/addStudent/{studentId}", 100, 25)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));

        Project project = new Project();
        ApplicationUser user = new Student();
        ((Student) user).setReminder("Please join a project");
        doReturn(Optional.of(project)).when(projectRepository).findById(any());
        doReturn(Optional.of(user)).when(applicationUserRepository).findById(any());
        this.mockMvc.perform(patch("/project/{projectId}/addStudent/{studentId}",
                        project.getId(), user.getId())
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("viewProjects"));
        assertNull(((Student) user).getReminder());
    }
}
