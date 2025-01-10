package sysc4806.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sysc4806.project.models.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {

}