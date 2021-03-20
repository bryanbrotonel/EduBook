/**
 * 
 */
package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.UserSession;
import javafx.scene.input.MouseEvent;

/**
 * @author bryan
 *
 */

public class ShellController {

	@FXML
	BorderPane mainPane;

	@FXML
	Text settingsNav;
	@FXML
	Text dashboardNav;
	@FXML
	Text logOutBtn;

	@FXML
	void initialize() throws SQLException {

		dashboardBtnListener();
	}

	@FXML
	public void dashboardBtnListener() throws SQLException {
		DashboardController dashboard = new DashboardController();

		System.out.println("dashboardBtnListener()");

		mainPane.setCenter(dashboard);
	}

	@FXML
	public void settingsBtnListener() throws SQLException {
		System.out.println("settingsBtnListener()");

	}

	@FXML
	public void logOutBtnListener(MouseEvent event) throws SQLException, IOException {
		System.out.println("logOutBtnListener()");

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		UserSession.cleanUserSession();

		window.setScene(tableViewScene);
		window.show();

	}
	
	@FXML
	public void bookApptBtnListener() throws SQLException {
		System.out.println("bookApptBtn()");
		
		BookAppointmentController bookAppt = new BookAppointmentController();
		
		mainPane.setCenter(bookAppt);
	}
}
