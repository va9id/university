package sysc4806.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sysc4806.project.models.*;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.repositories.ProjectRepository;
import sysc4806.project.services.ApplicationUserService;
import java.util.List;

import static sysc4806.project.util.AuthenticationHelper.*;

@Controller
public class ProjectController {
    private final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ApplicationUserService applicationUserService;

    @GetMapping(path = "/createProject")
    @Secured(PROFESSOR_ROLE)
    public String showProjectForm(Model model) {
        Project project = new Project();
        project.setMaxStudents(1);
        model.addAttribute("project", project);
        return "createProject";
    }

    @PostMapping(path="/createProject")
    @Secured(PROFESSOR_ROLE)
    public String createProject(@ModelAttribute("project") Project project) {
        log.info("CREATE project request received");
        Professor professor = (Professor) applicationUserService.getCurrentUser();
        project.setProfessor(professor);
        projectRepository.save(project);
        log.info(String.format("Project with id: %s, successfully created", project.getId()));
        return "redirect:/viewProjects";
    }

    @GetMapping(path = "/viewProjects")
    public String viewProjectsPage(Model model) throws Exception {
        List<Project> projects = (List<Project>) projectRepository.findAll();
        model.addAttribute("projects", projects);

        boolean isProfessor = applicationUserService.isCurrentUserProfessor();
        model.addAttribute("isProfessor", isProfessor);

        if (isProfessor) {
            Professor professor = (Professor) applicationUserService.getCurrentUser();
            model.addAttribute("professorUser", professor);
        } else {
            Student student = (Student) applicationUserService.getCurrentUser();
            model.addAttribute("studentUser", student);
        }
        return "viewProjects";
    }

    @DeleteMapping(path = "/deleteProject/{projectId}")
    @Secured(PROFESSOR_ROLE)
    public String deleteProject(@PathVariable Long projectId) {
        log.info(String.format("DELETE project request received for id: %s", projectId));
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            disassociateProject(project);
            projectRepository.delete(project);
            log.info(String.format("project with id: %s, successfully DELETED", projectId));
            return "viewProjects";
        } else {
            log.error(String.format("FAILED to DELETE project with id: %s", projectId));
            return "error";
        }
    }

    @PatchMapping(path = "/project/{projectId}/addStudent/{userId}")
    @Secured(STUDENT_ROLE)
    public String addStudentToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        log.info("add student to project PATCH request received");
        Project project = projectRepository.findById(projectId).orElse(null);
        Student student = (Student) applicationUserRepository.findById(userId).orElse(null);
        if (project != null && student != null) {
            project.addStudent(student);
            student.setProject(project);
            if (student.getReminder() != null) { student.setReminder(null); }
            log.info(String.format("student with id: %s has been added to project with id: %s", userId, projectId));
            projectRepository.save(project);
            return "viewProjects";
        }
        log.error("FAILED to add student to project");
        return "error";
    }

    @PatchMapping(path = "/project/{projectId}/removeStudent/{userId}")
    @Secured(STUDENT_ROLE)
    public String removeStudentFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        log.info("remove student from project PATCH request received");
        Project project = projectRepository.findById(projectId).orElse(null);
        Student student = (Student) applicationUserRepository.findById(userId).orElse(null);
        if (project != null && student != null) {
            project.removeStudent(student);
            student.setProject(null);
            student.getOralPresentationAvailability().clear();
            projectRepository.save(project);
            log.info(String.format("student with id: %s has been removed from project with id: %s", userId, projectId));
            return "viewProjects";
        }
        log.error("FAILED to remove student from project");
        return "error";
    }

    /**
     * Disassociates a project from students and professor
     * @param project The project to disassociate
     */
    private void disassociateProject(Project project) {
        for (Student s: project.getStudents()) {
            s.setProject(null);
            s.getOralPresentationAvailability().clear();
            applicationUserRepository.save(s);
        }
        Professor professor = project.getProfessor();
        professor.removeProject(project);
        applicationUserRepository.save(professor);
    }
}
