package listify.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.repository.ActivityRepository;
import listify.repository.ToDoListRepository;
import listify.repository.UserRepository;

public class UserService {
	List<User> users;
	private static UserService instance;

	public static UserService getInstance(){
		if (instance == null){
			instance = new UserService();
		}
		return instance;
	}
	
	private UserService() {
		users = new ArrayList<User>();
		// populate the database with some data
		/*User aless = new User("aless@gmail.com", "pw", "aless"); //create user to test the application without DB connection
        //create 2 to-to lists to display
        ToDoList list1 = new ToDoList(1, "lista_progetto");
        list1.addItem(new Activity(1, "impostare gitlab", 10, LocalDate.of(2025, 1, 20), "In Progress"));
        list1.addItem(new Activity(2, "scrivere codice", 3, LocalDate.of(2025, 4, 12), "To Do"));
        list1.addItem(new Activity(3, "scrivere test", 5, "Completed"));
        ToDoList list2 = new ToDoList(2, "lista_spesa"); 
        aless.addToDoList(list1);
        aless.addToDoList(list2);
        users.add(aless);*/
        
        UserRepository userRepository = new UserRepository();
        ToDoListRepository toDoListRepository = new ToDoListRepository();
        ActivityRepository activityepository = new ActivityRepository();
        users = userRepository.getUsers();
        
        for(User user : users) {
        	//retrieve todolist information
        	ArrayList<ToDoList> toDoLists = toDoListRepository.getToDoLists(user);
        	for(ToDoList list : toDoLists) {
        		ArrayList<Activity> activities = activityepository.getActivities(list);
        		for(Activity activity : activities) {
        			list.addItem(activity);
        		}
        		user.addToDoList(list);
        	}
        }
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

	public ToDoList getToDoList(String username, int listId) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						return list;
					}
				}
			}
		}
		return null;
	}
	
	public boolean updateToDoList(String username, int listId, Activity[] updatedToDoList) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						//update the list
						list.setToDoList(Arrays.asList(updatedToDoList));
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean updateToDoListName(String username, int listId, String newListName) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						//update the list
						list.setName(newListName);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean deleteList(String username, int listId) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						//update the list
						lists.remove(list);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean createList(String username, String listName) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				user.addToDoList(new ToDoList(listName));
				return true;
			}
		}
		return false;
	}
	
	public boolean createUser(String email, String username, String password) {
		//check if the user already exists
		for(User user : users) {
			if(email.equals(user.getEmail()) || username.equals(user.getUsername())){
				return false;
			}
		}
		users.add(new User(email, password, username));
		return true;
	}
	
}
