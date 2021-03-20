/**
 * 
 */
package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author bryan
 *
 */
public class Appointment {

	private SimpleIntegerProperty apptID;
	private SimpleStringProperty apptTitle;
	private SimpleStringProperty apptTime;
	private SimpleStringProperty apptDate;
	private SimpleStringProperty apptStudent;
	private SimpleStringProperty apptProfessor;

	public Appointment(int apptID, String apptTitle, String apptTime, String apptDate, String apptStudent,
			String apptProfessor) {
		
		this.apptID = new SimpleIntegerProperty(apptID);
		this.apptTitle = new SimpleStringProperty(apptTitle);
		this.apptTime = new SimpleStringProperty(apptTime);
		this.apptDate = new SimpleStringProperty(apptDate);
		this.apptStudent = new SimpleStringProperty(apptStudent);
		this.apptProfessor = new SimpleStringProperty(apptProfessor);

	}

	public int getApptID() {
		return apptID.get();
	}

	public void setApptID(int apptID) {
		this.apptID = new SimpleIntegerProperty(apptID);
	}

	public String getApptTitle() {
		return apptTitle.get();
	}

	public void setApptTitle(String apptTitle) {
		this.apptTitle = new SimpleStringProperty(apptTitle);
	}

	public String getApptTime() {
		return apptTime.get();
	}

	public void setApptTime(String apptTime) {
		this.apptTime = new SimpleStringProperty(apptTime);
	}

	public String getApptDate() {
		return apptDate.get();
	}

	public void setApptDate(String apptDate) {
		this.apptDate = new SimpleStringProperty(apptDate);
	}

	public String getApptStudent() {
		return apptStudent.get();
	}

	public void setApptStudent(String apptStudent) {
		this.apptStudent = new SimpleStringProperty(apptStudent);
	}

	public String getApptProfessor() {
		return apptProfessor.get();
	}

	public void setApptProfessor(String apptProfessor) {
		this.apptProfessor = new SimpleStringProperty(apptProfessor);
	}

}
