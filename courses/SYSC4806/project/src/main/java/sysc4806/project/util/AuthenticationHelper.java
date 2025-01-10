package sysc4806.project.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import sysc4806.project.models.ApplicationUser;
import sysc4806.project.models.Professor;
import sysc4806.project.models.Student;
import sysc4806.project.models.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthenticationHelper {
    public static final String PROFESSOR_ROLE = "ROLE_PROFESSOR";
    public static final String STUDENT_ROLE = "ROLE_STUDENT";

    public static boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken) ;
    }

    public static UserDetails createUserDetails(ApplicationUser user) throws Exception {
        String role = getUserRole(user);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(List.of(new SimpleGrantedAuthority(role)));
        return new UserDetails(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities,
                user.getId()
        );
    }

    public static String getUserRole(ApplicationUser user) throws Exception {
        String role;
        if (user instanceof Student) {
            role = STUDENT_ROLE;
        }
        else if (user instanceof Professor) {
            role = PROFESSOR_ROLE;
        }
        else {
            throw new Exception("User should be of type Student or Professor, but it was not!");
        }
        return role;
    }
}
