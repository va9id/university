package sysc4806.project.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sysc4806.project.models.*;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

public class TestAuthenticationHelper {

    @Test
    public void testIsUserLoggedIn() {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);
            when(mockContext.getAuthentication()).thenReturn(null);
            assertFalse(AuthenticationHelper.isUserLoggedIn());

            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(false);
            assertFalse(AuthenticationHelper.isUserLoggedIn());

            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            assertTrue(AuthenticationHelper.isUserLoggedIn());

            mockAuthentication = mock(AnonymousAuthenticationToken.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            assertFalse(AuthenticationHelper.isUserLoggedIn());
        }

    }

    @Test
    public void testCreateUserDetails() throws Exception {
        String testName1 = "TEST_NAME_1";
        String testEmail1 = "TEST_EMAIL_1";
        String testPassword1 = "TEST_PASSWORD_1";
        String testName2 = "TEST_NAME_2";
        String testEmail2 = "TEST_EMAIL_2";
        String testPassword2 = "TEST_PASSWORD_2";
        ApplicationUser user1 = new ApplicationUser(testName1, testEmail1, testPassword1);
        ApplicationUser user2 = new Student(testName1, testEmail1, testPassword1, Program.SOFTWARE_ENGINEERING);
        ApplicationUser user3 = new Professor(testName2, testEmail2, testPassword2);

        assertThrows(Exception.class, () -> AuthenticationHelper.createUserDetails(user1));
        UserDetails user2Details = AuthenticationHelper.createUserDetails(user2);
        UserDetails user3Details = AuthenticationHelper.createUserDetails(user3);
        assertEquals(testEmail1, user2Details.getUsername());
        assertEquals(testPassword1, user2Details.getPassword());
        assertEquals(new HashSet<>(List.of(new SimpleGrantedAuthority(STUDENT_ROLE))), user2Details.getAuthorities());
        assertEquals(testEmail2, user3Details.getUsername());
        assertEquals(testPassword2, user3Details.getPassword());
        assertEquals(new HashSet<>(List.of(new SimpleGrantedAuthority(PROFESSOR_ROLE))), user3Details.getAuthorities());
    }

    @Test
    public void testGetUserRole() throws Exception {
        ApplicationUser user1 = new ApplicationUser("name1", "email1", "pass1");
        ApplicationUser user2 = new Student("name2", "email2", "pass2", Program.SOFTWARE_ENGINEERING);
        ApplicationUser user3 = new Professor("name3", "email3", "pass3");

        assertThrows(Exception.class, () -> AuthenticationHelper.createUserDetails(user1));
        assertEquals(STUDENT_ROLE, AuthenticationHelper.getUserRole(user2));
        assertEquals(PROFESSOR_ROLE, AuthenticationHelper.getUserRole(user3));
    }
}
