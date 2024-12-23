package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import listify.domain.User;

public class UserRepository extends Repository{

	public ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user");
            while (resultSet.next()) {
            	users.add(new User(resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("username")));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
		return users;
	}	
}