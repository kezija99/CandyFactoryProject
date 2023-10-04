package mdp.candyfactory.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import mdp.candyfactory.repositories.UsersRepository;

//Class for displaying and working with user accounts. Only registered users can be deleted or blocked and only users on pending status can
//be approved or rejected.
public class UsersViewController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	private ListView<String> usersList;
	
	@FXML
	private Label infoLabel;
	
	private List<String> users = null;
	
	public void toFirstView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/FirstView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void approveRequest(ActionEvent event) {
		
		if(usersList.getSelectionModel().getSelectedItem() != null) {
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).compareTo(usersList.getSelectionModel().getSelectedItem()) == 0) {
					if(UsersRepository.approveRequest(i)) {
						refreshUsers();
						break;
					}
					else {
						infoLabel.setText("Picked user is not on pending status!");
						break;
					}
				}
			}
		}
	}
	
	public void denyRequest(ActionEvent event) {
		
		if(usersList.getSelectionModel().getSelectedItem() != null) {
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).compareTo(usersList.getSelectionModel().getSelectedItem()) == 0) {
					if(UsersRepository.denyRequest(i)) {
						refreshUsers();
						break;
					}
					else {
						infoLabel.setText("Picked user is not on pending status!");
						break;
					}
				}
			}
		}
	}
	
	public void removeUser(ActionEvent event) {
		
		if(usersList.getSelectionModel().getSelectedItem() != null) {
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).compareTo(usersList.getSelectionModel().getSelectedItem()) == 0) {
					if(UsersRepository.removeUser(i)) {
						refreshUsers();
						break;
					}
					else {
						infoLabel.setText("Picked user is not registered!");
						break;
					}
				}
			}
		}
	}
	
	public void blockUser(ActionEvent event) {
		
		if(usersList.getSelectionModel().getSelectedItem() != null) {
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).compareTo(usersList.getSelectionModel().getSelectedItem()) == 0) {
					if(UsersRepository.blockUser(i)) {
						refreshUsers();
						break;
					}
					else {
						infoLabel.setText("Picked user is not registered!");
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		refreshUsers();
	}
	
	public void refreshUsers() {
		
		usersList.getItems().clear();
		users = UsersRepository.getAllUsers();
		usersList.getItems().addAll(users);
	}
}
