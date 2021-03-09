package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
	
	@FXML TextField usernameTextField;
	@FXML TextField passwordTextField;
	@FXML Button selectBtn;
	@FXML Button insertBtn;
	@FXML Label textLabel;
	
	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;
	
	@FXML public void insertBtnListener() throws SQLException {
		
		statement = connection.createStatement();
		String sqlInsert = "INSERT INTO USERS VALUES('" + 001 + "', '" + usernameTextField.getText()+ "', '" + passwordTextField.getText() + "')";
		statement.executeUpdate(sqlInsert);
		
	}

	@FXML public void selectBtnListener() throws SQLException {
		
		statement = connection.createStatement();
		
		String sqlSelect = "SELECT USERNAME, PASSWORD FROM USERS;";
		ResultSet resultSet = statement.executeQuery(sqlSelect);
		
		while (resultSet.next()) {
			textLabel.setText(resultSet.getNString("USERNAME") + ": " + resultSet.getNString("PASSWORD"));
		}
		
	}
}