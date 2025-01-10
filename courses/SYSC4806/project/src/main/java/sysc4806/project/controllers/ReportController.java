package sysc4806.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sysc4806.project.models.Professor;
import sysc4806.project.models.Project;
import sysc4806.project.models.ReportFile;
import sysc4806.project.models.Student;
import sysc4806.project.services.ApplicationUserService;
import sysc4806.project.services.ReportService;
import sysc4806.project.util.AuthenticationHelper;

import java.util.Date;

import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private ApplicationUserService userService;

    @GetMapping(path = "/viewReports")
    @Secured(PROFESSOR_ROLE)
    public String viewProjectReports(Model model) {
        Professor user = (Professor) userService.getCurrentUser();
        model.addAttribute("deadline", Project.getDeadline().getTime().toString());
        if (!user.getProjects().isEmpty()) {
            model.addAttribute("hasProjects", true);
            model.addAttribute("projects", user.getProjects());
        }
        else {
            model.addAttribute("hasProjects", false);
        }
        return "professorViewReports";
    }

    @GetMapping(path = "/studentReportUpload")
    @Secured(STUDENT_ROLE)
    public String uploadReportPage(Model model) {
        Student user = (Student) userService.getCurrentUser();
        model.addAttribute("deadline", Project.getDeadline().getTime().toString());
        model.addAttribute("beforeDeadline", isBeforeDeadline());
        if (user.getProject() != null) {
            ReportFile report = user.getProject().getReport();
            model.addAttribute("inProject", true);
            model.addAttribute("reportName", report == null ? "" : report.getFileName());
        }
        else {
            model.addAttribute("inProject", false);
        }

        return "studentReportUpload";
    }

    @PostMapping(path = "/studentReportUpload")
    @Secured(STUDENT_ROLE)
    public String uploadReport(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        Student user = (Student) userService.getCurrentUser();
        if (user.getProject() != null) {
            model.addAttribute("deadline", Project.getDeadline().getTime().toString());
            boolean beforeDeadline = isBeforeDeadline();
            if (beforeDeadline) {
                try {
                    reportService.saveReport(user.getProject(), file);
                }
                catch (Exception e) {
                    model.addAttribute("uploadError", "There was an error uploading the file!");
                }
                model.addAttribute("reportName", file.getOriginalFilename());
            }
            model.addAttribute("inProject", true);
            model.addAttribute("beforeDeadline", beforeDeadline);
            return "studentReportUpload";
        }

        model.addAttribute("userType", STUDENT_ROLE);
        model.addAttribute("userName", user.getName());
        return "redirect:/home"; // Redirect to home page if project is null
    }

    protected boolean isBeforeDeadline() {
        return new Date().before(Project.getDeadline().getTime());
    }

}
