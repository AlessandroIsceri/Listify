package listify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import listify.domain.ToDoList;
import listify.domain.User;

@Controller
public class HomeController {
	
	User aless;
	
	public HomeController() {
        System.out.println("HomeController is loaded!");
        aless = new User("aless@gmail.com", "pw", "aless"); //create user to test the application without DB connection
        //create 2 to-to lists to display
        ToDoList list1 = new ToDoList("lista_progetto"); 
        ToDoList list2 = new ToDoList("lista_spesa"); 
        aless.addToDoList(list1);
        aless.addToDoList(list2);
	}
	
	@GetMapping("/login")
	public ModelAndView getLoginPage(){
		ModelAndView m = new ModelAndView();
		m.setViewName("login");	
		return m;
	}
	
	@PostMapping("/login")
	public ModelAndView getPage(@RequestParam("email") String email,@RequestParam("password") String password){
		ModelAndView m = new ModelAndView();
		if(email.equals(aless.getEmail()) && password.equals(aless.getPassword())){
			return new ModelAndView("redirect:/home");
		}
		else {
			m.setViewName("failedLogin");
		}
		return m;
	}
	
	@GetMapping("/home")
	public ModelAndView getHomePage(){
		ModelAndView m = new ModelAndView();
		System.out.println(aless.getUsername());
		m.addObject("username", aless.getUsername());
		m.addObject("toDoLists", aless.getToDoLists());
		m.setViewName("home");	
		return m;
	}
}
