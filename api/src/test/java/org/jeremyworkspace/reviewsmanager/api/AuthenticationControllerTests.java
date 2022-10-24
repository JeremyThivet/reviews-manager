package org.jeremyworkspace.reviewsmanager.api;

import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.lang.Assert;
import org.hamcrest.core.Is;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.jeremyworkspace.reviewsmanager.api.helpers.RandomFieldsGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenLogin_IncorrectUser_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void whenLogin_CorrectUser_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        String user = "{\"username\": \"" + name + "\", \"password\" : \"testtesttest123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Logging in
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = resultActions.andReturn();
        String headerAuth = result.getResponse().getHeader("Authorization");

        Assert.isTrue(headerAuth.startsWith("Bearer "));


    }

}
