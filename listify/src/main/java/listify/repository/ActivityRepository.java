package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

	/*public void updateActivities(int listId, Activity[] updatedToDoList) {
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
		
	}*/

	public int createActivity(int listId, Activity activity) {
		try {
            openConnection();
            String query = "INSERT INTO activity (name, priority, expirationDate, category, list_id) VALUES (\"" + activity.getName() + "\", " + activity.getPriority() + ", \"" + activity.getExpirationDate() + "\", \"" + activity.getCategory() + "\", "+ listId + ")";
            PreparedStatement  statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int generatedId = -1;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); 
            }
            closeConnection();
            return generatedId;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
	}

	public boolean deleteActivity(int activityId) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            System.out.println("DELETE FROM activity WHERE id = " + activityId);
            statement.executeUpdate("DELETE FROM activity WHERE id = " + activityId);
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}

	public boolean updateActivity(int activityId, Activity activity) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            System.out.println("UPDATE activity SET name = \"" + activity.getName() + "\", priority = " + activity.getPriority() + ", expirationDate = \"" + activity.getExpirationDate() + "\"" + ", category = \"" + activity.getCategory() + "\" WHERE id = " + activityId);
            statement.executeUpdate("UPDATE activity SET name = \"" + activity.getName() + "\", priority = " + activity.getPriority() + ", expirationDate = \"" + activity.getExpirationDate() + "\"" + ", category = \"" + activity.getCategory() + "\" WHERE id = " + activityId);
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}

	public boolean updateActivityCategory(int activityId, String category) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            System.out.println("UPDATE activity SET category = \"" + category + "\" WHERE id = " + activityId);
            statement.executeUpdate("UPDATE activity SET category = \"" + category + "\" WHERE id = " + activityId);
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}
}
