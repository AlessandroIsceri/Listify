package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.ToDoList;
import listify.domain.User;

public class ToDoListRepository {
	private String url = "jdbc:mysql://localhost:3306/listify";
	private String user = "root";
	private Connection connection = null;
	
	private void openConnection() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, "");
        System.out.println("CONNECTED CORRECTLY");
	}
	
	private void closeConnection() throws Exception{
		connection.close();
	}
	
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
