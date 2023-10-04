package mdp.candyfactory.client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdp.candyfactory.client.model.Order;
import mdp.candyfactory.client.model.Product;
import mdp.candyfactory.client.util.ConnectionFactoryUtil;

public class OrderViewController implements Initializable{
	
	private static String QUEUE_NAME;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label infoLabel, quantityLabel;
	
	@FXML
	private TextField quantityField, emailField;
	
	@FXML
	private ChoiceBox<String> productBox;
	
	@FXML
	private ListView<String> orderList;
	
	private String pickedProductName = "";
	public static List<Map<String, String>> products;
	private List<Map<String, String>> order = new ArrayList<>();
	private List<String> stringOrder = new ArrayList<>();
	private List<Product> orderProducts = new ArrayList<>();
	
	//Get the Message Queue name from the property file inside the project folder and populate the product choice box with latest products
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Properties prop;
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("clientApp_config.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		QUEUE_NAME = prop.getProperty("QUEUE_NAME");
		refresh();
	}
	
	//Update the order list view with latest added product and update the product choice box
	public void refresh() {
		
		productBox.getItems().clear();
		orderList.getItems().clear();
		
		if(order.size() > 0) {
			orderList.getItems().addAll(stringOrder);
		}
		
		List<String> productNames = new ArrayList<>();
		
		for(Map<String, String> product : products) {
			productNames.add(product.get("name"));
		}
		
		productBox.getItems().addAll(productNames);
		productBox.setOnAction(this::getPickedProduct);
	}
	
	//Adding a product to order by removing it from the list of available products and updating the order list
	public void addProduct(ActionEvent event) {
		
		if(pickedProductName == null)
			infoLabel.setText("Product has to be picked!");
		else if("".compareTo(quantityField.getText()) == 0)
			infoLabel.setText("Quantity has to be entered!");
		else {
			int productQuantity = Integer.parseInt(quantityLabel.getText());
			int newQuantity = Integer.parseInt(quantityField.getText());
			if(newQuantity > productQuantity || newQuantity < 1)
				infoLabel.setText("Quantity input error!");
			else {
				Product orderProduct = new Product(pickedProductName, newQuantity);
				Product oldProduct = new Product(pickedProductName, productQuantity);
				order.add(orderProduct.toMap());
				stringOrder.add("Name: " + pickedProductName + " | " + "Quantity: " + quantityField.getText());
				orderProducts.add(orderProduct);
				products.remove(oldProduct.toMap());
				refresh();
			}
		}
		
	}
	
	//Method for getting the picked product info and displaying its quantity on gui
	public void getPickedProduct(ActionEvent event) {
		
		pickedProductName = productBox.getValue();
		if(pickedProductName != null) {
			for(int i = 0; i < products.size(); i++) {
				if(products.get(i).get("name").compareTo(pickedProductName) == 0)
					quantityLabel.setText(products.get(i).get("quantity"));
			}
		}
	}
	
	//Sending the order to a Message queue after creating an order xml document using Jakarta XML binding api. Root element is the Order itself while
	//children fields are products and an email. After the order is sent on the queue, firstView is opened again. 
	public void finishOrder(ActionEvent event) throws IOException, TimeoutException, JAXBException {
		
		if(emailField.getText().length() < 10 || !emailField.getText().endsWith("@mail.com"))
			infoLabel.setText("Please insert valid e-mail!");
		else {
			JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Order rootElement = new Order();
            if(orderProducts.size() > 0) {
	            rootElement.setProducts(orderProducts);
	            rootElement.setUserMail(emailField.getText());
	            
	            StringWriter stringWriter = new StringWriter();
	            marshaller.marshal(rootElement, stringWriter);
	            String xmlString = stringWriter.toString();
	            
				Connection conn = ConnectionFactoryUtil.createConnection();
				Channel channel = conn.createChannel();
				
				channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				channel.basicPublish("", QUEUE_NAME, null, xmlString.getBytes(StandardCharsets.UTF_8));
				
				channel.close();
				conn.close();
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/FirstView.fxml"));
					stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (Exception e) {
				    e.printStackTrace();
				}
            }
            else
            	infoLabel.setText("Order cannot be empty!");
		}
	}
	
	public void toFirstView(ActionEvent event) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/FirstView.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
