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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ControllerPercorsi {
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;
	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;
	
	@FXML // fx:id="boxStagione"
	private ComboBox<String> boxStagione;
	@FXML // fx:id="boxOrario"
	private ComboBox<LocalTime> boxOrario;
	@FXML // fx:id="boxPartenza"
	private ComboBox<String> boxPartenza;
	@FXML // fx:id="boxArrivo"
	private ComboBox<String> boxArrivo;
	@FXML // fx:id="txtDurataMinimaOre"
	private TextField txtDurataMinimaOre;
	@FXML // fx:id="txtDurataMinimaMinuti"
	private TextField txtDurataMinimaMinuti;
	@FXML // fx:id="txtDurataMassimaOre"
	private TextField txtDurataMassimaOre;
	@FXML // fx:id="txtDurataMassimaMinuti"
	private TextField txtDurataMassimaMinuti;
	
	@FXML // fx:id="box1"
	private ComboBox<String> box1;
	@FXML // fx:id="box2"
	private ComboBox<String> box2;
	@FXML // fx:id="box3"
	private ComboBox<String> box3;
	@FXML // fx:id="box4"
	private ComboBox<String> box4;
	
	@FXML // fx:id="check1"
	private CheckBox check1;
	@FXML // fx:id="check2"
	private CheckBox check2;
	
	@FXML // fx:id="btnCalcolaBest"
	private Button btnCalcolaBest; // Value injected by FXMLLoader
	@FXML // fx:id="btnCalcolaSicuro"
	private Button btnCalcolaSicuro; // Value injected by FXMLLoader
	
	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
	
	@FXML // fx:id="processTime"
	private Label processTime;
	
	@FXML // fx:id="btnHome"
	private Button btnHome; // Value injected by FXMLLoader
	
	@FXML
	private ImageView img;
	
	private Model model;
	private Stage stage;
	
	Image image1 = new Image("main/mappa.jpg", true);
	
	@FXML  //se il primo animale non è stato scelto non è possibile scegliere il secondo
	void doAble2(ActionEvent event) {
		if(box1.getValue().equals("") || box1.getValue().equals("tutti")) {
			box2.setValue("");
			box2.setDisable(true);
		}
		else
			box2.setDisable(false);
	}
	
	@FXML  
	void doAble3(ActionEvent event) {
		if(box2.getValue().equals("")) {
			box3.setValue("");
			box3.setDisable(true);
		}
		else 
			box3.setDisable(false);
	}
	
	@FXML
	void doAble4(ActionEvent event) {
		if(box3.getValue().equals("")) {
			box4.setValue("");
			box4.setDisable(true);
		}
		else 
			box4.setDisable(false);
	}
	
	@FXML
	void doCalcolaBest(ActionEvent event) {
		long startTime = System.nanoTime();
		
		txtResult.clear();
		
		String stagione=boxStagione.getValue();
		if(stagione==null) {
			txtResult.appendText("Seleziona la stagione in cui si svolgerà il safari");
			return;
		}
		
		LocalTime orarioPartenza = boxOrario.getValue();
		if(orarioPartenza==null) {
			txtResult.appendText("Seleziona a che ora inizierà il safari");
			return;
		}
		
		char partenza;
		char arrivo;
		try {
			partenza = boxPartenza.getValue().charAt(0);
			arrivo = boxArrivo.getValue().charAt(0);
		} catch (Exception e) {
			txtResult.appendText("Seleziona un gate di partenza e uno di arrivo");
			e.printStackTrace();
			return;
		}
		
		LocalTime orarioArrivoMin;
		int oraMin=0;	
		int minutoMin=0;
			
		if(!(txtDurataMinimaOre.getText().equals(""))) {
			try {
				oraMin = Integer.valueOf(txtDurataMinimaOre.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
			if(oraMin>12 || oraMin<0) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
		}	
		if(!(txtDurataMinimaMinuti.getText().equals(""))) {
			try {
				minutoMin = Integer.valueOf(txtDurataMinimaMinuti.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
			if(minutoMin<0 || minutoMin>60) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
		}
		if(oraMin!=0 || minutoMin!=0) {
			orarioArrivoMin = orarioPartenza.plusHours(oraMin);
			orarioArrivoMin = orarioArrivoMin.plusMinutes(minutoMin);
		}
		else
			orarioArrivoMin=orarioPartenza.plusHours(2);

		
		//Gestisco la possibilità che nel campo durata max sia vuoto solo il campo ore, o solo i minuti
		//Se sono vuoti entrambi ignoro perche la durata è facoltativa e l'orario di Arrivo max sarà 19:30
		LocalTime orarioArrivoMax;
		int ora=0;
		int minuto=0;
		if(!(txtDurataMassimaOre.getText().equals(""))) {
			try {
				ora = Integer.valueOf(txtDurataMassimaOre.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
			if(ora>12 || ora<0) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
		}	
		if(!(txtDurataMassimaMinuti.getText().equals(""))) {
			try {
				minuto = Integer.valueOf(txtDurataMassimaMinuti.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
			if(minuto<0 || minuto>60) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
		}
		
		if(ora!=0 || minuto!=0) {
			orarioArrivoMax = orarioPartenza.plusHours(ora);
			orarioArrivoMax = orarioArrivoMax.plusMinutes(minuto);
		}
		else
			orarioArrivoMax=LocalTime.of(19, 30);

		String s1 = box1.getValue();
		String s2 = box2.getValue();
		String s3 = box3.getValue();
		String s4 = box4.getValue();
		if(s1.equals("")) {
			txtResult.appendText("Seleziona almeno 1 animale che sei interessato a vedere durante il safari");
			return;
		}
		if (s1.equals(s2) || s1.equals(s3) || s1.equals(s4) || (s2.equals(s3) && !s2.equals(""))
				|| (s2.equals(s4) && !s2.equals("")) || (s3.equals(s4) && !s3.equals(""))) {
					txtResult.appendText("Non è permesso selezionare una specie più di una volta");
					return;
		}
		
		Boolean palude = check1.isSelected();
		Boolean baobab = check2.isSelected();
		
		List<Character> percorso = new ArrayList<>(model.calcolaBest(s1, s2, s3, s4,
			stagione, partenza, arrivo, orarioPartenza, orarioArrivoMin, orarioArrivoMax, palude, baobab));
		
		if(percorso.size()==0) {
			txtResult.appendText("Nessun risultato trovato. Aumenta la durata massima concessa");
			return;
		}
		
		for(int i=0; i<percorso.size(); i++) {
			if(i==percorso.size()-1)
				txtResult.appendText(percorso.get(i).toString());
			else
				txtResult.appendText(percorso.get(i).toString()+ " -> ");
		}
		
		txtResult.appendText("\nL'orario di arrivo al"+boxArrivo.getValue().substring(3)+
				" è previsto alle " + model.getOrarioArrivo());
		
		if(stagione.equals("secca"))
			txtResult.appendText("\nIn passato, lungo questo percorso, durante la stagione secca,"
				+ " durante simili orari della giornata, sono stati avvistati:\n");
		if(stagione.equals("piogge"))
			txtResult.appendText("\nIn passato, lungo questo percorso, durante la stagione delle piogge,"
				+ " durante simili orari della giornata, sono stati avvistati:\n");
		
		//Output numero avvistamenti animali passati con gestione nomi singolari e plurali
		if(model.getNumS1()>1) {
			if(s1.equals("tutti"))
				txtResult.appendText(model.getNumS1() + " animali\n");
			else {
				if(s1.charAt(s1.length()-1)=='a')
					txtResult.appendText(String.valueOf(model.getNumS1()) + " " + s1.substring(0, s1.length()-1)+'e'+"\n");
				else if(s1.charAt(s1.length()-1)=='o' || s1.charAt(s1.length()-1)=='e')
					txtResult.appendText(String.valueOf(model.getNumS1()) + " " + s1.substring(0, s1.length()-1)+'i'+"\n");
				else
					txtResult.appendText(String.valueOf(model.getNumS1()) + " " + s1+"\n");
			}
		}
		else if(model.getNumS1()==1) {
			txtResult.appendText("1 " + s1+"\n");
		}
		
		if(model.getNumS2()>1) {
			if(s2.charAt(s2.length()-1)=='a')
				txtResult.appendText(String.valueOf(model.getNumS2()) + " " + s2.substring(0, s2.length()-1)+'e'+"\n");
			else if(s2.charAt(s2.length()-1)=='o' || s2.charAt(s2.length()-1)=='e')
				txtResult.appendText(String.valueOf(model.getNumS2()) + " " + s2.substring(0, s2.length()-1)+'i'+"\n");
			else
				txtResult.appendText(String.valueOf(model.getNumS2()) + " " + s2+"\n");
		}
		else if(model.getNumS2()==1) {
			txtResult.appendText("1 " + s2+"\n");
		}
		
		if(model.getNumS3()>1) {
			if(s3.charAt(s3.length()-1)=='a')
				txtResult.appendText(String.valueOf(model.getNumS3()) + " " + s3.substring(0, s3.length()-1)+'e'+"\n");
			else if(s3.charAt(s3.length()-1)=='o' || s3.charAt(s3.length()-1)=='e')
				txtResult.appendText(String.valueOf(model.getNumS3()) + " " + s3.substring(0, s3.length()-1)+'i'+"\n");
			else
				txtResult.appendText(String.valueOf(model.getNumS3()) + " " + s3+"\n");
		}
		else if(model.getNumS3()==1) {
			txtResult.appendText("1 " + s3+"\n");
		}
		
		if(model.getNumS4()>1) {
			if(s4.charAt(s4.length()-1)=='a')
				txtResult.appendText(String.valueOf(model.getNumS4()) + " " + s4.substring(0, s4.length()-1)+'e'+"\n");
			else if(s4.charAt(s4.length()-1)=='o' || s4.charAt(s4.length()-1)=='e')
				txtResult.appendText(String.valueOf(model.getNumS4()) + " " + s4.substring(0, s4.length()-1)+'i'+"\n");
			else
				txtResult.appendText(String.valueOf(model.getNumS4()) + " " + s4+"\n");
		}
		else if(model.getNumS4()==1) {
			txtResult.appendText("1 " + s4+"\n");
		}
		
		if(model.getNumMigrazioni()>1) {
			txtResult.appendText(String.valueOf(model.getNumMigrazioni()) + " migrazioni di gnu\n");
		}
		else if(model.getNumMigrazioni()==1) {
			txtResult.appendText("1 migrazione di gnu" +"\n");
		}
		
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;
		
		processTime.setText(String.valueOf(duration) + " ms");
		
	}

	@FXML
	void doCalcolaSicuro(ActionEvent event) {
		long startTime = System.nanoTime();
		txtResult.clear();
		
		String stagione=boxStagione.getValue();
		if(stagione==null) {
			txtResult.appendText("Seleziona la stagione in cui si svolgerà il safari");
			return;
		}
		
		LocalTime orarioPartenza = boxOrario.getValue();
		if(orarioPartenza==null) {
			txtResult.appendText("Seleziona a che ora inizierà il safari");
			return;
		}
		
		char partenza;
		char arrivo;
		try {
			partenza = boxPartenza.getValue().charAt(0);
			arrivo = boxArrivo.getValue().charAt(0);
		} catch (Exception e) {
			txtResult.appendText("Seleziona un gate di partenza e uno di arrivo");
			e.printStackTrace();
			return;
		}
		
		LocalTime orarioArrivoMin;
		int oraMin=0;	
		int minutoMin=0;
			
		if(!(txtDurataMinimaOre.getText().equals(""))) {
			try {
				oraMin = Integer.valueOf(txtDurataMinimaOre.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
			if(oraMin>12 || oraMin<0) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
		}	
		if(!(txtDurataMinimaMinuti.getText().equals(""))) {
			try {
				minutoMin = Integer.valueOf(txtDurataMinimaMinuti.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
			if(minutoMin<0 || minutoMin>60) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
		}
		if(oraMin!=0 || minutoMin!=0) {
			orarioArrivoMin = orarioPartenza.plusHours(oraMin);
			orarioArrivoMin = orarioArrivoMin.plusMinutes(minutoMin);
		}
		else
			orarioArrivoMin=orarioPartenza.plusHours(2);

		LocalTime orarioArrivoMax;
		int ora=0;
		int minuto=0;
		if(!(txtDurataMassimaOre.getText().equals(""))) {
			try {
				ora = Integer.valueOf(txtDurataMassimaOre.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
			if(ora>12 || ora<0) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo ore");
				return;
			}
		}	
		if(!(txtDurataMassimaMinuti.getText().equals(""))) {
			try {
				minuto = Integer.valueOf(txtDurataMassimaMinuti.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
			if(minuto<0 || minuto>60) {
				txtResult.appendText("Scrivi un numero intero e valido nel campo minuti");
				return;
			}
		}
		if(ora!=0 || minuto!=0) {
			orarioArrivoMax = orarioPartenza.plusHours(ora);
			orarioArrivoMax = orarioArrivoMax.plusMinutes(minuto);
		}
		else
			orarioArrivoMax=LocalTime.of(19, 30);
		
		Boolean palude = check1.isSelected();
		Boolean baobab = check2.isSelected();
		
		List<Character> percorso = new ArrayList<>(model.calcolaSicuro(stagione, partenza,
				arrivo, orarioPartenza, orarioArrivoMin, orarioArrivoMax, palude, baobab));
		
		if(percorso.size()==0) {
			txtResult.appendText("Nessun risultato trovato. Aumenta la durata massima concessa");
			return;
		}
		
		for(int i=0; i<percorso.size(); i++) {
			if(i==percorso.size()-1)
				txtResult.appendText(percorso.get(i).toString());
			else
				txtResult.appendText(percorso.get(i).toString()+ " -> ");
		}
		
		txtResult.appendText("\nL'orario di arrivo al"+boxArrivo.getValue().substring(3)+
				" è previsto alle " + model.getOrarioArrivo());
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;
		
		processTime.setText(String.valueOf(duration) + " ms");
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
	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxStagione != null : "fx:id=\"boxStagione\" was not injected: check your FXML file 'Home.fxml'.";
		assert boxOrario != null : "fx:id=\"boxOrario\" was not injected: check your FXML file 'Home.fxml'.";
		assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Home.fxml'.";
		assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Home.fxml'.";
		assert txtDurataMinimaOre != null : "fx:id=\"txtDurataMinimaOre\" was not injected: check your FXML file 'Home.fxml'.";
		assert txtDurataMinimaMinuti != null : "fx:id=\"txtDurataMinimaMinuti\" was not injected: check your FXML file 'Home.fxml'.";
		assert txtDurataMassimaOre != null : "fx:id=\"txtDurataMassimaOre\" was not injected: check your FXML file 'Home.fxml'.";
		assert txtDurataMassimaMinuti != null : "fx:id=\"txtDurataMassimaMinuti\" was not injected: check your FXML file 'Home.fxml'.";
		assert check1 != null : "fx:id=\"check1\" was not injected: check your FXML file 'Home.fxml'.";
		assert check2 != null : "fx:id=\"check2\" was not injected: check your FXML file 'Home.fxml'.";
		assert box1 != null : "fx:id=\"box1\" was not injected: check your FXML file 'Home.fxml'.";
		assert box2 != null : "fx:id=\"box2\" was not injected: check your FXML file 'Home.fxml'.";
		assert box3 != null : "fx:id=\"box3\" was not injected: check your FXML file 'Home.fxml'.";
		assert box4 != null : "fx:id=\"box4\" was not injected: check your FXML file 'Home.fxml'.";
		assert btnCalcolaBest != null : "fx:id=\"btnCalcolaBest\" was not injected: check your FXML file 'Home.fxml'.";
		assert btnCalcolaSicuro != null : "fx:id=\"btnCalcolaSicuro\" was not injected: check your FXML file 'Home.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Home.fxml'.";
		assert processTime != null : "fx:id=\"processTime\" was not injected: check your FXML file 'CalcoloPercorsi.fxml'.";
		assert btnHome != null : "fx:id=\"btnHome\" was not injected: check your FXML file 'CalcoloPercorsi.fxml'.";
		assert img != null : "fx:id=\"img\" was not injected: check your FXML file 'CalcoloPercorsi.fxml'.";
	}

	public void setModel(Model model, Stage stage) {
		this.model = model;
		this.stage=stage;
		img.setImage(image1);
		
		model.creaGrafo();
		
		boxStagione.getItems().addAll("secca", "piogge");
		
		List<LocalTime> orari = new ArrayList<LocalTime>();
		for(int i=7; i<20; i++) {
			orari.add(LocalTime.of(i, 0));
		}
		boxOrario.getItems().addAll(orari);
		
		boxPartenza.getItems().add("A - Oloolaimutia Gate");
		boxPartenza.getItems().add("Z - Sekenani Gate");
		boxPartenza.getItems().add("Q - Musiara Gate");
		boxArrivo.getItems().addAll(boxPartenza.getItems());
		
		box1.getItems().add("");
		box1.getItems().addAll(this.model.getSpecie());
		box2.getItems().addAll(box1.getItems());
		box3.getItems().addAll(box1.getItems());
		box4.getItems().addAll(box1.getItems());
		
		box1.getItems().add("tutti");
		
		box1.setValue("");
		box2.setValue("");
		box3.setValue("");
		box4.setValue("");

	}
}
