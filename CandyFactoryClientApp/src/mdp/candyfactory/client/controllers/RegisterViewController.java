package mdp.candyfactory.client.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

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
import mdp.candyfactory.client.model.User;

public class RegisterViewController {
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	Label infoLabel;
	
	@FXML
	TextField companyName, adress, phoneNumber, userName;
	
	@FXML
	PasswordField pass, pass2;
	
	//Registration by calling the REST endpoint if all fields are valid
	public void register(ActionEvent event) throws Exception {
		
		String company = companyName.getText();
		String address = adress.getText();
		String phone = phoneNumber.getText();
		String username = userName.getText();
		String password = pass.getText();
		String password2 = pass2.getText();
		
		if(company.length() < 1 || address.length() < 1 || phone.length() < 1 || username.length() < 1 
				|| password.length() < 1 || password2.length() < 1) 
			
			infoLabel.setText("All fields are required!");
		else {	
			if(password.compareTo(password2) == 0) {
				
				URL url = new URL(Main.PATH + "/users/register");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
				
				User user = new User(username, password, company, address, phone, "pending");
				
				Gson gson = new Gson();
		        String jsonUser =  gson.toJson(user);
				
		        OutputStream outputStream = conn.getOutputStream();
	            outputStream.write(jsonUser.getBytes("UTF-8"));
	            outputStream.close();
	            
	            int statusCode = conn.getResponseCode();
	            
	            if(statusCode == 200) {
	            	
	            	infoLabel.setText("Registration successful!");
	            }
	            else
	            	infoLabel.setText("Registration unsuccessful!");
			}
			else
				infoLabel.setText("Passwords do not match!");
		}
			
	}
	
	public void toLoginView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/LoginView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
