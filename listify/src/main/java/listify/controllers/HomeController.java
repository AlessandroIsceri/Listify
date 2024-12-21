package listify.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.services.UserService;

@Controller
public class HomeController {
	private UserService userService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public HomeController() {
		userService = new UserService();
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("HomeController is loaded!");
	}
	
	@PostMapping("/API/login")
	public ResponseEntity<String> getPage(@RequestBody Map<String, String> body){
		String username = userService.login(body.get("email"), body.get("password"));
		if(username != null) {
			return ResponseEntity.ok().body(username);
		}else {
			return ResponseEntity.notFound().build();
		}
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
	
	@GetMapping("/API/getNewActivityId")
	@ResponseBody
	public String getNewActivityId() {
		return "5";
	}
	
	@GetMapping("/API/getNewListId")
	@ResponseBody
	public String getNewListId() {
		return "5";
	}
	
	@PutMapping("/API/{username}/updateList/{listId}")
	public ResponseEntity updateList(@PathVariable(value="username") String username, 
							 @PathVariable(value="listId") int listId,
							 @RequestBody Activity[] toDoList) {
		if(userService.updateToDoList(username, listId, toDoList)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PutMapping("/API/{username}/updateListName/{listId}")
	public ResponseEntity updateList(@PathVariable(value="username") String username, 
							 @PathVariable(value="listId") int listId,
							 @RequestBody String newListName) {
		if(userService.updateToDoListName(username, listId, newListName)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/API/{username}/deleteList/{listId}")
	public ResponseEntity deleteList(@PathVariable(value="username") String username, 
			 						@PathVariable(value="listId") int listId){
		if(userService.deleteList(username, listId)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/createNewList/{listName}")
	public ResponseEntity createList(@PathVariable(value="username") String username, 
									 @PathVariable(value="listName") String listName){
		if(userService.createList(username, listName)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/register")
	public ResponseEntity createUser(@RequestBody Map<String, String> body){
		
		if(userService.createUser(body.get("email"), body.get("username"), body.get("password"))) {
			return ResponseEntity.status(HttpStatus.CREATED).build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
