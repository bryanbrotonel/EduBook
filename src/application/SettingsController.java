package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.UserSession;

public class SettingsController extends AnchorPane {
	
	UserSession currSession;
	
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
}
