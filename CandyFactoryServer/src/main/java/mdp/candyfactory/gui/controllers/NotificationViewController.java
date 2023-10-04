package mdp.candyfactory.gui.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mdp.candyfactory.services.NotificationService;

public class NotificationViewController {

	@FXML
	TextField contentField;
	
	@FXML
	Label infoLabel;
	
	public void send(ActionEvent event) throws FileNotFoundException, IOException {
		
		if(contentField.getText().length() < 5) {
			infoLabel.setText("Promotion text too short!");
		}
		else {
			NotificationService.sendMulticast(contentField.getText());
			infoLabel.setText("Promotion successfuly sent!");
		}
	}
	
}
