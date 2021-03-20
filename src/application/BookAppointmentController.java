package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.UserSession;

public class BookAppointmentController extends AnchorPane {
	
	UserSession currSession;
	
	public BookAppointmentController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookAppointment.fxml"));

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
