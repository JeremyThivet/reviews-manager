package org.jeremyworkspace.reviewsmanager.api;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.core.Is;
import org.jeremyworkspace.reviewsmanager.api.helpers.RandomFieldsGenerator;
import org.jeremyworkspace.reviewsmanager.api.helpers.UserTest;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ListReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Delete a list ok
    @Test
    public void whenDeleteRequestToListsAndValidUserAndValidList_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        // Adding the list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer idList = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lists/" + idList)
                        .header("Authorization", "Bearer " + u.getAccesToken())
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Try to get the list, must be empty.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lists/" + idList)
                        .header("Authorization", "Bearer " + u.getAccesToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void whenDeleteRequestToListsAndInvalidUserAndValidList_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        String nameTwo = RandomFieldsGenerator.getRandomName();
        UserTest uTwo = new UserTest();
        uTwo.setUsername(nameTwo);
        uTwo.setPassword("testtesttest123");
        uTwo = this.registerUserAndGetAccessTokenWithLogin(uTwo);

        // Adding the list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer idList = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        // User two tries to delete user 1 list.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lists/" + idList)
                        .header("Authorization", "Bearer " + uTwo.getAccesToken())
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }


    public UserTest registerUserAndGetAccessTokenWithLogin(UserTest u) throws Exception {
        String user = "{\"username\": \"" + u.getUsername() + "\", \"password\" : \"" + u.getPassword() + "\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        u.setId(id);
        String token = result.getResponse().getHeader("Authorization").split(" ")[1];
        u.setAccesToken(token);

        return u;
    }

}
