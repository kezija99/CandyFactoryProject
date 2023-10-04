package mdp.candyfactory.distributer.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class FirstViewController implements Initializable{

	@FXML
	ListView<String> productList;
	
	@FXML
	Label infoLabel, companyName;
	
	@FXML
	TextField productField;
	
	public static String name;
	private static List<String> products = new ArrayList<>();
	
	//Adding a new product for logged company by writing it to a company txt file inside the projects folder
	public void addNewProduct(ActionEvent event) {
		
		if(productField.getText().length() < 1)
			infoLabel.setText("Product name is required!");
		else if(products.contains(productField.getText()))
			infoLabel.setText("Product already exists!");
		else {
			File f = new File(name + ".txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, true))) {
				if(f.length() != 0)
					writer.newLine();
				writer.write(productField.getText());
				writer.flush();
				products.add(productField.getText());
				infoLabel.setText("New product successfuly added!");
				refresh();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}

	//Displaying companies products in gui after reading them from the company txt file inside the projects folder
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		companyName.setText(name);
		File f = new File(name + ".txt");
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try (BufferedReader reader = new BufferedReader(new FileReader(name + ".txt"))) {

		    String line;
		    while ((line = reader.readLine()) != null) {
		        products.add(line);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		productList.getItems().addAll(products);
	}
	
	//Updating the products list view on gui
	public void refresh() {
		
		productList.getItems().clear();
		
		productList.getItems().addAll(products);
	}
}
