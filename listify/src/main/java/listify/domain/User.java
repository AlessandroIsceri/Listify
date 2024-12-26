package listify.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String email;
	private String password;
	private String username;
	private List<ToDoList> toDoLists;
	
	public User() {
		super();
	}
	
	public User(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ToDoList> getToDoLists() {
		return toDoLists;
	}

	public void setToDoLists(List<ToDoList> toDoLists) {
		this.toDoLists = toDoLists;
	}
	
	public boolean addToDoList(ToDoList l) {
		return toDoLists.add(l);
	}
	
}
