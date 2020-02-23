package main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

public class ControllerAnalisi {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;
	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;
	
    @FXML
    private ComboBox<String> boxStagione;
    @FXML
    private CheckBox checkLeone;
    @FXML
    private CheckBox checkElefante;
    @FXML
    private CheckBox checkRinoceronte;
    @FXML
    private CheckBox checkBufalo;
    @FXML
    private CheckBox checkLeopardo;
    @FXML
    private CheckBox checkGhepardo;
    @FXML
    private CheckBox checkZebra;
    @FXML
    private CheckBox checkAntilope;
    @FXML
    private CheckBox checkGnu;
    @FXML
    private CheckBox checkIppopotamo;
    @FXML
    private CheckBox checkCoccodrillo;
    @FXML
    private CheckBox checkGiraffa;
    @FXML
    private CheckBox checkIena;
    @FXML
    private Button btnAnalisi;
    @FXML
    private TextArea txtResult;
    @FXML
    private Button btnIndietro;

	private Model model;
	private Stage stage;

    @FXML
    void doAnalisi(ActionEvent event) {
    	txtResult.clear();
    	
    	String stagione=boxStagione.getValue();
		if(stagione==null) {
			txtResult.appendText("Seleziona la stagione da analizzare, o seleziona \"entrambe\" ");
			return;
		}
    	
    	boolean leone=checkLeone.isSelected();
      	boolean elefante=checkElefante.isSelected();
      	boolean rinoceronte=checkRinoceronte.isSelected();
      	boolean bufalo=checkBufalo.isSelected();
      	boolean leopardo=checkLeopardo.isSelected();
      	boolean ghepardo=checkGhepardo.isSelected();
      	boolean zebra=checkZebra.isSelected();
      	boolean antilope=checkAntilope.isSelected();
      	boolean gnu=checkGnu.isSelected();
      	boolean ippopotamo=checkIppopotamo.isSelected();
      	boolean coccodrillo=checkCoccodrillo.isSelected();
      	boolean giraffa=checkGiraffa.isSelected();
      	boolean iena=checkIena.isSelected();
      	
      	Map<String,Double> densita = new HashMap<String,Double>(model.getDensita(stagione,leone,elefante,rinoceronte,bufalo,leopardo,ghepardo,
      			zebra,antilope,gnu,ippopotamo,coccodrillo,giraffa,iena));
      	
      	if(densita.size()!=0) {
      		txtResult.appendText("Nel masai mara, la stima della densità degli animali selezionati e':");
      		for(String animale : densita.keySet())
      			txtResult.appendText("\n"+animale+": "+densita.get(animale).floatValue()+"/km^2");
      	}
      	else
      		txtResult.appendText("Seleziona almeno un animale di cui vuoi conoscere la densità");
   
    }

    @FXML
    void doGoHome(ActionEvent event) {
    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			BorderPane root = (BorderPane)loader.load();
			
			Controller controller = loader.getController();
			
			controller.setStage(this.stage);
			
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

	public void setModel(Model model, Stage stage) {
		this.model=model;
		this.stage=stage;
		
		boxStagione.getItems().add("secca");
		boxStagione.getItems().add("piogge");
		boxStagione.getItems().add("entrambe");
	}

	@FXML
    void initialize() {
        assert boxStagione != null : "fx:id=\"boxStagione\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkLeone != null : "fx:id=\"checkLeone\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkElefante != null : "fx:id=\"checkElefante\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkRinoceronte != null : "fx:id=\"checkRinoceronte\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkBufalo != null : "fx:id=\"checkBufalo\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkLeopardo != null : "fx:id=\"checkLeopardo\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkGhepardo != null : "fx:id=\"checkGhepardo\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkZebra != null : "fx:id=\"checkZebra\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkAntilope != null : "fx:id=\"checkAntilope\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkGnu != null : "fx:id=\"checkGnu\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkIppopotamo != null : "fx:id=\"checkIppopotamo\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkCoccodrillo != null : "fx:id=\"checkCoccodrillo\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkGiraffa != null : "fx:id=\"checkGiraffa\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert checkIena != null : "fx:id=\"checkIena\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";
        assert btnIndietro != null : "fx:id=\"btnIndietro\" was not injected: check your FXML file 'AnalisiDensità.fxml'.";

    }
}

