package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.ToDoList;
import listify.domain.User;

public class ToDoListRepository extends Repository{
	
	public ArrayList<ToDoList> getToDoLists(User user) {
		ArrayList<ToDoList> toDoLists = new ArrayList<>();
		try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from todolist where user_email = \"" + user.getEmail() + "\"");
            while (resultSet.next()) {
            	toDoLists.add(new ToDoList(resultSet.getInt("id"), resultSet.getString("name")));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
		return toDoLists;
	}
	
}
