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
import sysc4806.project.models.Project;
import sysc4806.project.models.ReportFile;
import sysc4806.project.services.ApplicationUserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestDownloadController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private DownloadController controller;

    @MockBean
    private ApplicationUserService userService;

    @Test
    @WithMockStudent
    public void testStudentDownloadReport() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);

        // Test download when student not in a project
        this.mockMvc.perform(get("/downloadReport")).andExpect(status().isBadRequest());

        // Test download when student is in a project
        Project project = new Project();
        ReportFile report = new ReportFile("filename", "content".getBytes());
        project.setReport(report);
        WithMockCustomStudentUser.STUDENT.setProject(project);
        this.mockMvc.perform(get("/downloadReport")).andExpect(status().isOk())
                .andExpect(content().bytes(report.getFileData()));
    }

    @Test
    @WithMockProfessor
    public void testProfessorDownloadReport() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomProfessorUser.PROFESSOR);

        this.mockMvc.perform(get("/downloadReport/200")).andExpect(status().isBadRequest());

        Project project = new Project();
        ReportFile report = new ReportFile("filename", "content".getBytes());
        project.setReport(report);
        WithMockCustomProfessorUser.PROFESSOR.getProjects().add(project);
        this.mockMvc.perform(get("/downloadReport/" + project.getId())).andExpect(status().isOk())
                .andExpect(content().bytes(report.getFileData()));

    }
}
