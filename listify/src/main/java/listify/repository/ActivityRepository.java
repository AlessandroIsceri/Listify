package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.Activity;
import listify.domain.ToDoList;
import listify.domain.User;

public class ActivityRepository extends Repository{
	
	public ArrayList<Activity> getActivities(ToDoList list) {
		ArrayList<Activity> activities = new ArrayList<>();
		try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM activity WHERE list_id = " + list.getId());
            while (resultSet.next()) {
            	activities.add(new Activity(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("priority"), resultSet.getDate("expirationDate").toLocalDate(), resultSet.getString("category")));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
		return activities;
	}

	public void updateActivities(int listId, Activity[] updatedToDoList) {
		try {
			for(int i = 0; i < updatedToDoList.length; i++) {
				Activity activity = updatedToDoList[i];
				openConnection();
	            Statement statement = connection.createStatement();
				//all the activities already exists: just update them
	            statement.executeUpdate("UPDATE activity SET name = \"" + activity.getName() + "\", priority = " + activity.getPriority() + ", expirationDate = \"" + activity.getExpirationDate() + "\"" + ", category = \"" + activity.getCategory() + "\" WHERE id = " + activity.getId());
	            closeConnection();
			}
		} catch (Exception e) {
            System.out.println(e);
        }
		
	}
}
