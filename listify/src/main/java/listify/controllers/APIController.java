package listify.controllers;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import listify.domain.Activity;
import listify.services.UserService;

@Controller
public class APIController {
	private UserService userService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public APIController() {
		userService = UserService.getInstance();
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("APIController is loaded!");
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
	
	@PostMapping("/API/{username}/toDoList/{listId}/getNewActivityId")
	@ResponseBody
	public String getNewActivityId(@PathVariable(value="username") String username, 
			 					   @PathVariable(value="listId") int listId,
			 					  @RequestBody Activity activity) {
		return "" + userService.createActivity(username, listId, activity);
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
