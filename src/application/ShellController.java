/**
 * 
 */
package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.UserSession;
import javafx.scene.input.MouseEvent;

/**
 * @author bryan
 *
 */

public class ShellController extends AnchorPane {

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
		DashboardController dashboardController = new DashboardController();

		System.out.println("dashboardBtnListener()");
		
		dashboardController.setShellBorderPane(mainPane);
		dashboardController.bookApptBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					bookApptBtnListener();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});

		mainPane.setCenter(dashboardController);
	}

	@FXML
	public void settingsBtnListener() throws SQLException {
		SettingsController settingsController = new SettingsController();
		
		System.out.println("settingsBtnListener()");
		
		mainPane.setCenter(settingsController);

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
		
		bookAppt.setShellBorderPane(mainPane);

		mainPane.setCenter(bookAppt);
	}
}
