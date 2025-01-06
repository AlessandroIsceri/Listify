package listify.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;
import listify.config.WebConfig;
import listify.domain.Activity;
import listify.services.ListifyService;

@ContextConfiguration(classes = WebConfig.class)
//@WebMvcTest(APIController.class)
@ExtendWith(MockitoExtension.class) 
class APIControllerTest {

	@InjectMocks
    private APIController controller;
	
	@Mock
	ListifyService listifyService;
	
	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();
	
	private MockMvc mvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@BeforeEach
    public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
	
	@Test
	void registerTest() throws Exception {
		String url = "/API/register";
		
		//simulate a successful registration
		when(listifyService.createUser(anyString(), anyString(), anyString())).thenReturn(true);
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("email", "test@gmail.com");
		requestObject.put("password", "pw");
		requestObject.put("username", "test_user");
		String requestJson = requestObject.toPrettyString();
		
		HttpSession session = mvc.perform(post(url)
								 .contentType(APPLICATION_JSON_UTF8)
								 .content(requestJson.getBytes())) 
								 .andDo(print())
								 .andExpect(status().isCreated()) //the status should be 201 created
								 .andReturn()
								 .getRequest()
					             .getSession();
		
		//the session attribute username should not be null
		assertNotNull(session.getAttribute("username")); 
		
		//the session attribute username should be "test_user"
		assertEquals("test_user", session.getAttribute("username"));
		
		//simulate an unsuccessful registration
		when(listifyService.createUser(anyString(), anyString(), anyString())).thenReturn(false);
		session = mvc.perform(post(url)
							  .contentType(APPLICATION_JSON_UTF8)
							  .content(requestJson.getBytes())) 
							  .andDo(print())
							  .andExpect(status().isNotFound()) //the status should be 404 not found
							  .andReturn()
							  .getRequest()
				              .getSession();

		//the session attribute username should be null
		assertNull(session.getAttribute("username")); 
	}
	
