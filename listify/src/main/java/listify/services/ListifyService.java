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

public class ListifyService {
	List<User> users;
	private static ListifyService instance;
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
        
        // retrieve data from DB
        users = userRepository.getUsers();
        for(User user : users) {
        	//retrieve todolist information
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
		for(User user : users) {
			if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
				return user.getUsername();
			}
		}
		return null;
	}
	
	public User getUser(String username) {
		//no need to search in the DB -> faster
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

	public boolean updateToDoListName(String username, int listId, String newListName) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						//update the list name on the DB
						if(toDoListRepository.updateToDoListName(listId, newListName)) {
							//update the list in memory
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
						//delete the list from the db
						if(toDoListRepository.deleteList(listId)) {
							//update the list in memory
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
		if(newId != -1) { //-1 -> error in the db
			for(User user : users) {
				if(username.equals(user.getUsername())){
					user.addToDoList(new ToDoList(newId, listName));
				}
			}
		}
		return newId;
	}
	
	public boolean createUser(String email, String username, String password) {
		//check if the user already exists
		for(User user : users) {
			if(email.equals(user.getEmail()) || username.equals(user.getUsername())){
				return false;
			}
		}
		if(userRepository.createUser(email, password, username)) {
			users.add(new User(email, password, username));
			return true;
		}
		return false;
	}

	public int createActivity(String username, int listId, Activity activity) {
		int newId = activityRepository.createActivity(listId, activity);
		if(newId != -1) { //-1 -> error in the db
			activity.setId(newId);
			for(User user : users) {
				if(username.equals(user.getUsername())){
					List<ToDoList> lists = user.getToDoLists();
					for(ToDoList list : lists) {
						if(listId == list.getId()){
							list.addItem(activity);
						}
					}
				}
			}
		}
		return newId;
	}

	public boolean deleteActivity(String username, int listId, int activityId) {
		for(User user : users) {
			if(username.equals(user.getUsername())){
				List<ToDoList> lists = user.getToDoLists();
				for(ToDoList list : lists) {
					if(listId == list.getId()){
						for(Activity activity: list.getToDoList()) {
							if(activityId == activity.getId()) {
								//delete the activity from the db
								if(activityRepository.deleteActivity(activityId)) {
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
								//delete the activity from the db
								if(activityRepository.updateActivity(activityId, activity)) {
									cur_activity = activity;
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
								//delete the activity from the db
								if(activityRepository.updateActivityCategory(activityId, category)) {
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
	
}
