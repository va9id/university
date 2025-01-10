package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockCustomStudentUser;
import sysc4806.project.WithMockStudent;
import sysc4806.project.models.ApplicationUser;
import sysc4806.project.models.Professor;
import sysc4806.project.models.Program;
import sysc4806.project.models.Student;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.services.ApplicationUserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestLoginController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private LoginController controller;

    @MockBean
    private ApplicationUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ApplicationUserService userService;


    @Test
    @WithMockStudent
    public void testUserLoggedIn() throws Exception {
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(view().name("home"));
    }


    @Test
    @WithMockStudent
    public void testUserLoggedInWithReminder() throws Exception {
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        String reminder = "Please join a group";
        WithMockCustomStudentUser.STUDENT.setReminder(reminder);
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(model().attribute("reminder", reminder))
                .andExpect(view().name("home"));
    }


    @Test
    public void testUserNotLoggedIn() throws Exception {
        this.mockMvc.perform(get("/login")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"));
    }


    @Test
    public void testLogUserIn() throws Exception {
        String email = "EMAIL";
        String password = "PASSWORD";
        ApplicationUser user = new Professor("name", email, passwordEncoder.encode(password));
        when(userRepository.findApplicationUserByEmail(email)).thenReturn(user);

        // Test Valid Credentials
        this.mockMvc.perform(post("/loginHandler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .param("email", email).param("password", password))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/home"));

        // Test Invalid Credentials
        user.setPassword(passwordEncoder.encode("WRONG_PASSWORD"));
        this.mockMvc.perform(post("/loginHandler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", email).param("password", password))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/login?error"));
    }


    @Test
    public void testLogUserInWithReminder() throws Exception {
        String email = "EMAIL";
        String password = "PASSWORD";
        ApplicationUser user = new Student("name", email, passwordEncoder.
                encode(password), Program.SOFTWARE_ENGINEERING);
        when(userRepository.findApplicationUserByEmail(email)).thenReturn(user);

        // Log user in
        this.mockMvc.perform(post("/loginHandler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .param("email", email).param("password", password))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/home"));

    }
}
