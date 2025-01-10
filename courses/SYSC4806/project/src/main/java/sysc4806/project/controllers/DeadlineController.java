package sysc4806.project.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sysc4806.project.models.Project;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;

@Controller
public class DeadlineController {

    @GetMapping(path = "/deadlines")
    @Secured(PROFESSOR_ROLE)
    public String getDeadlinePage(Model model) {
        model.addAttribute("reportDeadline", Project.getDeadline().getTime().toString());
        return "deadlines";
    }

    @PostMapping(path = "/deadlines/setReport")
    @Secured(PROFESSOR_ROLE)
    public String setReportDeadline(@RequestParam("selectedDate") String deadline, Model model) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        cal.setTime(sdf.parse(deadline));
        Project.setDeadline(cal);
        model.addAttribute("reportDeadline", Project.getDeadline().getTime().toString());
        return "deadlines";
    }
}
