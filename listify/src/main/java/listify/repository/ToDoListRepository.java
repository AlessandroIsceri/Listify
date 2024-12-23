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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM todolist WHERE username = \"" + user.getUsername() + "\"");
            while (resultSet.next()) {
            	toDoLists.add(new ToDoList(resultSet.getInt("id"), resultSet.getString("name")));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
		return toDoLists;
	}

	public boolean updateToDoListName(int listId, String newListName) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE todolist SET name = \"" + newListName + "\" WHERE id = " + listId);
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}

	public boolean deleteList(int listId) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM todolist WHERE id = " + listId);
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
		
	}

	public boolean createList(String username, String listName) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO todolist (name, username) VALUES (\"" + listName + "\", \"" + username + "\")");
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}
}
