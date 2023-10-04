package mdp.candyfactory.distributer.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController {
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	TextField nameField;
	
	@FXML
	Label infoLabel;
	
	//Reading the companies txt file inside the project folder and checking if there is a company which corresponds the input company. If so, 
	//distributor is successfuly logged in.
	public void login(ActionEvent event) {
		
		if(nameField.getText().length() < 1)
			infoLabel.setText("Field cannot be empty!");
		else {
			try (BufferedReader reader = new BufferedReader(new FileReader("companies.txt"))) {
	
			    String line;
			    while ((line = reader.readLine()) != null) {
			        if(line.compareTo(nameField.getText()) == 0) {
			        	FirstViewController.name = line;
			        	
			    		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/distributer/resources/FirstView.fxml"));
			    		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			    		scene = new Scene(root);
			    		stage.setScene(scene);
			    		stage.show();
			    		break;
			        }
			    }
			    
			    infoLabel.setText("Company doesn't exist!");
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}
}
