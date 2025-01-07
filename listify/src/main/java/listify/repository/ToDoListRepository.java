package listify.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.ToDoList;
import listify.domain.User;

public class ToDoListRepository extends Repository{
	
	//get all the todolists from the DB
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
        	e.printStackTrace();
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
            return true; //return true if the update was successful
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise, return false
        }
	}

	//delete an existing list
	public boolean deleteToDoList(int listId) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM todolist WHERE id = " + listId);
            closeConnection();
            return true; //return true if the deletion was successful
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise, return false
        }
		
	}

	//create a new list and return its id
	public int createToDoList(String username, String listName) {
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
            return generatedId; //return the new id, generated from the DB
        } catch (Exception e) {
        	e.printStackTrace();
            return -1; //return -1 if an error occured during the creation
        }
	}
}
