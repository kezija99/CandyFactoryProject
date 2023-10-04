package mdp.candyfactory.order.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdp.candyfactory.order.controllers.FirstViewController;

public class Main extends Application{
	
	@Override
	public void start(Stage stage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/order/resources/LoginView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

	//Initializing the application by setting the SSL trustStore fields with the info read from the property file inside the project folder and
	//setting the Message queue working queue name
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("orderApp_config.properties")));
		FirstViewController.QUEUE_NAME = prop.getProperty("QUEUE_NAME");
		System.setProperty("javax.net.ssl.trustStore", prop.getProperty("KEY_STORE_PATH"));
		System.setProperty("javax.net.ssl.trustStorePassword", prop.getProperty("KEY_STORE_PASSWORD"));
		
		launch(args);
	}

}
