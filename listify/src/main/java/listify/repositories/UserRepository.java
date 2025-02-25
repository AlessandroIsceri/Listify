package listify.repositories;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.User;

public class UserRepository extends Repository{

	//get all the users from the DB
	public ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
            	users.add(new User(resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("username")));
            }
            closeConnection();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return users;
	}

	//create a new user
	public boolean createUser(String email, String password, String username) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO user (email, username, password) VALUES (\"" + email + "\", \"" + username + "\", \"" + password + "\")");
            closeConnection();
            return true; //if the creation was successful return true
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise return false
        }
	}

	public boolean deleteUser(String username) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM user WHERE username = \"" + username + "\"");
            closeConnection();
            return true; //if the deletion was successful return true
        } catch (Exception e) {
        	e.printStackTrace();
            return false; //otherwise return false
        }
	}	
}