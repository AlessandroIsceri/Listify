package listify.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import listify.controllers.APIController;
import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;
import listify.repositories.ActivityRepository;
import listify.repositories.ToDoListRepository;
import listify.repositories.UserRepository;

@ExtendWith(MockitoExtension.class) 
class ListifyServiceTest {

	@InjectMocks
    private ListifyService listifyService;
	
	@Mock
	UserRepository userRepostitory;
	
	@Mock
	ToDoListRepository toDoListRepository;
	
	@Mock
	ActivityRepository activityRepository;
	
	static User user;
	static ToDoList toDoList;
	static Activity activity;
	
	@BeforeEach
	void populateService() {
		//create some test data to test the methods
		when(userRepostitory.createUser(anyString(), anyString(), anyString())).thenReturn(true);
		user = new User("test@gmail.com", "password", "test_user");
		listifyService.createUser(user.getEmail(), user.getUsername(), user.getPassword());
	
		when(toDoListRepository.createToDoList(anyString(), anyString())).thenReturn(11);
		toDoList = new ToDoList(11, "first_list");
		listifyService.createToDoList(user.getUsername(), toDoList.getName());
		user.addToDoList(toDoList);
		
		when(activityRepository.createActivity(anyInt(), any(Activity.class))).thenReturn(20);
		activity = new Activity(20, "first_activity", 4, null, "Completed");
		listifyService.createActivity(user.getUsername(), toDoList.getId(), activity);
		toDoList.addActivity(activity);
	}
	
	@Test //test create user and get user
	void createUserTest() {
		//simulate a successful creation of the user
		when(userRepostitory.createUser(anyString(), anyString(), anyString())).thenReturn(true);
		
		User newUser = new User("test1@gmail.com", "password", "test1_user");
		
		assertTrue(listifyService.createUser(newUser.getEmail(), newUser.getUsername(), newUser.getPassword()));
		//the user must be created 
		assertEquals(newUser, listifyService.getUser("test1_user"));
		
		//simulate a unsuccessful creation of the user
		newUser = new User("test2@gmail.com", "password", "test2_user");
		when(userRepostitory.createUser(anyString(), anyString(), anyString())).thenReturn(false);
		assertFalse(listifyService.createUser(newUser.getEmail(), newUser.getUsername(), newUser.getPassword()));
		assertNull(listifyService.getUser("test2_user"));
	}

	@Test
	void loginTest() {		
		//successful login
		assertEquals(user.getUsername(), listifyService.login(user.getEmail(), user.getPassword()));
		
		//unsuccessful login (wrong password)
		assertNotEquals(user.getUsername(), listifyService.login(user.getEmail(), "pasword"));
	}
	
	@Test
	void deleteUserTest() {
		//unsuccessful deletion
		when(userRepostitory.deleteUser(anyString())).thenReturn(false);
		assertFalse(listifyService.deleteUser(user.getUsername()));
		assertNotNull(listifyService.getUser(user.getUsername()));
		assertEquals(user, listifyService.getUser(user.getUsername()));
		
		//successful deletion
		when(userRepostitory.deleteUser(anyString())).thenReturn(true);
		assertTrue(listifyService.deleteUser(user.getUsername()));
		assertNull(listifyService.getUser(user.getUsername()));
	}
	
	@Test 
	void createToDoListTest() {
		ToDoList newToDoList = new ToDoList();
		newToDoList.setName("second_list");
		//successful creation
		when(toDoListRepository.createToDoList(anyString(), anyString())).thenReturn(10);
		assertEquals(10, listifyService.createToDoList(user.getUsername(), newToDoList.getName()));
	
		newToDoList.setId(10);
		
		//the list must be created
		assertEquals(newToDoList, listifyService.getToDoList(user.getUsername(), 10));
	
		//unsuccessful creation
		when(toDoListRepository.createToDoList(anyString(), anyString())).thenReturn(-1);
		assertEquals(-1, listifyService.createToDoList(user.getUsername(), newToDoList.getName()));
	}
	
