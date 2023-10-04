package mdp.candyfactory.client.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdp.candyfactory.client.main.Main;

public class LoginViewController {
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	Label infoLabel;
	
	@FXML
	TextField username;
	
	@FXML
	PasswordField password;
	
	//Login attempt by calling REST endpoint
	public void login(ActionEvent event) throws IOException {
		
		String userName = username.getText();
		String pass = password.getText();

		if(userName.length() < 1 || pass.length() < 1)
			infoLabel.setText("All fields are required!");
		else {
			URL url = new URL(Main.PATH + "/users/login/" + userName + "/" + pass);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			int statusCode = conn.getResponseCode();
			
			if(statusCode == 200) {
				Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/FirstView.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
			else {
				infoLabel.setText("Login failed!");
			}

		}
	}
	
	public void toRegisterView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/RegisterView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
