package io.sumac.demo.member;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("debug")
public class MemberControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testCreateUpdateDeleteSuccess() throws Exception {

        // Arrange
        var data = mapper.createObjectNode();
        data.put("name", "tester1");
        data.put("login", "tester1");

        // Act
        RequestBuilder createRequest = MockMvcRequestBuilders.post("/api/v1/members").content(data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult createResult = mvc.perform(createRequest).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        JsonNode createResponse = mapper.readTree(createResult.getResponse().getContentAsString());
        var location = createResult.getResponse().getHeader("Location").replaceAll("http://localhost", "");

        RequestBuilder getRequest = MockMvcRequestBuilders.get(location);
        MvcResult getResult = mvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode getResponse = mapper.readTree(getResult.getResponse().getContentAsString());

        // change status
        ((ObjectNode)getResponse).put("status", "CANCELLED");

        RequestBuilder updateRequest = MockMvcRequestBuilders.put(location).content(getResponse.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult updateResult = mvc.perform(updateRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode updateResponse = mapper.readTree(updateResult.getResponse().getContentAsString());

        RequestBuilder deleteRequest = MockMvcRequestBuilders.delete(location);
        MvcResult deleteResult = mvc.perform(deleteRequest).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
        JsonNode deleteResponse = mapper.readTree(deleteResult.getResponse().getContentAsString());

        RequestBuilder deleteRequestAgain = MockMvcRequestBuilders.delete(location);
        MvcResult deleteResultAgain = mvc.perform(deleteRequestAgain).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        JsonNode deleteResponseAgain = mapper.readTree(deleteResultAgain.getResponse().getContentAsString());

        RequestBuilder getRequestAgain = MockMvcRequestBuilders.get(location);
        MvcResult getResultAgain = mvc.perform(getRequestAgain).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        JsonNode getResponseAgain = mapper.readTree(getResultAgain.getResponse().getContentAsString());

        RequestBuilder getAllRequest = MockMvcRequestBuilders.get("/api/v1/members");
        MvcResult getAllResult = mvc.perform(getAllRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode getAllResponse = mapper.readTree(getAllResult.getResponse().getContentAsString());

        getAllResponse.path("_embedded").path("members").forEach(System.out::println);
    }

}
