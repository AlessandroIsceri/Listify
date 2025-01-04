package listify.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
	private String email;
	private String password;
	private String username;
	private List<ToDoList> toDoLists;
	
	//constructor for Jackson
	public User() {
		super();
		this.toDoLists = new ArrayList<ToDoList>();
	}
	
	public User(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.toDoLists = new ArrayList<ToDoList>();
	}

	//getters and setters
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
	
	public void removeToDoList(ToDoList list) {
		toDoLists.remove(list);
	}
	
	public void addToDoList(ToDoList l) {
		toDoLists.add(l);
	}
	
	public ToDoList getToDoList(int listId) {
		for(ToDoList list : toDoLists) {
			if(listId == list.getId()){ //found the target list
				return list;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", username=" + username + ", toDoLists=" + toDoLists.toString()
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password, toDoLists, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Objects.equals(toDoLists, other.toDoLists) && Objects.equals(username, other.username);
	}
	
}
