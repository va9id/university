package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import sysc4806.project.WithMockProfessor;
import sysc4806.project.models.Project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static sysc4806.project.util.AuthenticationHelper.STUDENT_ROLE;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestDeadlineController {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockProfessor
    public void testGetDeadlinePage() throws Exception {
        String deadline = Project.getDeadline().getTime().toString();
        this.mockMvc.perform(get("/deadlines")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("deadlines"))
                .andExpect(model().attribute("reportDeadline", deadline))
                .andDo(print()).andExpect(content().string(containsString(deadline)));
    }

    @Test
    @WithMockProfessor
    public void testSetReportDeadline() throws Exception {
        String deadline = "2023-12-12";
        this.mockMvc.perform(post("/deadlines/setReport").param("selectedDate", deadline))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("deadlines"))
                .andExpect(model().attribute("reportDeadline", Project.getDeadline().getTime().toString()));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        cal.setTime(sdf.parse(deadline));
        assertEquals(cal, Project.getDeadline());
    }
}