	@Test
	//TODO MANCA CONTROLLARE IL BODY RITORNATO CON LOGIN SUCCESSFUL
	void loginTest() throws Exception {
		String url = "/API/login";
		
		//simulate an successful login
		when(listifyService.login(anyString(), anyString())).thenReturn("test_user");
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("email", "test@gmail.com");
		requestObject.put("password", "pw");
		String requestJson = requestObject.toPrettyString();
		
		HttpSession session = mvc.perform(post(url)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn()
				 .getRequest()
	             .getSession();

		//the session attribute username should not be null
		assertNotNull(session.getAttribute("username")); 
		
		//the session attribute username should be "test_user"
		assertEquals("test_user", session.getAttribute("username"));

		//simulate an unsuccessful login
		when(listifyService.login(anyString(), anyString())).thenReturn(null);
		
		session = mvc.perform(post(url)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isNotFound()) //the status should be 404 not found
				 .andReturn()
				 .getRequest()
	             .getSession();
		
		//the session attribute username should be null
		assertNull(session.getAttribute("username")); 
	}

	@Test
	void logoutTest() throws Exception {
		String url = "/API/test_user/logout";
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		//successful logout (the user was logged in -> the session attribute was set)
		HttpSession session = mvc.perform(post(url)
								 .sessionAttrs(sessionAttrs)) 
								 .andDo(print())
								 .andExpect(status().isOk()) //the status should be 200 ok
								 .andReturn()
								 .getRequest()
					             .getSession();
		
		//the username session attribute should be removed
		assertNull(session.getAttribute("username"));
		
		//unsuccessful logout (the user was not logged in -> the session attribute was not set)
		session = mvc.perform(post(url)) 
								 .andDo(print())
								 .andExpect(status().isUnauthorized()) //the status should be unathorized
								 .andReturn()
								 .getRequest()
					             .getSession();
		
	}
	
	@Test
	void createToDoListTest() throws Exception {
		String url = "/API/test_user/createList";
		//simulate successful list creation
		when(listifyService.createToDoList(anyString(), anyString())).thenReturn(10);
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		MvcResult result = mvc.perform(post(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content("first_list".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isCreated()) //the status should be 201 created
				 .andReturn();
		
		//and the body should contain the new id (10)
		assertEquals("10", result.getResponse().getContentAsString());
		
		//simulate unsuccessful list creation
		when(listifyService.createToDoList(anyString(), anyString())).thenReturn(-1);

		mvc.perform(post(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content("first_list".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isInternalServerError()) //the status should be 500 internal server error
				 .andReturn();
		
	}
	
	@Test
	void updateToDoListNameTest() throws Exception {
		String url = "/API/test_user/updateListName/10";
		//simulate successful list update
		when(listifyService.updateToDoListName(anyString(), anyInt(), anyString())).thenReturn(true);
	
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
	
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content("second_list".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn();
		
		//simulate unsuccessful list update
		when(listifyService.updateToDoListName(anyString(), anyInt(), anyString())).thenReturn(false);
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content("second_list".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isNotFound()) //the status should be 404 not found
				 .andReturn();
		
	}
	
	@Test 
	void deleteToDoListTest() throws Exception {
		String url = "/API/test_user/deleteList/10";
		//simulate successful list deletion
		when(listifyService.deleteToDoList(anyString(), anyInt())).thenReturn(true);
	
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
	
		mvc.perform(delete(url).sessionAttrs(sessionAttrs)) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn();
		
		//simulate unsuccessful list deletion
		when(listifyService.deleteToDoList(anyString(), anyInt())).thenReturn(false);
		mvc.perform(delete(url).sessionAttrs(sessionAttrs))
				 .andDo(print())
				 .andExpect(status().isNotFound()) //the status should be 404 not found
				 .andReturn();
	}
	
	@Test 
	void createActivityTest() throws Exception {
		String url = "/API/test_user/toDoList/10/createActivity";
		//simulate successful activity creation
		when(listifyService.createActivity(anyString(), anyInt(), any(Activity.class))).thenReturn(15);
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("name", "first activity");
		requestObject.put("priority", 4);
		requestObject.put("expirationDate", LocalDate.now().toString());
		requestObject.put("category", "To Do");
		String requestJson = requestObject.toPrettyString();
		
		MvcResult result = mvc.perform(post(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isCreated()) //the status should be 201 created
				 .andReturn();
		
		//and the body should contain the new activity id (15)
		assertEquals("15", result.getResponse().getContentAsString());
		
		//simulate unsuccessful activity creation
		when(listifyService.createActivity(anyString(), anyInt(), any(Activity.class))).thenReturn(-1);
		
		mvc.perform(post(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .accept(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isInternalServerError()) //the status should be 500 internal server error
				 .andReturn();
	}
	
	@Test
	void updateActivityTest() throws Exception {
		String url = "/API/test_user/updateList/10/updateActivity/15";
		//simulate successful activity update
		when(listifyService.updateActivity(anyString(), anyInt(), anyInt(), any(Activity.class))).thenReturn(true);
	
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
	
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("name", "first activity");
		requestObject.put("priority", 4);
		requestObject.put("expirationDate", LocalDate.now().toString());
		requestObject.put("category", "To Do");
		String requestJson = requestObject.toPrettyString();
		
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn();
		
		//simulate unsuccessful activity update
		when(listifyService.updateActivity(anyString(), anyInt(), anyInt(), any(Activity.class))).thenReturn(false);
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .content(requestJson.getBytes())) 
				 .andDo(print())
				 .andExpect(status().isNotFound()) //the status should be 404 not found
				 .andReturn();
	}
	
	@Test
	void updateActivityCategoryTest() throws Exception {
		String url = "/API/test_user/updateList/10/updateActivityCategory/15";
		//simulate successful activity update
		when(listifyService.updateActivityCategory(anyString(), anyInt(), anyInt(), anyString())).thenReturn(true);
	
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .content("Completed".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn();
		
		//simulate unsuccessful activity update
		when(listifyService.updateActivityCategory(anyString(), anyInt(), anyInt(), anyString())).thenReturn(false);
		mvc.perform(put(url).sessionAttrs(sessionAttrs)
				 .contentType(APPLICATION_JSON_UTF8)
				 .content("To Do".getBytes())) 
				 .andDo(print())
				 .andExpect(status().isNotFound()) //the status should be 404 not found
				 .andReturn();
	}
	
	@Test
	void deleteActivityTest() throws Exception {
		String url = "/API/test_user/updateList/10/deleteActivity/15";
		//simulate successful activity deletion
		when(listifyService.deleteActivity(anyString(), anyInt(), anyInt())).thenReturn(true);
	
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		mvc.perform(delete(url).sessionAttrs(sessionAttrs)) 
				 .andDo(print())
				 .andExpect(status().isOk()) //the status should be 200 ok
				 .andReturn();
		
		//simulate unsuccessful activity deletion
		when(listifyService.deleteActivity(anyString(), anyInt(), anyInt())).thenReturn(false);
		mvc.perform(delete(url).sessionAttrs(sessionAttrs))
			 .andDo(print())
			 .andExpect(status().isNotFound()) //the status should be 404 not found
			 .andReturn();
	}
	
	//TODO MANCANO TEST NON AUTORIZZATI
}