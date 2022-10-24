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
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // User create ok
    @Test
    public void whenPostRequestToUserAndValidUser_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)));
    }

    // user get ok
    @Test
    public void whenGetRequestToUserAndValidId_thenCorrectResponse() throws Exception {

        // Add a user and try to retrieve it
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }

    // user update ok
    /*@Test
    public void whenPutRequestToUserAndValidUser_thenCorrectResponse() throws Exception {

        // Add a user and try to retrieve it
        String user = "{\"username\": \"Charly\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        user = "{\"username\": \"Charlytwo\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + id)
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Charlytwo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }*/

    // Username wrong size
    @Test
    public void whenPostRequestToUserAndInValidUsername_thenCorrectResponse() throws Exception {
        String user = "{\"username\": \"A\", \"password\" : \"testtesttest123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    // Password bad format
    @Test
    public void whenPostRequestToUserAndInValidPassword_thenCorrectResponse() throws Exception {
        String user = "{\"username\": \"Bobby\", \"password\" : \"testtesttest\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    // User already exists
    public void whenPostRequestToUserAndUserAlreadyExists_thenCorrect460Response() throws Exception {

        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is(460));
    }

    // user id does not exist
    @Test
    public void whenGetRequestToUserAndInvalidId_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/99999"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    // user update password wrong size
    /*@Test
    public void whenPutRequestToUserAndInvalidUser_thenCorrectResponse() throws Exception {

        // Add a user and try to retrieve it
        String user = "{\"username\": \"Epsilon\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        user = "{\"password\": \"test\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/"+ id)
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }*/


    // user delete ok
    @Test
    public void whenDeleteRequestToUserAndValidId_thenCorrectResponse() throws Exception {

        // Add a user and try to delete it
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Try to get the user, must be empty.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    // Add a list to a user ok
    @Test
    public void whenPostRequestToUserAndValidUserAndValidList_thenCorrectResponse() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + id + "/lists")
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.listName").value(nameList))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)));
    }

    // Add a list to non existing user
    @Test
    public void whenPostRequestToUserAndInvalidUserAndValidList_thenCorrectResponse() throws Exception {

        // Adding the list
        String list = "{\"listName\": \"Ma super liste\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/99999/lists")
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Add a list to existing user but list name wrong size
    @Test
    public void whenPostRequestToUserAndValidUserAndInvalidList_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        // Adding the list
        String list = "{\"listName\": \"\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + id + "/lists")
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Get all user lists

}
