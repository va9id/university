package sysc4806.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sysc4806.project.models.Project;
import sysc4806.project.models.ReportFile;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.repositories.ProjectRepository;
import sysc4806.project.repositories.ReportFileRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class TestReportService {
    @Spy
    private ProjectRepository projectRepository = mock(ProjectRepository.class);

    @Spy
    private ReportFileRepository reportFileRepository = mock(ReportFileRepository.class);

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveReport() throws Exception{
        Project project = new Project();
        String filename = "testfile.txt";
        byte[] data = "testData".getBytes();
        MultipartFile file = new MockMultipartFile("test", filename, "testContentType", data);
        ReportFile expectedReportFile = new ReportFile(filename, data);
        reportService.saveReport(project, file);
        assertEquals(expectedReportFile.getFileName(), project.getReport().getFileName());
        assertArrayEquals(expectedReportFile.getFileData(), project.getReport().getFileData());
    }

    @Test
    public void testSaveReportNullProject() {
        Project project = null;
        String filename = "testfile.txt";
        byte[] data = "testData".getBytes();
        MultipartFile file = new MockMultipartFile("test", filename, "testContentType", data);

        assertThrows(Exception.class, () -> {
            reportService.saveReport(project, file);
        });
    }

    @Test
    public void testGetReport() {
        Project project = new Project();
        String filename = "testfile.txt";
        byte[] data = "testData".getBytes();
        ReportFile expectedReportFile = new ReportFile(filename, data);
        project.setReport(expectedReportFile);

        assertEquals(expectedReportFile.getFileName(), reportService.getReport(project).getFileName());
        assertArrayEquals(expectedReportFile.getFileData(), reportService.getReport(project).getFileData());
    }

    @Test
    public void testGetReportNullProject() {
        Project project = null;
        assertNull(reportService.getReport(project));
    }
}
