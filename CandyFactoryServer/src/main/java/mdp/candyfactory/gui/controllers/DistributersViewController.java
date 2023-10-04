package mdp.candyfactory.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdp.candyfactory.distributer.rmi.DistributerInterface;
import mdp.candyfactory.model.Product;
import mdp.candyfactory.repositories.ProductRepository;

public class DistributersViewController implements Initializable{

	private Stage stage;
	private Scene scene;
	
	@FXML
	ListView<String> companiesList, productsList;
	
	@FXML
	Label infoLabel;
	
	@FXML
	TextField productQuantity;

	//Initializing the companies list view by getting the list of companies by calling the method from RMI interface defined in Distributer application
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			Registry registry = LocateRegistry.getRegistry(1099);
			DistributerInterface distributer = (DistributerInterface) registry.lookup("Distributer");
			
			List<String> companies = distributer.getCompanies();
			companiesList.getItems().addAll(companies);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Displaying the list of products of a picked company by calling the method from RMI interface defined in Distributer application
	public void showProducts(ActionEvent event) {
		
		if(companiesList.getSelectionModel().getSelectedItem() == null)
			infoLabel.setText("You must pick a company!");
		else {
			productsList.getItems().clear();
			try {
				Registry registry = LocateRegistry.getRegistry(1099);
				DistributerInterface distributer = (DistributerInterface) registry.lookup("Distributer");
				
				List<String> companyProducts = distributer.getCompanyProducts(companiesList.getSelectionModel().getSelectedItem());
				productsList.getItems().addAll(companyProducts);
				infoLabel.setText("");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void buyProduct(ActionEvent event) {
		
		if(productsList.getSelectionModel().getSelectedItem() == null)
			infoLabel.setText("You must pick a product!");
		else if(productQuantity.getText().length() < 1)
			infoLabel.setText("Quantity field is required!");
		else {
			addProduct(productsList.getSelectionModel().getSelectedItem(), productQuantity.getText());
			infoLabel.setText("Product successfuly bought!");
			productQuantity.setText("");
		}
	}
	
	public void addProduct(String name, String quantity) {
		
		int newProductQuantity;
		try {
			newProductQuantity = Integer.parseInt(quantity);
			Product newProduct = new Product(name, newProductQuantity);
			ProductRepository.addProduct(newProduct);
		}
		catch(NumberFormatException e) {
			infoLabel.setText("Neispravan unos kolicine!");
		}
	}
	
	public void toFirstView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/FirstView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
