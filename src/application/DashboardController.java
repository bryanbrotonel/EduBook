package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import connectivity.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class DashboardController extends BorderPane {

	UserSession currSession;

	@FXML
	Text greetingText;
	TableView apptTable;

	public DashboardController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));

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
	void initialize() throws SQLException {
		greetingText.setText("Hello, " + currSession.getFirstName());
		populateApptTable();
		
	}
	
	void populateApptTable() throws SQLException {
		
		List<String[]> appointments = currSession.getAppt(currSession.getEmail());
		
		for (String[] row : appointments) {
			System.out.println(Arrays.toString(row));
			
		}

	}

}