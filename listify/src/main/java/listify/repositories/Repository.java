package listify.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {
	private String url = "jdbc:mysql://localhost:3306/listify"; //DB address
	private String user = "root"; //DB user
	protected Connection connection = null;
	
	protected void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, "");
	}
	
	protected void closeConnection() throws SQLException{
		connection.close();
	}
	
}
