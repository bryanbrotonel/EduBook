package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connectivity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
	@FXML
	TableColumn<Appointment, String> editCol;
	@FXML
	Button bookApptBtn;
	@FXML
	Text dashboardErrorText;
	@FXML
	Text apptTodayText;

	BorderPane shellPane;

	Appointment selectedAppt;

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
		setTodayText();

	}

	private void setTodayText() {
		int todayAppts = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();

		for (Appointment a : apptData)
			if (a.getApptDate().equalsIgnoreCase(formatter.format(date)))
				todayAppts++;

		apptTodayText.setText("You have " + todayAppts + " appointments today.");

	}

	public void bookApptBtnListener() throws SQLException {

		BookAppointmentController bookAppt = new BookAppointmentController();

		bookAppt.setShellBorderPane(shellPane);

		shellPane.setCenter(bookAppt);
	}

	public void setUpTable() throws SQLException {
		apptCol.setCellValueFactory(new PropertyValueFactory<>("ApptTitle"));
		profCol.setCellValueFactory(new PropertyValueFactory<>("ApptProfessor"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("ApptDate"));
		startTimeCol.setCellValueFactory(new PropertyValueFactory<>("ApptStartTime"));
		endTimeCol.setCellValueFactory(new PropertyValueFactory<>("ApptEndTime"));

		apptTable.setRowFactory(tv -> {
			TableRow<Appointment> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty()))
					selectedAppt = row.getItem();
			});
			return row;
		});

		populateApptTable();

		apptTable.getSortOrder().add(dateCol);

	}

	public void populateApptTable() throws SQLException {

		apptData = new ArrayList<Appointment>();

		List<String[]> appointments = currSession.getAppt(currSession.getEmail());

		for (String[] row : appointments) {

			int idValue = Integer.valueOf(row[0]);
			String titleValue = row[1];
			String dateValue = row[2];
			String startTimeValue = row[3];
			String endTimeValue = row[4];
			String studentValue = row[5];
			String professorValue = row[6];

			apptData.add(new Appointment(idValue, titleValue, dateValue, startTimeValue, endTimeValue, studentValue,
					professorValue));

			tableData = FXCollections.observableArrayList(apptData);

		}

		apptTable.setItems(tableData);
	}

	public void editApptBtnListener() throws SQLException {

		if (selectedAppt == null)
			dashboardErrorText.setText("Select an appointment to edit");
		else {
			BookAppointmentController editAppt = new BookAppointmentController(selectedAppt);

			editAppt.setShellBorderPane(shellPane);
			shellPane.setCenter(editAppt);
		}

	}

	public void deleteApptBtnListener(ActionEvent event) throws SQLException, IOException {

		if (selectedAppt == null)
			dashboardErrorText.setText("Select an appointment to delete");
		else {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();

			String sqlDelete = "DELETE FROM APPOINTMENTS WHERE ID = " + selectedAppt.getApptID() + ";";

			statement.execute(sqlDelete);

			selectedAppt = null;

			apptTable.getItems().clear();
			populateApptTable();
		}

	}

	public void setShellBorderPane(BorderPane pane) {
		this.shellPane = pane;
	}

}