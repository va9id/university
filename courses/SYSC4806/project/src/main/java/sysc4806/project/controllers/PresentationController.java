package sysc4806.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sysc4806.project.models.*;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.repositories.ProjectRepository;
import sysc4806.project.services.ApplicationUserService;
import sysc4806.project.util.AuthenticationHelper;

import java.time.LocalDateTime;
import java.util.*;

import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@Controller
public class PresentationController {
    @Autowired
    private ApplicationUserService userService;

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final Logger log = LoggerFactory.getLogger(HomePageController.class);

    @GetMapping(path = "/presentations")
    public String handleGetPresentationPage(Model model) throws Exception {
        addToModel(model);
        return "presentations";
    }

    @PostMapping(path = "/presentations/oralPresentation/availability")
    @Secured(STUDENT_ROLE)
    public String addOralPresentationAvailability(@RequestParam("oralPresentationAvailability") List<LocalDateTime> availability, Model model) throws Exception {
        Student user = (Student) userService.getCurrentUser();
        for (LocalDateTime dateTime : availability)
            if (!user.getOralPresentationAvailability().contains(new PresentationAvailability(dateTime))) {
                PresentationAvailability presentationAvailability = new PresentationAvailability(dateTime);
                user.addOralPresentationAvailability(presentationAvailability);
            }

        userRepository.save(user);
        addToModel(model);
        return "redirect:/presentations";

    }

    @PostMapping(path = "/presentations/oralPresentation/setDate")
    @Secured(PROFESSOR_ROLE)
    public String setOralPresentationDate(@RequestParam("oralPresentationDate") LocalDateTime dateTime,
                                          @RequestParam("projectId") Long projectId, Model model) throws Exception {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.setOralPresentationDateTime(dateTime);
            projectRepository.save(project);
            addToModel(model);
            return "redirect:/presentations";
        }
        log.error(String.format("Project with ID %d does not exist!", projectId));
        return "redirect:/error";
    }

    private void addToModel(Model model) throws Exception {
        ApplicationUser user = userService.getCurrentUser();
        String role = AuthenticationHelper.getUserRole(user);
        model.addAttribute("role", role);
        model.addAttribute("oralPresentationTimes", PresentationAvailability.getOralPresentationDates());
        if (role.equals(PROFESSOR_ROLE)) {
            Professor professor = (Professor) user;
            model.addAttribute("projects", professor.getProjects());
            Map<Long, List<LocalDateTime>> possibleOralPresentationTimes = new HashMap<>();
            professor.getProjects().forEach((project) -> {
                List<Set<LocalDateTime>> studentTimes = new ArrayList<>();
                project.getStudents().forEach((student -> {
                    Set<LocalDateTime> times = new HashSet<>();
                    student.getOralPresentationAvailability().forEach(presentationAvailability -> times.add(presentationAvailability.getDateTime()));
                    studentTimes.add(times);
                }));
                if (!studentTimes.isEmpty()) {
                    Set<LocalDateTime> possibleTimes = studentTimes.get(0);
                    for (int i = 1; i < studentTimes.size(); i++) {
                        possibleTimes.retainAll(studentTimes.get(i));
                    }
                    possibleOralPresentationTimes.put(project.getId(), new ArrayList<>(possibleTimes));
                }
                else {
                    possibleOralPresentationTimes.put(project.getId(), new ArrayList<>());
                }
            });
            model.addAttribute("possibleOralPresentationTimes", possibleOralPresentationTimes);
        }

        else if (role.equals(STUDENT_ROLE)) {
            Student student = (Student) user;
            model.addAttribute("studentProject", student.getProject());
            List<LocalDateTime> currentAvailability = new ArrayList<>();
            student.getOralPresentationAvailability().forEach((availability) -> currentAvailability.add(availability.getDateTime()));
            model.addAttribute("currentAvailability", currentAvailability);
        }
    }
}