	@Test
	void updateToDoListNameTest() {
		//successful update
		when(toDoListRepository.updateToDoListName(anyInt(), anyString())).thenReturn(true);
		assertTrue(listifyService.updateToDoListName(user.getUsername(), toDoList.getId(), "First_list"));
		//the name must be updated
		assertEquals("First_list", listifyService.getToDoList(user.getUsername(), toDoList.getId()).getName());

		//unsuccessful update
		when(toDoListRepository.updateToDoListName(anyInt(), anyString())).thenReturn(false);
		assertFalse(listifyService.updateToDoListName(user.getUsername(), toDoList.getId(), "FIRST_LIST"));
		//the name must not be updated
		assertEquals("First_list", listifyService.getToDoList(user.getUsername(), toDoList.getId()).getName());
	}
	
	@Test 
	void deleteToDoListTest(){
		//unsuccessful deletion
		when(toDoListRepository.deleteToDoList(anyInt())).thenReturn(false);
		assertFalse(listifyService.deleteToDoList(user.getUsername(), toDoList.getId()));
		assertNotNull(listifyService.getToDoList(user.getUsername(), toDoList.getId()));
		assertEquals(toDoList, listifyService.getToDoList(user.getUsername(), toDoList.getId()));
		
		//successful deletion
		when(toDoListRepository.deleteToDoList(anyInt())).thenReturn(true);
		assertTrue(listifyService.deleteToDoList(user.getUsername(), toDoList.getId()));
		assertNull(listifyService.getToDoList(user.getUsername(), toDoList.getId()));
	}
	
	@Test 
	void createActivityTest(){
		Activity newActivity = new Activity(21, "second_activity", 7, LocalDate.now(), "In Progress");
		
		//successful creation
		when(activityRepository.createActivity(anyInt(), any(Activity.class))).thenReturn(21);
		assertEquals(21, listifyService.createActivity(user.getUsername(), toDoList.getId(), newActivity));
		
		//the activity must be created
		assertEquals(newActivity, listifyService.getActivity(user.getUsername(), 11, 21));
	
		//unsuccessful creation
		when(activityRepository.createActivity(anyInt(), any(Activity.class))).thenReturn(-1);
		assertEquals(-1, listifyService.createActivity(user.getUsername(), toDoList.getId(), newActivity));
	}
	
	@Test
	void updateActivityTest() {
		//unsuccessful update
		when(activityRepository.updateActivity(anyInt(), any(Activity.class))).thenReturn(false);
		Activity newActivity = new Activity(activity.getId(), "second_activity", 7, LocalDate.now(), "In Progress");
		assertFalse(listifyService.updateActivity(user.getUsername(), toDoList.getId(), activity.getId(), newActivity));
		assertNotEquals(newActivity, listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));		
		assertEquals(activity, listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));
		
		//successful update
		when(activityRepository.updateActivity(anyInt(), any(Activity.class))).thenReturn(true);
		assertTrue(listifyService.updateActivity(user.getUsername(), toDoList.getId(), activity.getId(), newActivity));
		assertEquals(newActivity, listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));		
	}
	
	@Test
	void updateActivityCategoryTest() {
		//unsuccessful update
		when(activityRepository.updateActivityCategory(anyInt(), anyString())).thenReturn(false);
		assertFalse(listifyService.updateActivityCategory(user.getUsername(), toDoList.getId(), activity.getId(), "In Progress"));
		assertNotEquals("In Progress", listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()).getCategory());		
		assertEquals("Completed", listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()).getCategory());		
		
		//successful update
		when(activityRepository.updateActivityCategory(anyInt(), anyString())).thenReturn(true);
		assertTrue(listifyService.updateActivityCategory(user.getUsername(), toDoList.getId(), activity.getId(), "In Progress"));
		assertNotEquals("Completed", listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()).getCategory());		
		assertEquals("In Progress", listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()).getCategory());		
	}

	@Test
	void deleteActivityTest() {
		//unsuccessful deletion
		when(activityRepository.deleteActivity(anyInt())).thenReturn(false);
		assertFalse(listifyService.deleteActivity(user.getUsername(), toDoList.getId(), activity.getId()));
		assertNotNull(listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));
		assertEquals(activity, listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));
		
		//successful deletion
		when(activityRepository.deleteActivity(anyInt())).thenReturn(true);
		assertTrue(listifyService.deleteActivity(user.getUsername(), toDoList.getId(), activity.getId()));
		assertNull(listifyService.getActivity(user.getUsername(), toDoList.getId(), activity.getId()));
	}
	
}
