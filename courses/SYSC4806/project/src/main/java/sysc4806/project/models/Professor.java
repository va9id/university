package sysc4806.project.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Professor model
 */
@Entity
public class Professor extends ApplicationUser {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "professor")
    private List<Project> projects;

    public Professor() {
        this.projects = new ArrayList<>();
    }

    public Professor(String name, String email, String password) {
        super(name, email, password);
        this.projects = new ArrayList<>();
    }

    /**
     * Gets list of projects professor is supervising
     * @return List<Project> the list of projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Remove Project from the Professors projects list
     * @param project Project to be removed from the project
     */
    public void removeProject(Project project) {
        projects.remove(project);
    }
}
