package sysc4806.project;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import sysc4806.project.models.Program;
import sysc4806.project.models.Student;
import sysc4806.project.models.UserDetails;

import static sysc4806.project.util.AuthenticationHelper.createUserDetails;

public class WithMockCustomStudentUser
        implements WithSecurityContextFactory<WithMockStudent> {
    public static final Student STUDENT = new Student("name", "email", "password", Program.SOFTWARE_ENGINEERING);

    @Override
    public SecurityContext createSecurityContext(WithMockStudent annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails userDetails = null;
        try {
            userDetails = createUserDetails(STUDENT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, "password", userDetails.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}