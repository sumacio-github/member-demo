package io.sumac.demo.member;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MemberDataService members;

    private static final String NOW_STRING = "2020-11-30T21:47:51.482741";
    private static final LocalDateTime NOW = LocalDateTime.parse(NOW_STRING);

    @Test
    public void testFindByAll() throws Exception {

        // Arrange
        var data = new ArrayList<Member>();
        data.add(new Member(1, "tester1", null, "tester1", 1.1f, "ACTIVE", NOW));
        data.add(new Member(2, "tester2", "test2", "tester2", 1.2f, "ACTIVE", NOW));
        data.add(new Member(3, "tester3", null, "tester3", 1.3f, "ACTIVE", NOW));
        Mockito.when(members.findByAll()).thenReturn(data);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/members");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        var membersArray = response.get("_embedded").get("members");
        Assertions.assertEquals(3, membersArray.size());
        var member1 = membersArray.get(0);
        var member2 = membersArray.get(1);
        var member3 = membersArray.get(2);

        Assertions.assertEquals("tester1", member1.path("name").asText());
        Assertions.assertEquals("tester1", member1.path("login").asText());
        Assertions.assertEquals("ACTIVE", member1.path("status").asText());
        Assertions.assertEquals(NOW_STRING, member1.path("joined").asText());
        Assertions.assertEquals(1.1, member1.path("rating").asDouble());
        Assertions.assertEquals(1, member1.path("id").asInt());
        Assertions.assertEquals(true, member1.path("aliasName").isNull());

        Assertions.assertEquals("tester2", member2.path("name").asText());
        Assertions.assertEquals("tester2", member2.path("login").asText());
        Assertions.assertEquals("ACTIVE", member2.path("status").asText());
        Assertions.assertEquals(NOW_STRING, member2.path("joined").asText());
        Assertions.assertEquals(1.2, member2.path("rating").asDouble());
        Assertions.assertEquals(2, member2.path("id").asInt());
        Assertions.assertEquals("test2", member2.path("aliasName").asText());

        Assertions.assertEquals("tester3", member3.path("name").asText());
        Assertions.assertEquals("tester3", member3.path("login").asText());
        Assertions.assertEquals("ACTIVE", member3.path("status").asText());
        Assertions.assertEquals(NOW_STRING, member3.path("joined").asText());
        Assertions.assertEquals(1.3, member3.path("rating").asDouble());
        Assertions.assertEquals(3, member3.path("id").asInt());
        Assertions.assertEquals(true, member3.path("aliasName").isNull());

    }

    @Test
    public void testFindByAllEmpty() throws Exception {

        // Arrange
        var data = new ArrayList<Member>();
        Mockito.when(members.findByAll()).thenReturn(data);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/members");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        var membersArray = response.path("_embedded").path("members");
        Assertions.assertEquals(0, membersArray.size());

    }

    @Test
    public void testGetById() throws Exception {

        // Arrange
        var data = Optional.of(new Member(1, "tester1", null, "tester1", 1.1f, "ACTIVE", NOW));
        Mockito.when(members.getById(1)).thenReturn(data);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/members/1");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        var member = response;
        Assertions.assertEquals("tester1", member.path("name").asText());
        Assertions.assertEquals("tester1", member.path("login").asText());
        Assertions.assertEquals("ACTIVE", member.path("status").asText());
        Assertions.assertEquals(NOW_STRING, member.path("joined").asText());
        Assertions.assertEquals(1.1, member.path("rating").asDouble());
        Assertions.assertEquals(1, member.path("id").asInt());
        Assertions.assertEquals(true, member.path("aliasName").isNull());

    }

    @Test
    public void testGetByIdNotFound() throws Exception {

        // Arrange
        Member member = null;
        var data = Optional.ofNullable(member);
        Mockito.when(members.getById(1)).thenReturn(data);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/members/1");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
       Assertions.assertTrue(response.isEmpty());

    }

    @Test
    public void testDelete() throws Exception {

        // Arrange
        Mockito.when(members.delete(1)).thenReturn(true);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.delete("/api/v1/members/1");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        Assertions.assertTrue(response.isEmpty());

    }

    @Test
    public void testDeleteNotFound() throws Exception {

        // Arrange
        Mockito.when(members.delete(1)).thenReturn(false);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.delete("/api/v1/members/1");
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        Assertions.assertTrue(response.isEmpty());

    }

    @Test
    public void testCreate() throws Exception {

        // Arrange
        var data = mapper.createObjectNode();
        data.put("name", "tester1");
        data.put("login", "tester1");
        var output = new Member(1, "tester1", "tester1", "tester1", 1.1f, "ACTIVE", LocalDateTime.now());

        Mockito.when(members.create(mapper.treeToValue(data, MemberData.class))).thenReturn(output);

        // Act
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/members").content(data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());
        var location = result.getResponse().getHeader("Location");

        // Assert
        Assertions.assertEquals(1, response.get("id").asInt());
        Assertions.assertEquals("tester1", response.get("name").asText());
        Assertions.assertEquals("tester1", response.get("aliasName").asText());
        Assertions.assertEquals("tester1", response.get("login").asText());
        Assertions.assertEquals("ACTIVE", response.get("status").asText());
        Assertions.assertEquals(1.1, response.get("rating").asDouble());
        Assertions.assertEquals("http://localhost/api/v1/members/1", location);

    }

    @Test
    public void testUpdate() throws Exception {

        // Arrange
        var data = mapper.createObjectNode();
        data.put("name", "tester1");
        data.put("login", "tester1");
        data.put("aliasName", "tester1");
        data.put("rating", 1.1);
        data.put("status", "ACTIVE");
        data.put("joined", NOW_STRING);
        data.put("id", 1);

        Mockito.when(members.update(1, mapper.treeToValue(data, Member.class))).thenReturn(mapper.treeToValue(data, Member.class));

        // Act
        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/members/1").content(data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());

        // Assert
        Assertions.assertEquals(1, response.get("id").asInt());
        Assertions.assertEquals("tester1", response.get("name").asText());
        Assertions.assertEquals("tester1", response.get("aliasName").asText());
        Assertions.assertEquals("tester1", response.get("login").asText());
        Assertions.assertEquals("ACTIVE", response.get("status").asText());
        Assertions.assertEquals(1.1, response.get("rating").asDouble());
    }
}