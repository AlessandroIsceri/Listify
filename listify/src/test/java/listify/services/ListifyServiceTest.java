package listify.services;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import listify.domain.User;
import listify.domain.Activity;
import listify.domain.ToDoList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListifyServiceTest {
	
	@Test
	@Order(1)
    public void testGetInstance() {
		ListifyService instance1 = ListifyService.getInstance();
        ListifyService instance2 = ListifyService.getInstance();

        assertNotNull("The first instance should not be null", instance1);
        assertSame("Both instances should be the same", instance1, instance2);
    }

	@Test 
	@Order(2)
	public void testCreateUser() {
		ListifyService listifyService = ListifyService.getInstance();
		
		//the service should permit to create a new user
		assertTrue(listifyService.createUser("test@gmail.com", "test_user", "password"));
		
		//cannot re-create the same user (same email or username)
		assertFalse(listifyService.createUser("test@gmail.com", "test_user1", "password"));
		assertFalse(listifyService.createUser("test1@gmail.com", "test_user", "password"));
		assertFalse(listifyService.createUser("test@gmail.com", "test_user", "password"));
		
		//but can create a similar user (same password but different email and username)
		assertTrue(listifyService.createUser("test1@gmail.com", "test_user1", "password"));
	}
	
	
	@Test 
	@Order(3)
	public void testLogin() {
		ListifyService listifyService = ListifyService.getInstance();
		
		//try to login with previously created users
		assertSame("test_user", listifyService.login("test@gmail.com", "password"));
		assertSame("test_user1", listifyService.login("test1@gmail.com", "password"));

		//the service must return the right username
		assertNotSame("test_user1", listifyService.login("test@gmail.com", "password"));
		assertNotSame("test_user", listifyService.login("test1@gmail.com", "password"));
		
		//the service must return null if the user does not exists
		assertNull(listifyService.login("test2@gmail.com", "password"));
	}
	
	@Test
	@Order(4)
	public void testGetUser() {
		ListifyService listifyService = ListifyService.getInstance();
		
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setPassword("password");
		user.setUsername("test_user");
		user.setToDoLists(new ArrayList<ToDoList>());
		
		//get the user from the username
		User returnedUser = listifyService.getUser(user.getUsername());
		assertNotNull(returnedUser); //previously created user, should not be null
		
		//the returned user must be identical to the created one
		assertEquals(user, returnedUser);
	}
	
	@Test
	@Order(5)
	public void testListManipulation() {
		ListifyService listifyService = ListifyService.getInstance();
		//try to create a list
		int listId = listifyService.createList("test_user", "first_list");
		assertNotSame(-1, listId); //id should not be -1, unless the DB is not working correctly
		assertNotNull(listifyService.getToDoList("test_user", listId)); //the list should be created and accessible
		assertNull(listifyService.getToDoList("test_user", listId + 1)); //this list should not exist
		
		//try to update list name
		assertTrue(listifyService.updateToDoListName("test_user", listId, "first_list_1"));
		//the name should be updated correctly
		assertSame("first_list_1", listifyService.getToDoList("test_user", listId).getName());
	
		//try to create an activity and add it to the previosly created list
		Activity activity = new Activity();
		activity.setName("first_activity");
		activity.setCategory("To Do");
		activity.setExpirationDate(LocalDate.now());
		activity.setPriority(3);
		int activityId = listifyService.createActivity("test_user", listId, activity);
		assertNotSame(-1, activityId); //the activity should have an id different from -1, unless there was an error in the DB
		
		//check if the activity was correctly inserted into the "first_list_1" to-do list
		assertSame("first_list_1", listifyService.getToDoList("test_user", listId).getName());
		Activity returnedActivity = listifyService.getToDoList("test_user", listId).getToDoList().get(0); //the created activity should be in position 0
		assertEquals(activity, returnedActivity);
		
		//try to update an activity
		activity.setPriority(6);
		activity.setExpirationDate(null);
		
		//check if the activity was updated correctly
		listifyService.updateActivity("test_user", listId, activityId, activity);
		returnedActivity = listifyService.getToDoList("test_user", listId).getToDoList().get(0); //the created activity should be in position 0
		assertEquals(activity, returnedActivity);
		
		//try to update the activity category
		listifyService.updateActivityCategory("test_user", listId, activityId, "Completed");
		returnedActivity = listifyService.getToDoList("test_user", listId).getToDoList().get(0); //the created activity should be in position 0
		assertEquals("Completed", returnedActivity.getCategory());
		
		//try to delete an activity
		listifyService.deleteActivity("test_user", listId, activityId);
		
		//the todolist now should be empty
		List<Activity> toDoList = listifyService.getToDoList("test_user", listId).getToDoList();
		assertEquals(0, toDoList.size());
		
		//try to delete the todolist
		listifyService.deleteList("test_user", listId);
		assertEquals(0, listifyService.getUser("test_user").getToDoLists().size());
		
	}

	@Test
	@Order(11)
	public void testUserDelete() {
		ListifyService listifyService = ListifyService.getInstance();
		//remove previously created users
		assertTrue(listifyService.deleteUser("test_user"));
		assertTrue(listifyService.deleteUser("test_user1"));

		//cannot delete two times the same user
		assertFalse(listifyService.deleteUser("test_user"));
		assertFalse(listifyService.deleteUser("test_user"));

		//cannot login with a deleted account
		assertNull(listifyService.login("test@gmail.com", "password"));
		assertNull(listifyService.login("test1@gmail.com", "password"));
	}
}