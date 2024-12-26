package listify.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.ToDoList;
import listify.domain.User;

public class ToDoListRepository extends Repository{
	
	//get all the todolists
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

	//update the name of an existing to do list
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

	//delete an existing list
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

	//create a new list and return its id
	public int createList(String username, String listName) {
		try {
            openConnection();
            String query = "INSERT INTO todolist (name, username) VALUES (\"" + listName + "\", \"" + username + "\")";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int generatedId = -1;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); 
            }
            closeConnection();
            System.out.println(generatedId);
            return generatedId;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
	}
}
