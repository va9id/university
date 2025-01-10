package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockCustomProfessorUser;
import sysc4806.project.WithMockCustomStudentUser;
import sysc4806.project.WithMockProfessor;
import sysc4806.project.WithMockStudent;
import sysc4806.project.repositories.ApplicationUserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sysc4806.project.util.AuthenticationHelper.PROFESSOR_ROLE;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestHomePageController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private HomePageController controller;

    @MockBean
    private ApplicationUserRepository userRepository;

    @Test
    @WithMockStudent
    public void testStudentHomePage() throws Exception {
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        final String userType = STUDENT_ROLE.substring(5);
        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));
        when(userRepository.findApplicationUserByEmail(anyString())).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));
        this.mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));;
    }


    @Test
    @WithMockStudent
    public void testStudentHomePageWithReminder() throws Exception {
        final String reminder = "Please join a project";
        WithMockCustomStudentUser.STUDENT.setReminder(reminder);
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        final String userType = STUDENT_ROLE.substring(5);

        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));
        when(userRepository.findApplicationUserByEmail(anyString())).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(model().attribute("reminder", reminder))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));
        this.mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(model().attribute("reminder", reminder))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));;
    }

    @Test
    @WithMockProfessor
    public void testProfessorHomePage() throws Exception {
        final String name = WithMockCustomProfessorUser.PROFESSOR.getName();
        final String userType = PROFESSOR_ROLE.substring(5);
        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));
        when(userRepository.findApplicationUserByEmail(anyString())).thenReturn(WithMockCustomProfessorUser.PROFESSOR);
        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", PROFESSOR_ROLE))
                .andExpect(model().attribute("userName", name))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));
        this.mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("userType", PROFESSOR_ROLE))
                .andExpect(model().attribute("userName", name))
                .andDo(print()).andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(userType)));
    }
}
