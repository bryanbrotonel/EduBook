package application;

import java.io.Console;
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

	@FXML Label textLabel;
	@FXML TextField textField;
	@FXML Button button;

	@FXML public void buttonListener() throws SQLException {
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();

		String sqlInsert = "INSERT INTO USERS VALUES('" + 001 + "', '" + textField.getText() + "', 'Password123')";
		statement.executeUpdate(sqlInsert);
		
		String sqlSelect = "SELECT * FROM USERS;";
		ResultSet resultSet = statement.executeQuery(sqlSelect);
		while (resultSet.next()) {
			textLabel.setText(resultSet.getString(1));
		}
		
		
	}

}