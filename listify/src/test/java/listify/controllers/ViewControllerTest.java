package listify.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import listify.config.WebConfig;
import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.services.ListifyService;

@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(MockitoExtension.class) 
class ViewControllerTest {

	@InjectMocks
    private ViewController controller;
	
	@Mock
	ListifyService listifyService;
	
	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();
	
	private MockMvc mvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private Activity createActivity(String name, String category, LocalDate date, int priority) {
		Activity activity = new Activity();
		activity.setName(name);
		activity.setCategory(category);
		activity.setExpirationDate(date);
		activity.setPriority(priority);
		return activity;
	}
	
	private User createTestUser() {
		User user = new User("test@gmail.com", "password", "test_user");
		ToDoList toDoList1 = new ToDoList(10, "first_list");
		
		Activity activity1 = createActivity("first_activity", "To Do", LocalDate.now(), 3);
		Activity activity2 = createActivity("second_activity", "Completed", null, 8);
		
		ToDoList toDoList2 = new ToDoList(15, "second_list");
		
		toDoList1.addActivity(activity1);
		toDoList1.addActivity(activity2);
		
		user.addToDoList(toDoList1);
		user.addToDoList(toDoList2);
		return user;
	}
	
	@BeforeEach
    void setup() {
		//setup the environment to send mock http requests
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
		
		mvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }
	
	@Test
	void getHomePageTest() throws Exception {
		//successful get request (session attribute username == path variable username)
		String url = "/test_user/home";
		
		User user = createTestUser();
		
		when(listifyService.getUser(anyString())).thenReturn(user);
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		mvc.perform(get(url)
					.sessionAttrs(sessionAttrs)) 
					.andDo(print())
					.andExpect(view().name("home")) //the view name should be "home"
					.andExpect(forwardedUrl("/WEB-INF/views/home.jsp"))
					.andExpect(model().attribute("username", "test_user")) //the attribute username should be equal to "test_user"
					.andExpect(model().attribute("toDoLists", user.getToDoLists())); //the attribute toDoLists should be equals to user's to do lists
		
		//simulate unauthorized request (no session parameter username)
		mvc.perform(get(url))
					.andDo(print())
					.andExpect(view().name("errorPage")) //the view name should be "errorPage"
					.andExpect(forwardedUrl("/WEB-INF/views/errorPage.jsp"));
		
	}
	
	@Test 
	void getToDoListPageTest() throws Exception{
		String url = "/test_user/toDoList/10";

		User user = createTestUser();
		
		when(listifyService.getToDoList(anyString(), anyInt())).thenReturn(user.getToDoList(10));
		
		Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("username", "test_user");
		
		mvc.perform(get(url)
					.sessionAttrs(sessionAttrs)) 
				 	.andDo(print())
					.andExpect(view().name("toDoList")) //the view name should be "toDoList"
					.andExpect(forwardedUrl("/WEB-INF/views/toDoList.jsp"))
					.andExpect(model().attribute("username", "test_user")) //the attribute username should be equal to "test_user"
					.andExpect(model().attribute("toDoList", user.getToDoList(10))); //the attribute list should be equal to user's to do list with id 10
					
		//simulate unauthorized request (wrong session parameter username)
		sessionAttrs.put("username", "test_user_1");
		mvc.perform(get(url)
					.sessionAttrs(sessionAttrs))
					.andDo(print())
					.andExpect(view().name("errorPage")) //the view name should be "errorPage"
					.andExpect(forwardedUrl("/WEB-INF/views/errorPage.jsp"));
		
	}

}
