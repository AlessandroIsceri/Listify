package listify.repository;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import listify.domain.User;

public class UserRepository extends Repository{

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
            System.out.println(e);
        }
		return users;
	}

	public boolean createUser(String email, String password, String username) {
		try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO user (email, username, password) VALUES (\"" + email + "\", \"" + username + "\", \"" + password + "\")");
            closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
	}	
}