package connectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class UserSession {

	private static UserSession instance;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;

	private UserSession(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public static UserSession getInstance() {
		return instance;
	}

	public static UserSession getInstance(String email) throws SQLException {
		if (instance == null) {
			String[] data = getUserData(email);
			instance = new UserSession(data[0], data[1], email, data[3]);
		}
		return instance;
	}
	
	public static String[] getUserData(String email) throws SQLException {
		String[] userData = new String[5];
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement;
		
		statement = connection.createStatement();
		
		String sqlSelect = "SELECT * FROM USERS WHERE EMAIL='" + email + "';";

		ResultSet resultSet = statement.executeQuery(sqlSelect);
		
		while (resultSet.next()) {
			userData[0] = resultSet.getString("FIRSTNAME");
			userData[1] = resultSet.getString("LASTNAME");
			userData[2] = email;
			userData[3] = resultSet.getString("PASSWORD");
		}
		return userData;
		
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getemail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void cleanUserSession() {
		firstName = "";// or null
		lastName = "";// or null
		email = "";// or null
		password = "";// or null
	}

	@Override
	public String toString() {
		return "UserSession{" + "firstName='" + firstName + '\'' + "lastName='" + lastName + '\'' + "email='" + email + '\'' + ", password=" + password + '}';
	}
}