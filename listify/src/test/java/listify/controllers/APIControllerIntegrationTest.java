package listify.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;
import listify.services.ListifyService;
import listify.controllers.APIController;
import listify.domain.Activity;

@SpringBootTest(classes = APIController.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class APIControllerIntegrationTest {

    private ListifyService listifyService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	@Order(1)
	void registerTest() throws Exception {
		//successful registration: the user does not exist
		String url = "/API/register";
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("email", "test@gmail.com");
		requestObject.put("password", "pw");
		requestObject.put("username", "test_user");
		String requestJson = requestObject.toPrettyString();
		
		HttpSession session = mvc.perform(post(url)
										  .contentType(APPLICATION_JSON_UTF8)
										  .accept(APPLICATION_JSON_UTF8)
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
		
		
		//unsuccessful registration: the user already exists
		session = mvc.perform(post(url)
							  .contentType(APPLICATION_JSON_UTF8)
							  .accept(APPLICATION_JSON_UTF8)
							  .content(requestJson.getBytes())) 
							  .andDo(print())
							  .andExpect(status().isConflict()) //the controller should return status code 409 conflict (already existing user)
							  .andReturn()
							  .getRequest()
				              .getSession();

		assertNull(session.getAttribute("username")); //the session attribute should not be set
		
	}
	
	@Test
	@Order(2)
	void loginTest() throws Exception {
		//login with a previously created user (successful)
		
		String url = "/API/login";
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("email", "test@gmail.com");
		requestObject.put("password", "pw");
		requestObject.put("username", "test_user");
		String requestJson = requestObject.toPrettyString();
		
		mvc.perform(post(url)
					.contentType(APPLICATION_JSON_UTF8)
					.accept(APPLICATION_JSON_UTF8)
					.content(requestJson.getBytes())) 
					.andDo(print())
					.andExpect(status().isOk()).andReturn();
		
		
		//login with a not existing user (should fail)	
		
		requestObject = objectMapper.createObjectNode();
		requestObject.put("email", "test2@gmail.com");
		requestObject.put("password", "pw");
		requestObject.put("username", "not_existing_user");
		requestJson = requestObject.toPrettyString();
		
		mvc.perform(post(url)
					.contentType(APPLICATION_JSON_UTF8)
					.accept(APPLICATION_JSON_UTF8)
					.content(requestJson.getBytes())) 
					.andDo(print())
					.andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@Order(3)
	void logoutTest() throws Exception{
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
	@Order(4)
	void createToDoListTest() throws Exception {
		String url = "/API/test_user/createToDoList";
		//successful list creation
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		MvcResult result = mvc.perform(post(url)
									   .sessionAttrs(sessionAttrs)
									   .contentType(APPLICATION_JSON_UTF8)
									   .accept(APPLICATION_JSON_UTF8)
									   .content("first_list".getBytes())) 
							 		   .andDo(print())
							 		   .andExpect(status().isCreated()) //the status should be 201 created
							 		   .andReturn();
		
		//and the body should contain the new id 
		String newListId = result.getResponse().getContentAsString();
		assertNotNull(newListId);
		
		url = "/API/test_user/updateToDoListName/" + newListId;
		//successful list update
	
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
	
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.accept(APPLICATION_JSON_UTF8)
					.content("second_list".getBytes())) 
				 	.andDo(print())
				 	.andExpect(status().isOk()) //the status should be 200 ok
				 	.andReturn();
		
		//cannot update a not existing list
		url = "/API/test_user/updateToDoListName/" + newListId + "1";
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.accept(APPLICATION_JSON_UTF8)
					.content("second_list".getBytes())) 
				 	.andDo(print())
				 	.andExpect(status().isNotFound()) //the status should be 404 not found
				 	.andReturn();
		
		
		
		//try to create an activity
		
		url = "/API/test_user/updateToDoList/" + newListId + "/createActivity";
		//successful activity creation
		
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
		
		ObjectNode requestObject = objectMapper.createObjectNode();
		requestObject.put("name", "first activity");
		requestObject.put("priority", 4);
		requestObject.put("expirationDate", LocalDate.now().toString());
		requestObject.put("category", "To Do");
		String requestJson = requestObject.toPrettyString();
		
		result = mvc.perform(post(url)
							 .sessionAttrs(sessionAttrs)
							 .contentType(APPLICATION_JSON_UTF8)
							 .accept(APPLICATION_JSON_UTF8)
							 .content(requestJson.getBytes())) 
				 			 .andDo(print())
				 			 .andExpect(status().isCreated()) //the status should be 201 created
				 			 .andReturn();
		
		String newActivityId = result.getResponse().getContentAsString();
		assertNotNull(newActivityId);
		
		
		//try to update the activity 
		url = "/API/test_user/updateToDoList/" + newListId + "/updateActivity/" + newActivityId;
		//successful activity update
	
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
	
		requestObject = objectMapper.createObjectNode();
		requestObject.put("name", "first activity 1");
		requestObject.put("priority", 5);
		requestObject.put("expirationDate", LocalDate.now().toString());
		requestObject.put("category", "Completed");
		requestJson = requestObject.toPrettyString();
		
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.content(requestJson.getBytes())) 
				    .andDo(print())
				    .andExpect(status().isOk()) //the status should be 200 ok
				    .andReturn();
		
		//try to update activity category
		url = "/API/test_user/updateToDoList/" + newListId + "/updateActivityCategory/" + newActivityId;
		//successful activity update
	
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
		
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.content("In Progress".getBytes())) 
				 	.andDo(print())
				 	.andExpect(status().isOk()) //the status should be 200 ok
				 	.andReturn();
		
		//cannot update a not existing activity
		
		url = "/API/test_user/updateToDoList/" + newListId + "/updateActivity/" + newActivityId + "2";

		System.out.println(url);
		
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.content(requestJson.getBytes())) 
				 	.andDo(print())
				 	.andExpect(status().isNotFound()) //the status should be 404 not found
				 	.andReturn();
		
		//cannot update the category of a not existing activity
		
		url = "/API/test_user/updateToDoList/" + newListId + "/updateActivityCategory/" + newActivityId + "2";
		
		mvc.perform(put(url)
					.sessionAttrs(sessionAttrs)
					.contentType(APPLICATION_JSON_UTF8)
					.content("To Do".getBytes())) 
				 	.andDo(print())
				 	.andExpect(status().isNotFound()) //the status should be 404 not found
				 	.andReturn();
		
		//try to delete an activity
		url = "/API/test_user/updateToDoList/" + newListId + "/deleteActivity/" + newActivityId;
		//successful activity deletion
		
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
		
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs)) 
				 	.andDo(print())
				 	.andExpect(status().isOk()) //the status should be 200 ok
				 	.andReturn();
		
		//cannot delete the same activity two times
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs))
			 		.andDo(print())
			 		.andExpect(status().isNotFound()) //the status should be 404 not found
			 		.andReturn();
		
		//try to delete a toDoList
		url = "/API/test_user/deleteToDoList/" + newListId;
		//successful list deletion
	
		//Map<String, Object> sessionAttrs = new HashMap<>();
		//sessionAttrs.put("username", "test_user");
	
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs)) 
				 	.andDo(print())
				 	.andExpect(status().isOk()) //the status should be 200 ok
				 	.andReturn();
		
		//cannot delete the same toDoList two times
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs))
				 	.andDo(print())
				 	.andExpect(status().isNotFound()) //the status should be 404 not found
				 	.andReturn();
	}
	
	
	
	@Test
	@Order(7)
	void deleteUserTest() throws Exception {
		//delete an existing user (should be successful)
		String url = "/API/test_user/deleteUser";
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs)) 
					.andDo(print())
					.andExpect(status().isOk()) //the status should be 200 ok
					.andReturn();
		
		//delete a not existing user(unsuccessful)
		mvc.perform(delete(url)
					.sessionAttrs(sessionAttrs)) 
					.andDo(print())
					.andExpect(status().isNotFound()) //the status should be 404 not found
					.andReturn();
	}
	
}