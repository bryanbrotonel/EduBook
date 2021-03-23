package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import connectivity.ConnectionClass;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.UserSession;

public class BookAppointmentController extends AnchorPane {

	UserSession currSession;

	@FXML
	TextField apptTitleTextField;
	@FXML
	ChoiceBox<String> profTextField;
	@FXML
	DatePicker apptDatePicker;

	@FXML
	ChoiceBox<String> apptStartTimePicker;

	@FXML
	ChoiceBox<String> apptEndTimePicker;

	@FXML
	Text errMsgText;

	Calendar endOfDay;

	ConnectionClass connectionClass = new ConnectionClass();
	Connection connection = connectionClass.getConnection();
	Statement statement;

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

	@FXML
	public void initialize() throws SQLException, ParseException {
		setEndOfDay();

		configDatePicker();
		configProfPicker();
		configStartTimePicker();
		configEndTimePicker();
	}

	public void configDatePicker() {
		apptDatePicker.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();

				setDisable(empty || date.compareTo(today) < 0 || date.getDayOfWeek().getValue() > 5);
			}
		});
	}

	public void configProfPicker() throws SQLException {

		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement;

		statement = connection.createStatement();

		String sqlSelect = "SELECT * FROM PROFESSORS";

		ResultSet resultSet = statement.executeQuery(sqlSelect);

		while (resultSet.next())
			profTextField.getItems().add(resultSet.getString("LastName") + ", " + resultSet.getString("FirstName"));

	}

	public void configStartTimePicker() throws ParseException {
		DateFormat df = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		while (getCalendarSeconds(cal) < getCalendarSeconds(endOfDay)) {
			apptStartTimePicker.getItems().add(df.format(cal.getTime()));
			cal.add(Calendar.MINUTE, 30);
		}

	}

	public void configEndTimePicker() throws ParseException {
		apptStartTimePicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String startTime = apptStartTimePicker.getItems().get((Integer) number2);
				try {
					setEndTimes(startTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void setEndTimes(String startTime) throws ParseException {
		DateFormat df = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		
		String[] values = startTime.split(":");

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(values[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(values[1]));
		cal.set(Calendar.SECOND, 0);
		
		apptEndTimePicker.getItems().clear();

		for (int i = 0; i < 2; i++) {
			cal.add(Calendar.MINUTE, 30);
			if (getCalendarSeconds(cal) <= getCalendarSeconds(endOfDay))
				apptEndTimePicker.getItems().add(df.format(cal.getTime()));
		}

	}

	public void scheduleApptBtnListener() throws SQLException {
		System.out.println("scheduleApptBtnListener()");

		if (apptTitleTextField.getText() == "")
			errMsgText.setText("Please fill in the Appointment title.");
		else if (profTextField.getValue() == null)
			errMsgText.setText("Please specify a Professor.");
		else if (apptDatePicker.getValue() == null)
			errMsgText.setText("Please specify an Appiontment date.");
		else if (apptStartTimePicker.getValue() == null)
			errMsgText.setText("Please specify an Appiontment start time.");
		else if (apptEndTimePicker.getValue() == null)
			errMsgText.setText("Please specify an Appiontment end time.");
		else {

			String apptTitle = apptTitleTextField.getText();
			System.out.print("[" + apptTitle + ", ");

			String apptProf = getProfEmail(profTextField.getValue());
			System.out.print(apptProf + ", ");

			LocalDate localDate = apptDatePicker.getValue();
			System.out.print(localDate + ", ");

			String apptStartTime = apptStartTimePicker.getValue();
			System.out.print(apptStartTime + ", ");

			String apptEndTime = apptEndTimePicker.getValue();
			System.out.println(apptEndTime + "]");
		}

	}

	public String getProfEmail(String prof) throws SQLException {
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement;
		String profEmail = "";

		String[] values = prof.split(",");

		for (int i = 0; i < values.length; i++)
			values[i] = values[i].trim();

		statement = connection.createStatement();

		String sqlSelect = "SELECT * FROM PROFESSORS WHERE LASTNAME='" + values[0] + "' AND FIRSTNAME='" + values[1]
				+ "';";

		ResultSet resultSet = statement.executeQuery(sqlSelect);

		while (resultSet.next())
			profEmail = resultSet.getString("EMAIL");

		return profEmail;

	}

	public void setEndOfDay() {
		endOfDay = Calendar.getInstance();
		endOfDay.set(Calendar.HOUR_OF_DAY, 17);
		endOfDay.set(Calendar.MINUTE, 0);
		endOfDay.set(Calendar.SECOND, 0);
	}

	public int getCalendarSeconds(Calendar cal) {
		return cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
	}

}
