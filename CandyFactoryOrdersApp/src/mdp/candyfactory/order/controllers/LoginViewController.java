package mdp.candyfactory.order.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController{

	private Stage stage;
	private Scene scene;
	
	@FXML
	TextField name, surname;
	
	@FXML
	Label infoLabel;
	
	//Reading the informations from a properties file inside the project folder needed for secure connection login, such as Secure host address
	//and secure port number.
	public void login(ActionEvent event) throws UnknownHostException, IOException {
		
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("orderApp_config.properties")));
		if(name.getText().length() < 1 || surname.getText().length() < 1)
			infoLabel.setText("Both fields are required!");
		else {
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			String response = "";
			try {
				SSLSocket s = (SSLSocket) sf.createSocket(prop.getProperty("HOST"), Integer.parseInt(prop.getProperty("SECURE_PORT")));
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			
				out.println(name.getText() + " " + surname.getText());
				
				response = in.readLine();
				
				in.close();
				out.close();
				s.close();
				
				if("Success".compareTo(response) == 0) {
					
					Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/order/resources/FirstView.fxml"));
					stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
				else
					infoLabel.setText("Operator not found!");
			}
			catch(Exception e) {
				infoLabel.setText("Secure server connection failure");
			}

		}
	}
}