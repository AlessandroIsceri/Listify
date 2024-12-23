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
            ResultSet resultSet = statement.executeQuery("select * from activity where list_id = " + list.getId());
            while (resultSet.next()) {
            	activities.add(new Activity(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("priority"), resultSet.getDate("expirationDate").toLocalDate(), resultSet.getString("category")));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
		return activities;
	}
}
