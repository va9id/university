package sysc4806.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sysc4806.project.models.ApplicationUser;
import sysc4806.project.models.Student;
import sysc4806.project.services.ApplicationUserService;
import sysc4806.project.util.AuthenticationHelper;

@Controller
public class HomePageController {
    private final Logger log = LoggerFactory.getLogger(HomePageController.class);

    @Autowired
    private ApplicationUserService userService;

    @GetMapping(path = {"/home", "/"})
    public String getHomePage(Model model) throws Exception {
        ApplicationUser user = userService.getCurrentUser();
        if (user != null) {
            String role = AuthenticationHelper.getUserRole(user);
            model.addAttribute("userType", role);
            model.addAttribute("userName", user.getName());
            if (user instanceof Student) {
                model.addAttribute("reminder", ((Student) user).getReminder());
            }
            return "home";
        }
        log.error("Failed to go to homepage");
        return "error";
    }
}
