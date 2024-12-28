package listify.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import listify.domain.ToDoList;
import listify.domain.User;
import listify.services.ListifyService;

@Controller
public class HomeController {
	private ListifyService listifyService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public HomeController() {
		listifyService = ListifyService.getInstance();
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("HomeController is loaded!");
	}
	
	@GetMapping("{username}/home")
	public ModelAndView getHomePage(HttpSession session, 
									@PathVariable(value="username") String username){
		//check if a logged user is trying to access a webpage of another user
		if(!(username.equals(session.getAttribute("username")))) {
			return new ModelAndView("errorPage");
		}
		ModelAndView m = new ModelAndView();
		User user = listifyService.getUser(username);
		m.addObject("username", user.getUsername());
		m.addObject("toDoLists", user.getToDoLists());
		m.setViewName("home");	
		return m;
	}
	
	@GetMapping("{username}/toDoList/{listId}")
	public ModelAndView getListPage(HttpSession session,
									@PathVariable(value="username") String username, 
									@PathVariable(value="listId") int listId){
		//check if a logged user is trying to access a webpage of another user
		if(!(username.equals(session.getAttribute("username")))) {
			return new ModelAndView("errorPage");
		}
		ModelAndView m = new ModelAndView();
		m.setViewName("toDoList");
		m.addObject("username", username);
		ToDoList list = listifyService.getToDoList(username, listId);
		m.addObject("list", list);
		return m;
	}
}
