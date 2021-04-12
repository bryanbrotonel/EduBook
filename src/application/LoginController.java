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
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UserSession;

public class LoginController {

	@FXML
	TextField emailTextField;
	@FXML
	PasswordField passwordTextField;
	@FXML
	Button selectBtn;
	@FXML
	Button insertBtn;
	@FXML
	Button registerBtn;
	@FXML
	Label textLabel;

	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;

	@FXML
	public void loginBtnListener(ActionEvent event) throws SQLException, IOException {

		statement = connection.createStatement();

		String sqlSelect = "SELECT * FROM USERS WHERE EMAIL='" + emailTextField.getText() + "';";

		ResultSet resultSet = statement.executeQuery(sqlSelect);

		if (resultSet.next() == false) {
			emailTextField.setStyle("-fx-border-color: #D64045; -fx-focus-color: #D64045;");
			if (passwordTextField.getText() != "")
				passwordTextField.setStyle("-fx-border-color: #D64045; -fx-focus-color: #D64045;");
		} else {
			do {
				if (!passwordTextField.getText().equals(resultSet.getObject("Password"))) {
					emailTextField.setStyle("-fx-border-color: #CED4DA; -fx-focus-color: #CED4DA;");
					passwordTextField.setStyle("-fx-border-color: #D64045; -fx-focus-color: #D64045;");
				} else {
					UserSession.getInstance(emailTextField.getText());
					redirectLogIn(event);
				}
			} while (resultSet.next());
		}

	}

	@FXML
	public void registerBtnListener(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("Register.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}

	public void redirectLogIn(ActionEvent event) throws IOException, SQLException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("Shell.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}
}