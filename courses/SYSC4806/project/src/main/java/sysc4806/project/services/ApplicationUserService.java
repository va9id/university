package sysc4806.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sysc4806.project.models.*;
import sysc4806.project.repositories.ApplicationUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.getUserRole;

@Service
public class ApplicationUserService {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    /**
     * Gets the current user
     * @return The current application user
     */
    public ApplicationUser getCurrentUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return applicationUserRepository.findApplicationUserByEmail(user.getUsername());
    }


    /**
     * Check if the current user is Professor
     * @return True if the current user is Professor
     */
    public boolean isCurrentUserProfessor() throws Exception {
        ApplicationUser currentUser = getCurrentUser();
        return Objects.equals(getUserRole(currentUser), PROFESSOR_ROLE);
    }

    /**
     * Gets a list of students not in a group
     * @return List<Students> Students not in a group
     */
    public List<Student> getStudentsNotInGroup() {
        List<Student> students = new ArrayList<>();
        for (ApplicationUser user: applicationUserRepository.findAll()) {
            if (user instanceof Student && ((Student) user).getProject() == null) {
                students.add((Student) user);
            }
        }
        return students;
    }
}
