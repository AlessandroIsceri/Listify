package listify.services;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import listify.domain.User;
import listify.domain.Activity;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListifyServiceIntegrationTest {
	
	static int listId;
	static int activityId;
	static ListifyService listifyService;
	static Activity activity;
	
	private Activity createActivity(String name, String category, LocalDate date, int priority) {
		Activity activity = new Activity();
		activity.setName(name);
		activity.setCategory(category);
		activity.setExpirationDate(date);
		activity.setPriority(priority);
		return activity;
	}
	
	@BeforeEach
	//TODO non dovrebbe essere eseguito before getIntsanceTest
	void getListifyService() {
		listifyService = ListifyService.getInstance();
	}
	
	@Test
	@Order(1)
    void getInstanceTest() {
		ListifyService instance1 = ListifyService.getInstance();
        ListifyService instance2 = ListifyService.getInstance();

        assertNotNull(instance1); //the first instance should not be null
        assertSame(instance1, instance2); //Both instances should be the same
    }

	@Test 
	@Order(2)
	void createUserTest() {
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
	void loginTest() {
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
	void getUserTest() {
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setPassword("password");
		user.setUsername("test_user");
		
		//get the user from the username
		User returnedUser = listifyService.getUser(user.getUsername());
		assertNotNull(returnedUser); //previously created user, should not be null
		
		//the returned user must be identical to the created one
		assertEquals(user, returnedUser);
		
		//should return null if the user does not exists
		assertNull(listifyService.getUser("not_existing_user"));
	}
	
	@Test
	@Order(5)
	void createToDoListTest() {
		ListifyService listifyService = ListifyService.getInstance();
		//try to create a list
		listId = listifyService.createToDoList("test_user", "first_list");
		assertNotSame(-1, listId); //id should not be -1, unless the DB is not working correctly
		assertNotNull(listifyService.getToDoList("test_user", listId)); //the list should be created and accessible
		assertNull(listifyService.getToDoList("test_user", listId + 1)); //this list should not exist		
	}

	@Test
	@Order(6)
	void updateToDoListNameTest() {
		//try to update list name
		assertTrue(listifyService.updateToDoListName("test_user", listId, "first_list_1"));
		//the name should be updated correctly
		assertSame("first_list_1", listifyService.getToDoList("test_user", listId).getName());
		
		//you cannot update the name of a not existing list
		assertFalse(listifyService.updateToDoListName("test_user", listId + 1, "first_list_1"));
	}
	
	@Test
	@Order(7)
	void createActivityTest() {
		//try to create an activity and add it to the previously created list
		activity = createActivity("first_activity", "To Do", LocalDate.now(), 3);
		
		activityId = listifyService.createActivity("test_user", listId, activity);
		assertNotSame(-1, activityId); //the activity should have an id different from -1, unless there was an error in the DB
		
		//check if the activity was correctly inserted into the "first_list_1" to-do list
		assertSame("first_list_1", listifyService.getToDoList("test_user", listId).getName());
		Activity returnedActivity = listifyService.getToDoList("test_user", listId).getActivity(activityId);
		assertEquals(activity, returnedActivity);
	}
	
	@Test
	@Order(8)
	void updateActivityTest() {
		//try to update an activity
		activity.setPriority(6);
		activity.setExpirationDate(null);
		listifyService.updateActivity("test_user", listId, activityId, activity);

		//check if the activity was updated correctly
		Activity returnedActivity = listifyService.getActivity("test_user", listId, activityId);
		assertEquals(activity, returnedActivity);
		
		//you cannot update a not existing activity
		assertFalse(listifyService.updateActivity("test_user", listId, activityId + 1, activity));
		assertFalse(listifyService.updateActivity("test_user", listId + 1, activityId, activity));
		assertFalse(listifyService.updateActivity("test_user2", listId, activityId, activity));
	}
	
	@Test
	@Order(9)
	void updateActivityCategoryTest() {
		//try to update the activity category
		listifyService.updateActivityCategory("test_user", listId, activityId, "Completed");
		Activity returnedActivity = listifyService.getActivity("test_user", listId, activityId);
		assertEquals("Completed", returnedActivity.getCategory());
		
		//you cannot update a not existing activity
		assertFalse(listifyService.updateActivityCategory("test_user", listId, activityId + 1, "Completed"));
		assertFalse(listifyService.updateActivityCategory("test_user", listId + 1, activityId, "Completed"));
		assertFalse(listifyService.updateActivityCategory("test_user2", listId, activityId, "Completed"));
	}
	
	@Test
	@Order(10)
	void deleteActivityTest() {
		//try to delete an activity
		assertTrue(listifyService.deleteActivity("test_user", listId, activityId));
		
		//you cannot delete an activity twice
		assertFalse(listifyService.deleteActivity("test_user", listId, activityId));
		
		//the todolist now should be empty
		List<Activity> toDoList = listifyService.getToDoList("test_user", listId).getToDoList();
		assertEquals(0, toDoList.size());
	}
	
	@Test
	@Order(11)
	void deleteToDoListTest() {
		//try to delete the todolist
		assertTrue(listifyService.deleteToDoList("test_user", listId));
		assertEquals(0, listifyService.getUser("test_user").getToDoLists().size());
		
		//you cannot delete a todolist twice
		assertFalse(listifyService.deleteToDoList("test_user", listId));
		
		//now the user should not have any todolist
		assertEquals(0, listifyService.getUser("test_user").getToDoLists().size());
	}
	
	@Test
	@Order(12)
	void listManipulationTest() {
		//try to create a list
		int listId = listifyService.createToDoList("test_user", "second_list");
		
		//create two activities
		Activity first_activity = createActivity("first_activity", "To Do", LocalDate.now(), 3);
		
		Activity second_activity = createActivity("second_activity", "Completed", null, 6);
		
		//add the activities to the todolist
		int firstId = listifyService.createActivity("test_user", listId, first_activity);
		int secondId = listifyService.createActivity("test_user", listId, second_activity);
		
		//the list length should be 2 now
		List<Activity> toDoList = listifyService.getToDoList("test_user", listId).getToDoList();
		assertEquals(2, toDoList.size());
		
		//delete the to do list
		listifyService.deleteToDoList("test_user", listId);
		
		//now the activities should not be accessible now
		assertNull(listifyService.getActivity("test_user", listId, firstId));
		assertNull(listifyService.getActivity("test_user", listId, secondId));
	}
	
	@Test
	@Order(13)
	void userDeleteTest() {
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