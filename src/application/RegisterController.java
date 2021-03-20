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
import models.UserSession;
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
	public void registerBtnListener(ActionEvent event) throws SQLException, IOException {
		
		String firstName =  regFirstNameTextField.getText();
		String lastName = regLastNameTextField.getText();
		String email = regEmailTextField.getText();
		String password = regPasswordTextField.getText();
		
		statement = connection.createStatement();
		
		if (!passMatchCheck(password, regConfirmPasswordTextField.getText()))
			regErrorLabel.setText("Passwords don't match.");
		
		else if (!isValidEmail(email))
			regErrorLabel.setText("Invalid email");
		
		else if (checkEmaiExists(email))
			regErrorLabel.setText("Email already registered");
		
		else {
			String sqlInsert = "INSERT INTO USERS(FirstName, LastName, Email, Password, Privileges) VALUES('" + firstName + "', '" + 
					lastName + "', '" + 
					email + "', '" + 
					password + "', '" +
					0 + "')";
			
			statement.executeUpdate(sqlInsert);
						
			UserSession newSession = UserSession.getInstance(firstName, lastName, email, password);
			System.out.println(newSession.toString());
			
			redirectRegistered(event);
		}
		
		statement.close();

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
	
	public boolean isValidEmail(String email) {
		   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		   return email.matches(regex);
	}
	
	public boolean checkEmaiExists(String email) throws SQLException {
		
		int count = 0;
		
		statement = connection.createStatement();
		
		String sqlSelect = "SELECT count(*) FROM USERS WHERE EMAIL='" + regEmailTextField.getText() + "';";
		ResultSet resultSet = statement.executeQuery(sqlSelect);

		if(resultSet.next())
		    count = resultSet.getInt(1);
				
		return count >= 1;
		
	}
	
	private boolean passMatchCheck(String pass, String confirmPass) {
	    boolean isCorrect = true;
	    
	    char[] password = pass.toCharArray();
	    char[] confirmPassword = confirmPass.toCharArray();
	    
	    isCorrect = password.length != confirmPassword.length ? false : Arrays.equals (password, confirmPassword);

	    //Zero out the password.
	    Arrays.fill(password,'0');
	    Arrays.fill(confirmPassword,'0');

	    return isCorrect;
	}
	
	private void redirectRegistered(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Shell.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
}
