package sysc4806.project.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sysc4806.project.models.Project;
import sysc4806.project.models.ReportFile;
import sysc4806.project.repositories.ProjectRepository;
import sysc4806.project.repositories.ReportFileRepository;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ReportService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportFileRepository reportFileRepository;

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    public void saveReport(Project project, MultipartFile file) throws Exception {
        try {
            try (InputStream inputStream = file.getInputStream()) {
                if (project != null) {
                    ReportFile reportFile = new ReportFile(file.getOriginalFilename(), inputStream.readAllBytes());
                    reportFileRepository.save(reportFile);
                    project.setReport(reportFile);
                    projectRepository.save(project);
                }
                else {
                    throw new Exception("Project cannot be null!");
                }
            }
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new IOException("Failed to store file.", e);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Project cannot be null.", e);
        }
    }

    public ReportFile getReport(Project project) {
        if (project != null) {
            return project.getReport();
        }
        return null;
    }
}
