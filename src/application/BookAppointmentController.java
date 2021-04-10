package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import models.Appointment;
import models.UserSession;

public class BookAppointmentController extends AnchorPane {

	UserSession currSession;

	BorderPane shellPane;

	@FXML
	Text bookApptTitleText;
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

	String[] apptData;
	ArrayList<String> profSchedule;
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

	public BookAppointmentController(Appointment appt) throws SQLException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookAppointment.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		currSession = UserSession.getInstance();

		setApptData(appt.getApptTitle(), appt.getApptProfessor(), appt.getApptDate(), appt.getApptStartTime(),
				appt.getApptEndTime());

		setProfAvailability();

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() throws SQLException, ParseException {

		setEndOfDay();

		if (apptData != null) {
			String startTime = formatTime(apptData[3]);

			setStartTimes();
			setEndTimes(startTime);

			apptTitleTextField.setText(apptData[1]);
			apptDatePicker.setValue(LocalDate.parse(apptData[2]));
			apptStartTimePicker.setValue(startTime);
			apptEndTimePicker.setValue(apptData[4].substring(0, apptData[4].length() - 3));
			profTextField.setValue(apptData[6]);

		}

		configProfPicker();
		configDatePicker();
		configStartTimePicker();
		configEndTimePicker();

		bookApptTitleText.setText(apptData == null ? "Book Appointment" : "Edit Appointment");

	}

	public void setApptData(String apptTitle, String apptProf, String apptDate, String apptStartTime,
			String apptEndTime) throws SQLException {

		String stuEmail = currSession.getEmail();

		statement = connection.createStatement();

		String sqlSelect = "SELECT * FROM APPOINTMENTS WHERE TITLE='" + apptTitle + "' AND DATE='" + apptDate
				+ "' AND STARTTIME='" + apptStartTime + "' AND ENDTIME='" + apptEndTime + "' AND STUDENT='" + stuEmail
				+ "' AND PROFESSOR='" + apptProf + "';";

		ResultSet resultSet = statement.executeQuery(sqlSelect);
		ResultSetMetaData resultSetMD = resultSet.getMetaData();
		int numberOfCol = resultSetMD.getColumnCount();

		apptData = new String[numberOfCol];

		while (resultSet.next())
			apptData[0] = resultSet.getString("ID");

		sqlSelect = "SELECT * FROM PROFESSORS WHERE EMAIL='" + apptProf + "';";
		resultSet = statement.executeQuery(sqlSelect);

		while (resultSet.next())
			apptData[6] = resultSet.getString("LastName") + ", " + resultSet.getString("FirstName");

		apptData[1] = apptTitle;
		apptData[2] = apptDate;
		apptData[3] = apptStartTime;
		apptData[4] = apptEndTime;
		apptData[5] = stuEmail;

	}

