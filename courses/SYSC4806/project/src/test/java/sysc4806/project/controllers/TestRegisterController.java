package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockCustomStudentUser;
import sysc4806.project.WithMockStudent;
import sysc4806.project.services.ApplicationUserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestRegisterController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserService userService;

    @Test
    @WithMockStudent
    public void testRegisterLoggedIn() throws Exception {
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/register/Student"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(view().name("home"));
        this.mockMvc.perform(get("/register/Professor"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(view().name("home"));
    }


    @Test
    @WithMockStudent
    public void testRegisterLoggedInWithReminder() throws Exception {
        final String name = WithMockCustomStudentUser.STUDENT.getName();
        String reminder = "Please join a group";
        WithMockCustomStudentUser.STUDENT.setReminder(reminder);
        when(userService.getCurrentUser()).thenReturn(WithMockCustomStudentUser.STUDENT);
        this.mockMvc.perform(get("/register/Student"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(model().attribute("reminder", reminder))
                .andExpect(view().name("home"));
        this.mockMvc.perform(get("/register/Professor"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("userType", STUDENT_ROLE))
                .andExpect(model().attribute("userName", name))
                .andExpect(model().attribute("reminder", reminder))
                .andExpect(view().name("home"));
    }


    @Test
    public void testRegisterLoggedOut() throws Exception {
        this.mockMvc.perform(get("/register/Professor")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("professorRegister"))
                .andExpect(model().attributeExists("professor"));
        this.mockMvc.perform(get("/register/Student")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("studentRegister"))
                .andExpect(model().attributeExists("student"));
    }
}
