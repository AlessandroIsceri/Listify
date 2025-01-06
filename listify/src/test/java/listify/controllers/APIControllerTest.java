package listify.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;
import listify.config.WebConfig;
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
		
		//simulate an unsuccessful registration
		when(listifyService.createUser(anyString(), anyString(), anyString())).thenReturn(false);
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
	
	
	

}