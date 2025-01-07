package listify.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.repositories.ActivityRepository;
import listify.repositories.ToDoListRepository;
import listify.repositories.UserRepository;

@Service
public class ListifyService {
	private List<User> users;
	private static ListifyService instance;
	//repository objects to handle requests to the DB
	private UserRepository userRepository;
	private ToDoListRepository toDoListRepository;
	private ActivityRepository activityRepository;

	//singleton
	public static ListifyService getInstance(){
		if (instance == null){
			instance = new ListifyService();
		}
		return instance;
	}
	
	private ListifyService() {
		users = new ArrayList<User>();
        userRepository = new UserRepository();
        toDoListRepository = new ToDoListRepository();
        activityRepository = new ActivityRepository();
        
        // retrieve data from DB when the class is instantiated
        users = userRepository.getUsers();
        for(User user : users) {
        	// retrieve todolist information
        	ArrayList<ToDoList> toDoLists = toDoListRepository.getToDoLists(user);
        	for(ToDoList list : toDoLists) {
        		//retrieve activity information
        		ArrayList<Activity> activities = activityRepository.getActivities(list);
        		for(Activity activity : activities) {
        			list.addActivity(activity);
        		}
        		user.addToDoList(list);
        	}
        }
	}
	
	//functions to handle users
	public boolean createUser(String email, String username, String password) {
		//check if the user already exists
		for(User user : users) {
			if(email.equals(user.getEmail()) || username.equals(user.getUsername())){
				return false;
			}
		}
		if(userRepository.createUser(email, password, username)) { //if the creation of the user was successful on the DB
			users.add(new User(email, password, username));
			return true;
		}
		return false;
	}
	
	public String login(String email, String password) {
		//no need to search in the DB -> faster here
		for(User user : users) {
			if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
				return user.getUsername();
			}
		}
		return null;
	}
	
	public User getUser(String username) {
		//no need to search in the DB -> faster here
		for(User user : users) {
			if(username.equals(user.getUsername())){
				return user;
			}
		}
		return null;
	}
	
	public boolean deleteUser(String username) {
		User user = getUser(username);
		if(user != null && userRepository.deleteUser(username)){
			users.remove(user);
			return true;
		}
		return false;
	}

	
	//functions to handle to do lists
	public int createToDoList(String username, String listName) {
		int newId = toDoListRepository.createToDoList(username, listName);
		if(newId != -1) { //-1 -> error in the DB
			User user = getUser(username);
			if(user != null) {
				//add the new list in memory
				user.addToDoList(new ToDoList(newId, listName));
				return newId;
			}
		}
		return -1;
	}
	
	public ToDoList getToDoList(String username, int listId) {
		User user = getUser(username);
		if(user != null) {
			return user.getToDoList(listId);
		}
		return null;
	}

	public boolean updateToDoListName(String username, int listId, String newListName) {
		ToDoList list = getToDoList(username, listId);
		if(list != null && toDoListRepository.updateToDoListName(listId, newListName)) { //if the list exists and the update was successful on DB
			list.setName(newListName); //update list name at runtime
			return true;
		}
		return false;
	}

	public boolean deleteToDoList(String username, int listId) {
		User user = getUser(username);
		if(user != null) {
			ToDoList list = user.getToDoList(listId);
			if(list != null && toDoListRepository.deleteToDoList(listId)) { //if the list exists and the deletion was successful on DB
				user.removeToDoList(list); //delete list at runtime
				return true;
			}
		}
		return false;
	}

	
	
	//functions to handle activities
	public int createActivity(String username, int listId, Activity activity) {
		int newId = activityRepository.createActivity(listId, activity);
		if(newId != -1) { //-1 -> error in the DB
			activity.setId(newId);
			ToDoList list = getToDoList(username, listId);
			if(list != null) {
				//add the activity to the target list
				list.addActivity(activity);
				return newId;
			}
		}
		return -1;
	}

	public Activity getActivity(String username, int listId, int activityId) {
		ToDoList list = getToDoList(username, listId);
		if(list != null) {
			return list.getActivity(activityId);
		}
		return null;
	}

	public boolean updateActivity(String username, int listId, int activityId, Activity activity) {
		Activity returnedActivity = getActivity(username, listId, activityId);
		if(returnedActivity != null && activityRepository.updateActivity(activityId, activity)) {
			//if the update of the DB was successful, update the activity at run-time
			returnedActivity.setName(activity.getName());
			returnedActivity.setCategory(activity.getCategory());
			returnedActivity.setPriority(activity.getPriority());
			returnedActivity.setExpirationDate(activity.getExpirationDate());
			return true;
		}
		return false;
	}

	public boolean updateActivityCategory(String username, int listId, int activityId, String category) {
		Activity returnedActivity = getActivity(username, listId, activityId);
		if(returnedActivity != null && activityRepository.updateActivityCategory(activityId, category)) {
			//if the update on DB was successful, update the category in memory
			returnedActivity.setCategory(category);
			return true;
		}
		return false;
	}

	public boolean deleteActivity(String username, int listId, int activityId) {
		ToDoList list = getToDoList(username, listId);
		if(list != null) {
			Activity activity = list.getActivity(activityId);
			if(activity != null && activityRepository.deleteActivity(activityId)) {
				//if the deletion was successful on the DB, delete the activity in memory
				list.removeActivity(activity);
				return true;
			}
		}
		return false;
	}
	
}
