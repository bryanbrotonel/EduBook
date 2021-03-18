package application;

import java.io.IOException;
import java.sql.SQLException;

import connectivity.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class DashboardController extends AnchorPane {
	
	UserSession currSession;
	
	@FXML Text greetingTextLabel;

    public DashboardController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void initialize() throws SQLException {
    	currSession = UserSession.getInstance();
    	greetingTextLabel.setText("Hello, " + currSession.getFirstName());
	}

}