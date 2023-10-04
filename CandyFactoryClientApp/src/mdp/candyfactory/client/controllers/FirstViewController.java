package mdp.candyfactory.client.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import mdp.candyfactory.client.main.Main;

public class FirstViewController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label infoLabel;
	
	@FXML
	private ListView productsList;
	
	public static List<Map<String, String>> products = null;
	
	public void toOrderView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/OrderView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		refreshProducts();
	}
	
	//Retrieve all available products by calling REST endpoint using HttpURLConnection class and update the gui-s list view
	public void refreshProducts() {
		
		productsList.getItems().clear();
		
		List<String> stringProducts = new ArrayList<>();
		try {
			URL url = new URL(Main.PATH + "/products");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        
	        int statusCode = conn.getResponseCode();
	        if (statusCode == HttpURLConnection.HTTP_OK) {
	        	
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            StringBuilder response = new StringBuilder();
	            
	            while ((line = reader.readLine()) != null) {
	                response.append(line);
	            }
	            
	            reader.close();
	            
	            Gson gson = new Gson();
                List<Map<String, String>> products = gson.fromJson(response.toString(),
                        new TypeToken<List<Map<String, String>>>() {}.getType());
                
                OrderViewController.products = gson.fromJson(response.toString(),
                        new TypeToken<List<Map<String, String>>>() {}.getType());;
                
                for (Map<String, String> product : products) {;
                	stringProducts.add(new String("Name: " + product.get("name") + " | " + "Quantity: " + product.get("quantity")));
                }
	        } 
	        else
	            infoLabel.setText("Error retrieving products!");
			}
		catch(Exception e) {
			e.printStackTrace();
		}
        
		productsList.getItems().addAll(stringProducts);
	}
}
