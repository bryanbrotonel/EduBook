package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML TextField emailTextField;
	@FXML TextField passwordTextField;
	@FXML Button selectBtn;
	@FXML Button insertBtn;
	@FXML Label textLabel;
	@FXML Button registerBtn;
	
	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;
	
	@FXML public void insertBtnListener() throws SQLException {
		
		statement = connection.createStatement();
		String sqlInsert = "INSERT INTO USERS VALUES('" + 001 + "', '" + emailTextField.getText()+ "', '" + passwordTextField.getText() + "')";
		statement.executeUpdate(sqlInsert);
		
	}

	@FXML public void selectBtnListener() throws SQLException {
		
		statement = connection.createStatement();
		
		String sqlSelect = "SELECT * FROM USERS WHERE EMAIL='" + emailTextField.getText() + "';";

		ResultSet resultSet = statement.executeQuery(sqlSelect);
		
		while (resultSet.next())
			textLabel.setText(resultSet.getNString("FIRSTNAME") + ", " + resultSet.getNString("LASTNAME") + ", " + resultSet.getNString("EMAIL") + ", " + resultSet.getNString("PASSWORD"));
		
	}

	@FXML public void registerBtnListener(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
}