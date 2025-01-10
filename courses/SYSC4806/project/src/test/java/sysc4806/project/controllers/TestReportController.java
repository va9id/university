package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockCustomProfessorUser;
import sysc4806.project.WithMockCustomStudentUser;
import sysc4806.project.WithMockProfessor;
import sysc4806.project.WithMockStudent;
import sysc4806.project.models.Project;
import sysc4806.project.models.ReportFile;
import sysc4806.project.services.ApplicationUserService;
import sysc4806.project.services.ReportService;

import java.util.Calendar;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestReportController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private ReportController controller;

    @MockBean
    private ApplicationUserService userService;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockStudent
    public void testUploadReportPage() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        WithMockCustomStudentUser.STUDENT.setProject(new Project());
        String reportFileName = "Test";
        WithMockCustomStudentUser.STUDENT.getProject().setReport(new ReportFile(reportFileName, "test".getBytes()));
        this.mockMvc.perform(get("/studentReportUpload"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("studentReportUpload"))
                .andExpect(model().attribute("deadline", Project.getDeadline().getTime().toString()))
                .andExpect(model().attribute("beforeDeadline", true))
                .andExpect(model().attribute("inProject", true))
                .andExpect(model().attribute("reportName",reportFileName));
    }

    @Test
    @WithMockStudent
    public void testUploadReportPageNullProject() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        WithMockCustomStudentUser.STUDENT.setProject(null);
        this.mockMvc.perform(get("/studentReportUpload"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("studentReportUpload"))
                .andExpect(model().attribute("inProject", false));
    }

    @Test
    @WithMockStudent
    public void testUploadReport() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        String filename = "FILENAME";
        byte[] data = "testData".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", filename, "testContentType", data);

        // Test with non-null project
        Project project = new Project();
        ReportFile report = new ReportFile(filename, data);
        project.setReport(report);
        WithMockCustomStudentUser.STUDENT.setProject(project);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Project.setDeadline(calendar);
        this.mockMvc.perform(multipart("/studentReportUpload").file(file))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("studentReportUpload"))
                .andExpect(model().attribute("reportName", filename))
                .andExpect(model().attribute("inProject", true))
                .andExpect(model().attribute("beforeDeadline", true));

        // Test with null project
        WithMockCustomStudentUser.STUDENT.setProject(null);
        this.mockMvc.perform(multipart("/studentReportUpload").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockProfessor
    public void testViewProjectReports() throws Exception {
        when(userService.getCurrentUser()).thenReturn(WithMockCustomProfessorUser.PROFESSOR);
        Project proj = new Project();
        String reportFileName = "Test";
        proj.setReport(new ReportFile(reportFileName, "test".getBytes()));
        WithMockCustomProfessorUser.PROFESSOR.getProjects().add(proj);
        this.mockMvc.perform(get("/viewReports"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("professorViewReports"))
                .andExpect(model().attribute("deadline", Project.getDeadline().getTime().toString()))
                .andExpect(model().attribute("hasProjects", true))
                .andExpect(model().attribute("projects", WithMockCustomProfessorUser.PROFESSOR.getProjects()));

        // Test when prof has no projects
        WithMockCustomProfessorUser.PROFESSOR.getProjects().clear();
        this.mockMvc.perform(get("/viewReports"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("professorViewReports"))
                .andExpect(model().attribute("deadline", Project.getDeadline().getTime().toString()))
                .andExpect(model().attribute("hasProjects", false));
    }


}
