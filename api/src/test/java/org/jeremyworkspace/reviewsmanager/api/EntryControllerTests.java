package org.jeremyworkspace.reviewsmanager.api;

import com.jayway.jsonpath.JsonPath;
import org.jeremyworkspace.reviewsmanager.api.helpers.RandomFieldsGenerator;
import org.jeremyworkspace.reviewsmanager.api.helpers.UserTest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EntryControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenAddingScoreFieldValueToValidEntry_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        // Adding a list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer listId = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        // Adding some fields to the list
        String scoreFieldName = RandomFieldsGenerator.getRandomName();
        int scoreMax = 20, displayOption = 0;
        String scoreFieldJson = "{ \"fieldName\": \"" + scoreFieldName + "\", " +
                                  "\"scoreMax\": \"" + scoreMax + "\", " +
                                  "\"displayOption\": \"" + displayOption + "\"}";

        MvcResult resultThree =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/fields?type=score")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(scoreFieldJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer fieldId = JsonPath.read(resultThree.getResponse().getContentAsString(), "$.id");

        // Adding an entry
        String entryName = RandomFieldsGenerator.getRandomName();
        String fieldValue = "15";
        String entryJson = "{ \"entryName\": \"" + entryName + "\", " +
                "\"fieldsWithValues\": {"+
                    "\"" + fieldId + "\": \"" + fieldValue + "\" }" +
                "}";

        MvcResult resultFour =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/entries")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(entryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();


        Integer entryId = JsonPath.read(resultFour.getResponse().getContentAsString(), "$.id");


        // try to get the entry
        mockMvc.perform(MockMvcRequestBuilders.get("/api/entries/" + entryId)
                        .header("Authorization", "Bearer " + u.getAccesToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entryName").value(entryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creator.username").value(u.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].field.id").value(fieldId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].value").value(fieldValue))
                ;
    }

    @Test
    public void whenUpdatingScoreFieldValueToValidEntry_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        // Adding a list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer listId = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        // Adding some fields to the list
        String scoreFieldName = RandomFieldsGenerator.getRandomName();
        int scoreMax = 20, displayOption = 0;
        String scoreFieldJson = "{ \"fieldName\": \"" + scoreFieldName + "\", " +
                "\"scoreMax\": \"" + scoreMax + "\", " +
                "\"displayOption\": \"" + displayOption + "\"}";

        MvcResult resultThree =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/fields?type=score")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(scoreFieldJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer fieldId = JsonPath.read(resultThree.getResponse().getContentAsString(), "$.id");

        // Adding an entry
        String entryName = RandomFieldsGenerator.getRandomName();
        String fieldValue = "15";
        String entryJson = "{ \"entryName\": \"" + entryName + "\", " +
                "\"fieldsWithValues\": {"+
                "\"" + fieldId + "\": \"" + fieldValue + "\" }" +
                "}";

        MvcResult resultFour =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/entries")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(entryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();


        Integer entryId = JsonPath.read(resultFour.getResponse().getContentAsString(), "$.id");


        // try to get the entry
        mockMvc.perform(MockMvcRequestBuilders.get("/api/entries/" + entryId)
                        .header("Authorization", "Bearer " + u.getAccesToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entryName").value(entryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creator.username").value(u.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].field.id").value(fieldId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].value").value(fieldValue))
        ;


        // Trying to update the list name and the value
        String entryReName = RandomFieldsGenerator.getRandomName();
        String fieldNewValue = "10";
        String entryNewJson = "{ \"entryName\": \"" + entryReName + "\", " +
                "\"fieldsWithValues\": {"+
                "\"" + fieldId + "\": \"" + fieldNewValue + "\" }" +
                "}";

        MvcResult resultFive =mockMvc.perform(MockMvcRequestBuilders.put("/api/entries/" + entryId )
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(entryNewJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();


        // try to get the entry and check values
        mockMvc.perform(MockMvcRequestBuilders.get("/api/entries/" + entryId)
                        .header("Authorization", "Bearer " + u.getAccesToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entryName").value(entryReName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creator.username").value(u.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].field.id").value(fieldId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldValueList[0].value").value(fieldNewValue))
        ;

    }

    @Test
    public void whenAddingIncorrectDateFieldValueToValidEntry_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        // Adding a list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer listId = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        // Adding some fields to the list
        String dateFieldName = RandomFieldsGenerator.getRandomName();
        String dateFieldJson = "{ \"fieldName\": \"" + dateFieldName + "\" }";

        MvcResult resultThree =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/fields?type=date")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(dateFieldJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer fieldId = JsonPath.read(resultThree.getResponse().getContentAsString(), "$.id");

        // Adding an entry
        String entryName = RandomFieldsGenerator.getRandomName();
        String fieldValue = "wrongdate";
        String entryJson = "{ \"entryName\": \"" + entryName + "\", " +
                "\"fieldsWithValues\": {"+
                "\"" + fieldId + "\": \"" + fieldValue + "\" }" +
                "}";

        MvcResult resultFour =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/entries")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(entryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Mauvais format de date.")).andReturn();

    }

    @Test
    public void whenAddingIncorrectScoreFieldValueToValidEntry_thenCorrectResponse() throws Exception {
        String name = RandomFieldsGenerator.getRandomName();
        UserTest u = new UserTest();
        u.setUsername(name);
        u.setPassword("testtesttest123");
        u = this.registerUserAndGetAccessTokenWithLogin(u);

        // Adding a list
        String nameList = RandomFieldsGenerator.getRandomName();
        String list = "{\"listName\": \"" + nameList + "\"}";
        MvcResult resulttwo = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/" + u.getId() + "/lists")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(list)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer listId = JsonPath.read(resulttwo.getResponse().getContentAsString(), "$.id");

        // Adding some fields to the list
        String scoreFieldName = RandomFieldsGenerator.getRandomName();
        int scoreMax = 20, displayOption = 0;
        String scoreFieldJson = "{ \"fieldName\": \"" + scoreFieldName + "\", " +
                "\"scoreMax\": \"" + scoreMax + "\", " +
                "\"displayOption\": \"" + displayOption + "\"}";

        MvcResult resultThree =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/fields?type=score")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(scoreFieldJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        Integer fieldId = JsonPath.read(resultThree.getResponse().getContentAsString(), "$.id");

        // Adding an entry
        String entryName = RandomFieldsGenerator.getRandomName();
        String fieldValue = "25";
        String entryJson = "{ \"entryName\": \"" + entryName + "\", " +
                "\"fieldsWithValues\": {"+
                "\"" + fieldId + "\": \"" + fieldValue + "\" }" +
                "}";

        MvcResult resultFour =mockMvc.perform(MockMvcRequestBuilders.post("/api/lists/" + listId + "/entries")
                        .header("Authorization", "Bearer " + u.getAccesToken())
                        .content(entryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Le score est plus élevé que le score maximum spécifié dans le champ.")).andReturn();
        ;
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
