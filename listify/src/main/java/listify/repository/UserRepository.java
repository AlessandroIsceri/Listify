package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import listify.domain.User;

public class UserRepository {
	
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
	
	public UserRepository() {
		
        try {
            openConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("email"));
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

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
