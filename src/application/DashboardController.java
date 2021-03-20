package application;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	TableColumn<Appointment, String> timeCol;

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
		timeCol.setCellValueFactory(new PropertyValueFactory<>("ApptTime"));

		populateApptTable();

		apptTable.setItems(tableData);

	}

	public void populateApptTable() throws SQLException {

		apptData = new ArrayList<Appointment>();

		List<String[]> appointments = currSession.getAppt(currSession.getEmail());

		for (String[] row : appointments) {
			System.out.println(Arrays.toString(row));

			String timestamp = Timestamp.valueOf(row[2]).toString();

			int idValue = Integer.valueOf(row[0]);
			String titleValue = row[1];
			String timeValue = timestamp.substring(timestamp.length() - 10);;
			String dateValue = timestamp.substring(0, 10);
			String studentValue = row[3];
			String professorValue = row[4];

			apptData.add(new Appointment(idValue, titleValue, timeValue, dateValue, studentValue, professorValue));

			tableData = FXCollections.observableArrayList(apptData);

		}
	}
	
}