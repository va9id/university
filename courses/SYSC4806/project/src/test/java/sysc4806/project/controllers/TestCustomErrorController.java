package sysc4806.project.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestCustomErrorController {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetErrorPage() throws Exception {
        this.mockMvc.perform(get("/error")).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("error"));
    }
}
