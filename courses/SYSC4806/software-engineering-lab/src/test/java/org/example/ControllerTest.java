package org.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateAddressBook() throws Exception {
        String requestBody = "{\"buddies\":[]}";

        mockMvc.perform(post("/addressBooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateBuddyInfo() throws Exception {
        String requestBody = "{\"name\":\"mbappe\", \"phone\":\"7\"}";
        mockMvc.perform(post("/buddyInfoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddBuddyInfoToAddressBook() throws Exception {
        String requestBody = "{\"buddies\":[]}";

        mockMvc.perform(post("/addressBooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        requestBody = "{\"name\":\"mbappe\", \"phone\":\"7\"}";
        mockMvc.perform(post("/buddyInfoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        String uriList = "http://localhost:8080/buddyInfoes/1";
        mockMvc.perform(patch("/addressBooks/1/buddies")
                        .contentType("text/uri-list")
                        .content(uriList))
                .andDo(print()).andExpect(status().is(204));
    }

    @Test
    public void testDeleteAddressBook() throws Exception {
        String requestBody = "{\"buddies\":[]}";

        mockMvc.perform(post("/addressBooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/addressBooks/1"))
                .andExpect(status().is(204));
    }

    @Test
    public void testDeleteBuddyInfo() throws Exception {
        String requestBody = "{\"name\":\"ronaldo\", \"phone\":\"7\"}";
        mockMvc.perform(post("/buddyInfoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/buddyInfoes/1"))
                .andExpect(status().is(204));
    }
}