	public void scheduleApptBtnListener() throws SQLException {

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

			LocalDate apptDate = apptDatePicker.getValue();
			System.out.print(apptDate + ", ");

			String apptStartTime = apptStartTimePicker.getValue();
			System.out.print(apptStartTime + ", ");

			String apptEndTime = apptEndTimePicker.getValue();
			System.out.println(apptEndTime + "]");

			scheduleAppointment(apptTitle, apptProf, apptDate, apptStartTime, apptEndTime);
		}

	}

	public void scheduleAppointment(String apptTitle, String apptProf, LocalDate apptDate, String apptStartTime,
			String apptEndTime) throws SQLException {

		statement = connection.createStatement();

		String sqlInsert = apptData == null
				? "INSERT INTO APPOINTMENTS(Title, Date, StartTime, EndTime, Student, Professor) VALUES('" + apptTitle
						+ "', '" + apptDate + "', '" + apptStartTime + "', '" + apptEndTime + "', '"
						+ currSession.getEmail() + "', '" + apptProf + "')"
				: "UPDATE APPOINTMENTS SET TITLE='" + apptTitle + "', DATE='" + apptDate + "', STARTTIME='"
						+ apptStartTime + "', ENDTIME='" + apptEndTime + "', STUDENT='" + currSession.getEmail()
						+ "', PROFESSOR='" + apptProf + "' WHERE ID = " + apptData[0] + ";";

		if (statement.executeUpdate(sqlInsert) == 1)
			redirectToDashboard();

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

		statement = connection.createStatement();

		String sqlSelect = "SELECT * FROM PROFESSORS";

		ResultSet resultSet = statement.executeQuery(sqlSelect);

		while (resultSet.next())
			profTextField.getItems().add(resultSet.getString("LastName") + ", " + resultSet.getString("FirstName"));

	}

	public void setProfAvailability() throws SQLException {

		String date = apptData != null ? apptData[2] : apptDatePicker.getValue().toString();
		String profEmail = apptData != null ? getProfEmail(apptData[6]) : getProfEmail(profTextField.getValue());

		LocalDate dateValue = LocalDate.parse(date);
		String sqlSelect = "SELECT * FROM APPOINTMENTS WHERE PROFESSOR='" + profEmail + "' AND DATE='"
				+ dateValue.format(DateTimeFormatter.ISO_LOCAL_DATE) + "';";

		profSchedule = new ArrayList<String>();
		ResultSet resultSet = statement.executeQuery(sqlSelect);

		while (resultSet.next()) {

			String startTime = resultSet.getString("STARTTIME");
			String endTime = resultSet.getString("ENDTIME");

			Calendar startTimeCal = parseTime(startTime);
			Calendar endTimeCal = parseTime(endTime);

			while (getCalendarSeconds(startTimeCal) < getCalendarSeconds(endTimeCal)) {

				String timeValue = formatTime(startTimeCal);

				if (!profSchedule.contains(timeValue))
					profSchedule.add(timeValue);

				startTimeCal.add(Calendar.MINUTE, 30);
			}
		}

		if (apptData != null) {
			Calendar startTime = parseTime(apptData[3]);
			Calendar endTime = parseTime(apptData[4]);
			
			while (getCalendarSeconds(endTime) >= getCalendarSeconds(startTime)) {
				String timeValue = formatTime(endTime);
				
				profSchedule.remove(timeValue);

				endTime.add(Calendar.MINUTE, -30);
			}
		}
	}

	public void configStartTimePicker() throws ParseException, SQLException {

		profTextField.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					setStartTimes();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		apptDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				try {
					setStartTimes();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void setStartTimes() throws SQLException {

		if ((profTextField.getValue() != null && apptDatePicker.getValue() != null) || apptData != null) {

			setProfAvailability();

			apptStartTimePicker.getItems().clear();

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			while (getCalendarSeconds(cal) < getCalendarSeconds(endOfDay)) {
				String timeValue = formatTime(cal);

				if (!profSchedule.contains(timeValue))
					apptStartTimePicker.getItems().add(timeValue);

				cal.add(Calendar.MINUTE, 30);
			}
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
		Calendar cal = parseTime(startTime);

		apptEndTimePicker.getItems().clear();

		cal.add(Calendar.MINUTE, 30);

		String timeValue = formatTime(cal);
		apptEndTimePicker.getItems().add(timeValue);

		if ((getCalendarSeconds(cal) < getCalendarSeconds(endOfDay)) && !profSchedule.contains(timeValue)) {
			cal.add(Calendar.MINUTE, 30);
			apptEndTimePicker.getItems().add(formatTime(cal));

		}

	}

	public String getProfEmail(String prof) throws SQLException {

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

	public Calendar parseTime(String time) {
		Calendar cal = Calendar.getInstance();

		String[] values = time.split(":");

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(values[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(values[1]));
		cal.set(Calendar.SECOND, 0);

		return cal;
	}

	public String formatTime(Calendar cal) {
		DateFormat df = new SimpleDateFormat("HH:mm");

		return df.format(cal.getTime());
	}

	public String formatTime(String time) {
		return time.substring(0, apptData[3].length() - 3);
	}

	public int getCalendarSeconds(Calendar cal) {
		return cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
	}

	public void redirectToDashboard() {
		DashboardController dashboardController = new DashboardController();

		dashboardController.setShellBorderPane(shellPane);
		this.shellPane.setCenter(dashboardController);
	}

	public void setShellBorderPane(BorderPane pane) {
		this.shellPane = pane;
	}

}
