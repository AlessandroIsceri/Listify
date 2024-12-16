package listify.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;

public class UserService {
	List<User> users;
	
	public UserService() {
		users = new ArrayList<User>();
		// populate the database with some data
		User aless = new User("aless@gmail.com", "pw", "aless"); //create user to test the application without DB connection
        //create 2 to-to lists to display
        ToDoList list1 = new ToDoList("lista_progetto");
        list1.addItem(new Activity("impostare gitlab", 10, LocalDate.of(2025, 1, 20), "In Progress"));
        list1.addItem(new Activity("scrivere codice", 3, LocalDate.of(2025, 4, 12), "To Do"));
        list1.addItem(new Activity("scrivere test", 5, "Completed"));
        ToDoList list2 = new ToDoList("lista_spesa"); 
        aless.addToDoList(list1);
        aless.addToDoList(list2);
        users.add(aless);
	}
	
	public String login(String email, String password) {
		for(User user : users) {
			if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
				return user.getUsername();
			}
		}
		return null;
	}
	
	public User getUser(String username) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				return user;
			}
		}
		return null;
	}

	public ToDoList getToDoList(String username, String listName) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listName.equals(list.getName())){
						return list;
					}
				}
			}
		}
		return null;
	}
	
}
