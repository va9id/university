package sysc4806.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sysc4806.project.models.ReportFile;

public interface ReportFileRepository extends CrudRepository<ReportFile, Long> {
    public ReportFile findReportFileById(Long id);
}
