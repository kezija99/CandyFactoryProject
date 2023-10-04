package mdp.candyfactory.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NotificationViewController {

	@FXML
	Label textLabel;
	
	//Method which sets the content of a popup window triggered by the multicast message
	public void setContent(String content) {
		
		textLabel.setText(content);
	}
}
