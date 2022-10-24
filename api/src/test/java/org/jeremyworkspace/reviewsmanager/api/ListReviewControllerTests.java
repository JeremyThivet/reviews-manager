package org.jeremyworkspace.reviewsmanager.api;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.core.Is;
import org.jeremyworkspace.reviewsmanager.api.helpers.RandomFieldsGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ListReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Delete a list ok
    @Test
    public void whenDeleteRequestToListsAndValidUserAndValidList_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        // Adding the list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + id + "/lists")
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer idList = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lists/" + idList))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Try to get the list, must be empty.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lists/" + idList))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
