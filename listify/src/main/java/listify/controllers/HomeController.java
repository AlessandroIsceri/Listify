package listify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	public HomeController() {
        System.out.println("HomeController is loaded!");
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
		if(email.equals("aless") && password.equals("1")){
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
		m.setViewName("home");	
		return m;
	}
}
