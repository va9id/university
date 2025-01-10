package sysc4806.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sysc4806.project.models.ApplicationUser;
import sysc4806.project.models.Professor;
import sysc4806.project.models.Student;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.services.ApplicationUserService;
import sysc4806.project.util.AuthenticationHelper;

import static sysc4806.project.util.AuthenticationHelper.isUserLoggedIn;

@Controller
public class RegisterController {
    private final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserService userService;

    @GetMapping(path ="/register/Professor")
    public String registerProf(Model model) throws Exception {
        if (isUserLoggedIn()) {
            ApplicationUser user = userService.getCurrentUser();
            String role = AuthenticationHelper.getUserRole(user);
            model.addAttribute("userType", role);
            model.addAttribute("userName", user.getName());
            if (user instanceof Student) {
                model.addAttribute("reminder", ((Student) user).getReminder());
            }
            return "home";
        }

        Professor professor = new Professor();
        model.addAttribute("professor", professor);
        return "professorRegister";
    }

    @GetMapping(path ="/register/Student")
    public String registerStudent(Model model) throws Exception {
        if (isUserLoggedIn()) {
            ApplicationUser user = userService.getCurrentUser();
            String role = AuthenticationHelper.getUserRole(user);
            model.addAttribute("userType", role);
            model.addAttribute("userName", user.getName());
            if (user instanceof Student) {
                model.addAttribute("reminder", ((Student) user).getReminder());
            }
            return "home";
        }

        Student student = new Student();
        model.addAttribute("student", student);
        return "studentRegister";
    }

    @PostMapping(path = "/register/Professor")
    public String createProfessor(@ModelAttribute Professor professor) {
        if (userRepository.findApplicationUserByEmail(professor.getEmail()) != null) {
            return "redirect:/register/Professor?error";
        }
        professor.setPassword(passwordEncoder.encode(professor.getPassword()));
        userRepository.save(professor);
        log.info("Professor has successfully registered");
        return "redirect:/login";
    }

    @PostMapping(path = "/register/Student")
    public String createStudent(@ModelAttribute Student student) {
        if (userRepository.findApplicationUserByEmail(student.getEmail()) != null) {
            return "redirect:/register/Student?error";
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userRepository.save(student);
        log.info("Student has successfully registered");
        return "redirect:/login";
    }
}
