package listify.services;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.repository.ActivityRepository;
import listify.repository.ToDoListRepository;
import listify.repository.UserRepository;

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
        			list.addItem(activity);
        		}
        		user.addToDoList(list);
        	}
        }
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

	public ToDoList getToDoList(String username, int listId) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists(); //get the list of the target user
				for(ToDoList list : lists) {
					if(listId == list.getId()){ //found the target list
						return list;
					}
				}
			}
		}
		return null;
	}

	public boolean updateToDoListName(String username, int listId, String newListName) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						//update the list name on the DB
						if(toDoListRepository.updateToDoListName(listId, newListName)) {
							//if the update on the DB was successful, update the list in memory
							list.setName(newListName);
							return true;
						}
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
						//delete the list from the DB
						if(toDoListRepository.deleteList(listId)) {
							//if the update on the DB was successful, update the list in memory
							lists.remove(list);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int createList(String username, String listName) {
		int newId = toDoListRepository.createList(username, listName);
		if(newId != -1) { //-1 -> error in the DB
			for(User user : users) {
				if(username.equals(user.getUsername())){
					//add the new list in memory
					user.addToDoList(new ToDoList(newId, listName));
					return newId;
				}
			}
		}
		return -1;
	}
	
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

	public int createActivity(String username, int listId, Activity activity) {
		int newId = activityRepository.createActivity(listId, activity);
		if(newId != -1) { //-1 -> error in the DB
			activity.setId(newId);
			for(User user : users) {
				if(username.equals(user.getUsername())){
					List<ToDoList> lists = user.getToDoLists();
					for(ToDoList list : lists) {
						if(listId == list.getId()){
							//add the activity to the target list
							list.addItem(activity);
							return newId;
						}
					}
				}
			}
		}
		return -1;
	}

	public boolean deleteActivity(String username, int listId, int activityId) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						for(Activity activity: list.getToDoList()) {
							if(activityId == activity.getId()) {
								//delete the activity from the DB
								if(activityRepository.deleteActivity(activityId)) {
									//if the deletion was successful on the DB, delete the activity in memory
									list.removeItem(activity);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean updateActivity(String username, int listId, int activityId, Activity activity) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						for(Activity cur_activity: list.getToDoList()) {
							if(activityId == cur_activity.getId()) {
								//update the activity in the DB
								if(activityRepository.updateActivity(activityId, activity)) {
									//if the update of the DB was successful, update the activity at run-time
									cur_activity.setName(activity.getName());
									cur_activity.setCategory(activity.getCategory());
									cur_activity.setPriority(activity.getPriority());
									cur_activity.setExpirationDate(activity.getExpirationDate());
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean updateActivityCategory(String username, int listId, int activityId, String category) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						for(Activity cur_activity: list.getToDoList()) {
							if(activityId == cur_activity.getId()) {
								//update the activity on the DB
								if(activityRepository.updateActivityCategory(activityId, category)) {
									//if the update on DB was successful, update the category in memory
									cur_activity.setCategory(category);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean deleteUser(String username) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				if(userRepository.deleteUser(username)) {
					users.remove(user);
					return true;
				}
			}
		}
		return false;
	}
}
