package listify.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import listify.domain.ToDoList;
import listify.domain.User;
import listify.services.UserService;

@Controller
public class HomeController {
	private UserService userService;
	
	public HomeController() {
		userService = new UserService();
        System.out.println("HomeController is loaded!");
	}
	
	@GetMapping("/login")
	public ModelAndView getLoginPage(){
		ModelAndView m = new ModelAndView();
		m.setViewName("login");	
		return m;
	}
	
	@PostMapping("/login")
	public ModelAndView getPage(@RequestParam("email") String email,
								@RequestParam("password") String password){
		ModelAndView m = new ModelAndView();
		String username = userService.login(email, password);
		if(username != null) {
			return new ModelAndView("redirect:/home/"+username);
		}
		m.setViewName("failedLogin");
		return m;
	}
	
	@GetMapping("/home/{username}")
	public ModelAndView getHomePage(@PathVariable(value="username") String username){
		ModelAndView m = new ModelAndView();
		User user = userService.getUser(username);
		m.addObject("username", user.getUsername());
		m.addObject("toDoLists", user.getToDoLists());
		m.setViewName("home");	
		return m;
	}
	
	@GetMapping("/home/{username}/{listName}")
	public ModelAndView getListPage(@PathVariable(value="username") String username, 
									@PathVariable(value="listName") String listName){
		ModelAndView m = new ModelAndView();
		m.setViewName("toDoList");
		m.addObject("username", username);
		ToDoList list = userService.getToDoList(username, listName);
		m.addObject("list", list);
		return m;
	}
	
	@GetMapping("/API/getNewId")
	@ResponseBody
	public String getNewId() {
		return "5";
	}
	
}
