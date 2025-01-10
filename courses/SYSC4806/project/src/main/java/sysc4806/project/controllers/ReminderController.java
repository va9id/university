package sysc4806.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sysc4806.project.models.Student;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.services.ApplicationUserService;

import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;

@Controller
public class ReminderController {
    private final Logger log = LoggerFactory.getLogger(ReminderController.class);

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private final String REMINDER = "Please join a group";

    @GetMapping(path = "/listStudents")
    @Secured(PROFESSOR_ROLE)
    public String listStudentsNotInGroup(Model model) {
        model.addAttribute("students", applicationUserService.getStudentsNotInGroup());
        return "professorNotify";
    }

    @PatchMapping(path = "/notifyStudent/{studentId}")
    @Secured(PROFESSOR_ROLE)
    public String notifyStudent(@PathVariable Long studentId) {
        Student student = (Student) applicationUserRepository.findById(studentId).orElse(null);
        if (student != null) {
            student.setReminder(REMINDER);
            applicationUserRepository.save(student);
            return "professorNotify";
        }
        log.error("Failed to go back to professorNotify.html when notifying student");
        return "error";
    }
}
