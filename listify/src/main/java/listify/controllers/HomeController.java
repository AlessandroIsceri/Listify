package listify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import listify.domain.ToDoList;
import listify.domain.User;
import listify.services.UserService;

@Controller
public class HomeController {
	private UserService userService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public HomeController() {
		userService = UserService.getInstance();
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("HomeController is loaded!");
	}
	
	@GetMapping("{username}/home")
	public ModelAndView getHomePage(@PathVariable(value="username") String username){
		ModelAndView m = new ModelAndView();
		User user = userService.getUser(username);
		m.addObject("username", user.getUsername());
		m.addObject("toDoLists", user.getToDoLists());
		m.setViewName("home");	
		return m;
	}
	
	@GetMapping("{username}/toDoList/{listId}")
	public ModelAndView getListPage(@PathVariable(value="username") String username, 
									@PathVariable(value="listId") int listId){
		ModelAndView m = new ModelAndView();
		m.setViewName("toDoList");
		m.addObject("username", username);
		ToDoList list = userService.getToDoList(username, listId);
		m.addObject("list", list);
		return m;
	}
}
