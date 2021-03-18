package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

public class RegisterController {
	@FXML
	private TextField regFirstNameTextField;
	@FXML
	private TextField regLastNameTextField;
	@FXML
	private TextField regPasswordTextField;
	@FXML
	private TextField regConfirmPasswordTextField;
	@FXML
	private TextField regEmailTextField;
	@FXML
	private Button registerUserBtn;
	@FXML
	private Label logInLink;
	@FXML
	private Label regErrorLabel;

	
	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;

	// Event Listener on Button[#registerUserBtn].onAction
	@FXML
	public void registerBtnListener(ActionEvent event) throws SQLException {
		
		statement = connection.createStatement();
		
		if (passMatchCheck()) {
			String sqlInsert = "INSERT INTO USERS VALUES('" + regFirstNameTextField.getText() + "', '" + 
					regLastNameTextField.getText() + "', '" + 
					regEmailTextField.getText() + "', '" + 
					regPasswordTextField.getText() + "')";
			
			statement.executeUpdate(sqlInsert);
		}
		else {
			regErrorLabel.setText("Passwords don't match.");
		}
		
	}
	
	// Event Listener on Label[#logInLink].onMouseClicked
	@FXML
	public void logInLinkListener(MouseEvent event) throws IOException {

        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
	private boolean passMatchCheck() {
	    boolean isCorrect = true;
	    
	    char[] password = regPasswordTextField.getText().toCharArray();
	    char[] confirmPassword = regConfirmPasswordTextField.getText().toCharArray();
	    
	    isCorrect = password.length != confirmPassword.length ? false : Arrays.equals (password, confirmPassword);

	    //Zero out the password.
	    Arrays.fill(password,'0');
	    Arrays.fill(confirmPassword,'0');

	    return isCorrect;
	}
	
}
