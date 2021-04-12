package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.UserSession;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsController extends AnchorPane {
	
	UserSession currSession;
	
	@FXML private TextField settingsFirstNameTextField;
	@FXML private TextField settingsLastNameTextField;
	@FXML private TextField settingsEmailTextField;
	@FXML private TextField settingsOldPasswordTextField;
	@FXML private TextField settingsNewPasswordTextField;
	@FXML private TextField settingsConfirmPasswordTextField;
	@FXML private Button settingsSaveBtn;
	@FXML private Label settingsErrorLabel;
	
	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;
		
	public SettingsController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		currSession = UserSession.getInstance();

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void initialize()  {
		settingsFirstNameTextField.setText(currSession.getFirstName());
		settingsLastNameTextField.setText(currSession.getLastName());
		settingsEmailTextField.setText(currSession.getEmail());
		
		settingsFirstNameTextField.setEditable(false);
		settingsLastNameTextField.setEditable(false);
		settingsEmailTextField.setEditable(false);

	}
	
	@FXML public void settingsBtnListener(ActionEvent event) throws SQLException, IOException {
		
		currSession = UserSession.getInstance();
		
		String firstName =  settingsFirstNameTextField.getText();
		String lastName = settingsLastNameTextField.getText();
		String email = settingsEmailTextField.getText();
		String oldPassword = currSession.getPassword();
		String newPassword = settingsNewPasswordTextField.getText();
		String confirmPassword = settingsConfirmPasswordTextField.getText();
		
		
		if (!passMatchCheck(oldPassword, settingsOldPasswordTextField.getText()))
			settingsErrorLabel.setText("Old Password incorrect");
		else
		{
			if (!passMatchCheck(newPassword, settingsConfirmPasswordTextField.getText()))
				settingsErrorLabel.setText("Passwords don't match.");
			
			else {
				statement = connection.createStatement();
			
				String sqlUpdate = "UPDATE Users SET Password = '" + newPassword + "' WHERE EMAIL = '" + email + "';";

				statement.executeUpdate(sqlUpdate);
		
				statement.close();
			}
		}

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
}
