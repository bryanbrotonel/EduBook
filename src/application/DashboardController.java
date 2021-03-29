package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
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

	BorderPane shellPane;

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

		Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> cellFactory = new Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>>() {
			@Override
			public TableCell<Appointment, String> call(final TableColumn<Appointment, String> param) {
				final TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {
					final Button btn = new Button("Edit");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction(event -> {
								Appointment appointment = getTableView().getItems().get(getIndex());
								System.out.println(appointment.getApptTitle());

								BookAppointmentController editAppt;

								try {
									editAppt = new BookAppointmentController(appointment.getApptTitle(),
											appointment.getApptProfessor(), appointment.getApptDate(),
											appointment.getApptStartTime(), appointment.getApptEndTime());
									editAppt.setShellBorderPane(shellPane);
									shellPane.setCenter(editAppt);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							});
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		editCol.setCellFactory(cellFactory);

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

			apptData.add(new Appointment(idValue, titleValue, dateValue, startTimeValue, endTimeValue, studentValue,
					professorValue));

			tableData = FXCollections.observableArrayList(apptData);

		}
	}

	public void setShellBorderPane(BorderPane pane) {
		shellPane = pane;
	}

}