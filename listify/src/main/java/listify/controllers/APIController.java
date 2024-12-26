package listify.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import listify.domain.Activity;
import listify.services.ListifyService;

@Controller
public class APIController {
	private ListifyService listifyService;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public APIController() {
		System.out.println("istanzio il service... ");
		listifyService = ListifyService.getInstance();
		System.out.println("service istanziato");
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("APIController is loaded!");
	}
	
	@PostMapping("/API/login")
	public ResponseEntity<String> getPage(@RequestBody Map<String, String> body){
		String username = listifyService.login(body.get("email"), body.get("password"));
		if(username != null) {
			return ResponseEntity.ok().body(username);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/register")
	public ResponseEntity createUser(@RequestBody Map<String, String> body){
		if(listifyService.createUser(body.get("email"), body.get("username"), body.get("password"))) {
			return ResponseEntity.status(HttpStatus.CREATED).build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/createList")
	@ResponseBody
	public String createList(@PathVariable(value="username") String username, 
							 @RequestBody String listName){
		return "" + listifyService.createList(username, listName);
	}
	
	@PutMapping("/API/{username}/updateListName/{listId}")
	public ResponseEntity updateList(@PathVariable(value="username") String username, 
									 @PathVariable(value="listId") int listId,
									 @RequestBody String newListName) {
		if(listifyService.updateToDoListName(username, listId, newListName)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/API/{username}/deleteList/{listId}")
	public ResponseEntity deleteList(@PathVariable(value="username") String username, 
			 						 @PathVariable(value="listId") int listId){
		if(listifyService.deleteList(username, listId)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/toDoList/{listId}/createActivity")
	@ResponseBody
	public String createActivity(@PathVariable(value="username") String username, 
			 					 @PathVariable(value="listId") int listId,
			 					 @RequestBody Activity activity) {
		return "" + listifyService.createActivity(username, listId, activity);
	}
	
	@PutMapping("/API/{username}/updateList/{listId}/updateActivity/{activityId}")
	public ResponseEntity updateActivity(@PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId,
			 							 @RequestBody Activity activity) {
		if(listifyService.updateActivity(username, listId, activityId, activity)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/API/{username}/updateList/{listId}/updateActivityCategory/{activityId}")
	public ResponseEntity updateActivity(@PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId,
			 							 @RequestBody String category) {
		if(listifyService.updateActivityCategory(username, listId, activityId, category)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/API/{username}/updateList/{listId}/deleteActivity/{activityId}")
	public ResponseEntity deleteActivity(@PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId) {
		if(listifyService.deleteActivity(username, listId, activityId)) {
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
