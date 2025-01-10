package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockProfessor;
import sysc4806.project.models.Student;
import sysc4806.project.repositories.ApplicationUserRepository;
import sysc4806.project.services.ApplicationUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestReminderController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserService userService;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Test
    @WithMockProfessor
    public void testListStudentsNotInGroup() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        when(userService.getStudentsNotInGroup()).thenReturn(students);
        this.mockMvc.perform(get("/listStudents").with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("professorNotify"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    @WithMockProfessor
    public void testNotifyStudent() throws Exception {
        this.mockMvc.perform(patch("/notifyStudent/{studentId}", 100L).with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));

        Student student = new Student();
        doReturn(Optional.of(student)).when(applicationUserRepository).findById(any());
        this.mockMvc.perform(patch("/notifyStudent/{studentId}",
                        student.getId()).with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("professorNotify"));
        assertEquals("Please join a group", student.getReminder());
    }
}
