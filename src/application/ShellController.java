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
		
		dashboardController.setShellBorderPane(mainPane);

		mainPane.setCenter(dashboardController);
	}

	@FXML
	public void settingsBtnListener() throws SQLException {
		SettingsController settingsController = new SettingsController();
		
		mainPane.setCenter(settingsController);

	}

	@FXML
	public void logOutBtnListener(MouseEvent event) throws SQLException, IOException {

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		UserSession.cleanUserSession();

		window.setScene(tableViewScene);
		window.show();

	}

}
