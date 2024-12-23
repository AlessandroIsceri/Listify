package listify.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Repository {
	private String url = "jdbc:mysql://localhost:3306/listify";
	private String user = "root";
	protected Connection connection = null;
	
	protected void openConnection() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, "");
        System.out.println("CONNECTED CORRECTLY");
	}
	
	protected void closeConnection() throws Exception{
		connection.close();
	}
	
}
