package application;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import models.Appointment;
import models.UserSession;

public class DashboardController extends BorderPane {

	UserSession currSession;

	@FXML
	Text greetingText;
	@FXML
	TableView<Appointment> apptTable;
	@FXML
	TableColumn<Appointment, String> apptCol;
	@FXML
	TableColumn<Appointment, String> profCol;
	@FXML
	TableColumn<Appointment, String> dateCol;
	@FXML
	TableColumn<Appointment, String> startTimeCol;
	@FXML
	TableColumn<Appointment, String> endTimeCol;

	ObservableList<Appointment> tableData;

	ArrayList<Appointment> apptData;

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
	public void initialize() throws SQLException {
		greetingText.setText("Hello, " + currSession.getFirstName());

		setUpTable();

	}

	public void setUpTable() throws SQLException {
		apptCol.setCellValueFactory(new PropertyValueFactory<>("ApptTitle"));
		profCol.setCellValueFactory(new PropertyValueFactory<>("ApptProfessor"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("ApptDate"));
		startTimeCol.setCellValueFactory(new PropertyValueFactory<>("ApptStartTime"));
		endTimeCol.setCellValueFactory(new PropertyValueFactory<>("ApptEndTime"));

		populateApptTable();

		apptTable.setItems(tableData);

	}

	public void populateApptTable() throws SQLException {

		apptData = new ArrayList<Appointment>();

		List<String[]> appointments = currSession.getAppt(currSession.getEmail());

		for (String[] row : appointments) {
			System.out.println(Arrays.toString(row));

			int idValue = Integer.valueOf(row[0]);
			String titleValue = row[1];
			String dateValue = row[2];
			String startTimeValue = row[3];
			String endTimeValue = row[4];
			String studentValue = row[5];
			String professorValue = row[6];

			apptData.add(new Appointment(idValue, titleValue, dateValue, startTimeValue, endTimeValue, studentValue, professorValue));

			tableData = FXCollections.observableArrayList(apptData);

		}
	}
	
}