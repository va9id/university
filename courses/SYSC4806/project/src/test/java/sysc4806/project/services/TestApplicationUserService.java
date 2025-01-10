package sysc4806.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sysc4806.project.models.*;
import sysc4806.project.repositories.ApplicationUserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class TestApplicationUserService {
    @Spy
    private ApplicationUserRepository applicationUserRepository = mock(ApplicationUserRepository.class);
    @InjectMocks
    private ApplicationUserService applicationUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCurrentUser() {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        ApplicationUser user = new Student();
        UserDetails userDetails = new UserDetails("testUser", "testPassword", new ArrayList<SimpleGrantedAuthority>(), 123L);
        try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);
            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
            when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(user);
            assertEquals(user, applicationUserService.getCurrentUser());
        }
    }


    @Test
    public void testIsCurrentUserProfessor() throws Exception {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        ApplicationUser user1 = new Professor();
        ApplicationUser user2 = new Student();
        UserDetails userDetails = new UserDetails("testUser", "testPassword", new ArrayList<SimpleGrantedAuthority>(), 123L);
        try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);
            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
            when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(user1);
            assertTrue(applicationUserService.isCurrentUserProfessor());
            when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(user2);
            assertFalse(applicationUserService.isCurrentUserProfessor());
        }
    }


    @Test
    public void testGetStudentsNotInGroup() {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        Student user = new Student();
        Student user2 = new Student();
        List<ApplicationUser> students = new ArrayList<>();
        students.add(user);
        students.add(user2);

        UserDetails userDetails = new UserDetails("testUser", "testPassword", new ArrayList<SimpleGrantedAuthority>(), 123L);
        try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);
            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
            when(applicationUserRepository.findAll()).thenReturn(students);
            assertEquals(new HashSet<>(applicationUserService.getStudentsNotInGroup()), new HashSet<>(students));
        }
    }

    @Test
    public void testGetStudentsNotInGroupWithAllInGroup() {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        Student user = new Student();
        Student user2 = new Student();
        Project project = new Project();
        project.addStudent(user);
        project.addStudent(user2);

        UserDetails userDetails = new UserDetails("testUser", "testPassword", new ArrayList<SimpleGrantedAuthority>(), 123L);
        try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);
            when(mockContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
            when(applicationUserRepository.findAll()).thenReturn(new ArrayList<>());
            assertEquals(new HashSet<>(applicationUserService.getStudentsNotInGroup()), new HashSet<>());
        }
    }
}
