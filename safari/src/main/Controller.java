package main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

public class Controller {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button btnPercorsi;
    @FXML
    private Button btnAnalisi;
    
	private Stage stage;

    @FXML
    void doGoAnalisi(ActionEvent event) {
    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("AnalisiDensità.fxml"));
			BorderPane root = (BorderPane)loader.load();
			
			ControllerAnalisi controller = loader.getController();
			
			Model model = new Model();
			controller.setModel(model,this.stage);
			
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    @FXML
    void doGoPercorsi(ActionEvent event) {
    		try {
    	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CalcoloPercorsi.fxml"));
    			BorderPane root = (BorderPane)loader.load();
    			
    			ControllerPercorsi controller = loader.getController();
    			
    			Model model = new Model();
    			controller.setModel(model,this.stage);
    			
    			Scene scene = new Scene(root);
    			
    			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    			stage.setScene(scene);
    			stage.show();
    			
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    }

    
    public void setStage(Stage stage) {
		this.stage=stage;
	}
    
    @FXML
    void initialize() {
        assert btnPercorsi != null : "fx:id=\"btnPercorsi\" was not injected: check your FXML file 'Home.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Home.fxml'.";

    }
}