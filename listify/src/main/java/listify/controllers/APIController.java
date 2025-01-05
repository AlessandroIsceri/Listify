package listify.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpSession;
import listify.domain.Activity;
import listify.services.ListifyService;

@RestController
public class APIController {
	private ListifyService listifyService; //business logic object
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public APIController() {
		listifyService = ListifyService.getInstance();
		// register module to handle dates
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
        System.out.println("APIController is loaded!");
	}
		
	@PostMapping(value="/API/login", consumes = "application/json;charset=UTF-8")
	public ResponseEntity<String> login(HttpSession session, 
										@RequestBody Map<String, String> body){
		System.out.println("CIAO");
		String username = listifyService.login(body.get("email"), body.get("password"));
		if(username != null) {
			//successful login
			session.setAttribute("username", username); //set session attribute
			return ResponseEntity.ok().body(username);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/register")
	public ResponseEntity<?> createUser(HttpSession session, 
									 @RequestBody Map<String, String> body){
		if(listifyService.createUser(body.get("email"), body.get("username"), body.get("password"))) {
			//successful registration
			session.setAttribute("username", body.get("username")); //set session attribute
			return ResponseEntity.status(HttpStatus.CREATED).build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/logout")
	public ResponseEntity<?> logout(HttpSession session){
		String username = (String) session.getAttribute("username");
		if(username != null) {
			//successful logout
			session.invalidate(); //destroy the session
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/createList")
	@ResponseBody
	public ResponseEntity<String> createList(HttpSession session,
							 @PathVariable(value="username") String username, 
							 @RequestBody String listName){
		//check if a logged user is trying to access the data of another user
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("-1");
		}
		//return the id of the new list with .body()
		return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(listifyService.createToDoList(username, listName)));
	}
	
	@PutMapping("/API/{username}/updateListName/{listId}")
	public ResponseEntity<?> updateList(HttpSession session,
									 @PathVariable(value="username") String username, 
									 @PathVariable(value="listId") int listId,
									 @RequestBody String newListName) {
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(listifyService.updateToDoListName(username, listId, newListName)) {
			//successful update
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/API/{username}/deleteList/{listId}")
	public ResponseEntity<?> deleteList(HttpSession session,
									 @PathVariable(value="username") String username, 
			 						 @PathVariable(value="listId") int listId){
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(listifyService.deleteToDoList(username, listId)) {
			//successful deletion
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/API/{username}/toDoList/{listId}/createActivity")
	@ResponseBody
	public ResponseEntity<String> createActivity(HttpSession session,
								 @PathVariable(value="username") String username, 
			 					 @PathVariable(value="listId") int listId,
			 					 @RequestBody Activity activity) {
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("-1");
		}
		//return the id of the new activiy with .body()
		return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(listifyService.createActivity(username, listId, activity)));
	}
	
	@PutMapping("/API/{username}/updateList/{listId}/updateActivity/{activityId}")
	public ResponseEntity<?> updateActivity(HttpSession session,
										 @PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId,
			 							 @RequestBody Activity activity) {
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(listifyService.updateActivity(username, listId, activityId, activity)) {
			//successful update
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/API/{username}/updateList/{listId}/updateActivityCategory/{activityId}")
	public ResponseEntity<?> updateActivity(HttpSession session,
										 @PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId,
			 							 @RequestBody String category) {
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(listifyService.updateActivityCategory(username, listId, activityId, category)) {
			//successful update
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/API/{username}/updateList/{listId}/deleteActivity/{activityId}")
	public ResponseEntity<?> deleteActivity(HttpSession session,
										 @PathVariable(value="username") String username, 
			 							 @PathVariable(value="listId") int listId,
			 							 @PathVariable(value="activityId") int activityId) {
		if(!(username.equals(session.getAttribute("username")))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(listifyService.deleteActivity(username, listId, activityId)) {
			//successful deletion
			return ResponseEntity.ok().build(); 
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
