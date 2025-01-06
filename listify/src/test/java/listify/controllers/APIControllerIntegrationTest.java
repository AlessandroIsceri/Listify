package listify.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;
import listify.services.ListifyService;
import listify.controllers.APIController;

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
							  .andExpect(status().isNotFound()) //the controller should return status code 404
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
	}
	
}