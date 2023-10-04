package mdp.candyfactory.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdp.candyfactory.model.Product;
import mdp.candyfactory.repositories.ProductRepository;

public class ProductsViewController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	private ListView<String> productsList;
	
	@FXML
	private Label infoLabel;
	
	@FXML
	private TextField newName, newQuantity, updatedName, updatedQuantity;
	
	private static List<Map<String, String>> products = null;
	
	public void toFirstView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/FirstView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void addNewProduct(ActionEvent event) {
		
		if(newName.getText().length() < 1 || newQuantity.getText().length() < 1)
			infoLabel.setText("Both fields are required!");
		else
			addProduct(newName.getText(), newQuantity.getText());
	}
	
	public void addProduct(String name, String quantity) {
		
		int newProductQuantity;
		try {
			newProductQuantity = Integer.parseInt(quantity);
			Product newProduct = new Product(name, newProductQuantity);
			ProductRepository.addProduct(newProduct);
			refreshProducts();
			newName.setText("");
			newQuantity.setText("");
		}
		catch(NumberFormatException e) {
			infoLabel.setText("Quantity input error!");
		}
	}
	
	public void deleteProduct(ActionEvent event) {
		
		if(productsList.getSelectionModel().getSelectedItem() != null) {
			
			for(int i = 0; i < products.size(); i++) {
				
				Map<String, String> product = products.get(i);
			
				if(new String("Name: " + product.get("name") + " | " + "Quantity: " + product.get("quantity")).compareTo
						(productsList.getSelectionModel().getSelectedItem()) == 0) {
					ProductRepository.updateProductDb(product.get("name").toString());
					refreshProducts();
					break;
				}
			}
		}
		else
			infoLabel.setText("Pick a product to delete!");
	}
	
	public void updateProduct(ActionEvent event) {
		
		infoLabel.setText("");
		if(updatedName.getText().length() < 1)
			infoLabel.setText("You must enter the product name!");
		else {	
			String newProductName = updatedName.getText();
			int newProductQuantity;
			try {
				newProductQuantity = Integer.parseInt(updatedQuantity.getText());
				ProductRepository.updateProduct(newProductName, newProductQuantity);
				refreshProducts();
				updatedName.setText("");
				updatedQuantity.setText("");
			}
			catch(NumberFormatException e) {
				infoLabel.setText("Quantity input error!");
			}
		}	
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		refreshProducts();
	}
	
	
	public void refreshProducts() {
		
		productsList.getItems().clear();
		products = ProductRepository.getAllProducts();
		List<String> productStrings = new ArrayList<>();
		for(Map<String, String> product : products) {
			
			productStrings.add(new String("Name: " + product.get("name") + " | " + "Quantity: " + product.get("quantity")));
		}
		
		productsList.getItems().addAll(productStrings);
	}
}
