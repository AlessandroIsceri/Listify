package listify.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import listify.domain.Activity;
import listify.domain.ToDoList;

public class ActivityRepository extends Repository{
	
	//get all the activities in the DB
	public ArrayList<Activity> getActivities(ToDoList list) {
		ArrayList<Activity> activities = new ArrayList<>();
		try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM activity WHERE list_id = " + list.getId());
            while (resultSet.next()) {
            	LocalDate date = null;
            	// to handle possible null pointer exceptions
            	if(resultSet.getDate("expirationDate") != null) {
            		date = resultSet.getDate("expirationDate").toLocalDate();
            	}
            	activities.add(new Activity(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("priority"), date, resultSet.getString("category")));
            }
            closeConnection();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return activities;
	}

	//create a new activity and return its id
	public int createActivity(int listId, Activity activity) {
		try {
            openConnection();
            String expDate = "NULL";
            // to handle possible null pointer exceptions
            if(activity.getExpirationDate() != null) {
            	expDate = "\"" + activity.getExpirationDate() + "\"";
            }
            String query = "INSERT INTO activity (name, priority, expirationDate, category, list_id) VALUES (\"" + activity.getName() + "\", " + activity.getPriority() + ", " + expDate + ", \"" + activity.getCategory() + "\", "+ listId + ")";
            PreparedStatement  statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int generatedId = -1;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); 
            }
            closeConnection();
            return generatedId; //return the new id (created from the DB)
        } catch (Exception e) {
        	e.printStackTrace();
            return -1; //return -1 if an error occured during the creation
        }
	}

	//delete an activity from the DB
	public boolean deleteActivity(int activityId) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM activity WHERE id = " + activityId);
            closeConnection();
            return true; //return true if the deletion was successful on the DB
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise, return false
        }
	}

	//update an activity
	public boolean updateActivity(int activityId, Activity activity) {
		try {
            openConnection();
            String expDate = "NULL";
        	// to handle possible null pointer exceptions
            if(activity.getExpirationDate() != null) {
            	expDate = "\"" + activity.getExpirationDate() + "\"";
            }
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE activity SET name = \"" + activity.getName() + "\", priority = " + activity.getPriority() + ", expirationDate = " + expDate + ", category = \"" + activity.getCategory() + "\" WHERE id = " + activityId);
            closeConnection();
            return true; //return true if the update was successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; //otherwise, return false
        }
	}

	//update the category of an activity
	public boolean updateActivityCategory(int activityId, String category) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE activity SET category = \"" + category + "\" WHERE id = " + activityId);
            closeConnection();
            return true; //return true if the update was successful
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise, return false
        }
	}
}
