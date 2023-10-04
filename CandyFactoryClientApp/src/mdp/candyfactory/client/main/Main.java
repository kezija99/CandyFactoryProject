package mdp.candyfactory.client.main;

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
import mdp.candyfactory.client.multicast.NotificationThread;

public class Main extends Application {
	
	public static String PATH;
	@Override
	public void start(Stage stage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/client/resources/LoginView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}
	
	//Getting the main url of the REST server from the BASE_URL field in property file inside the projects folder and starting the app
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("clientApp_config.properties")));
		PATH = prop.getProperty("BASE_URL");
		NotificationThread nt = new NotificationThread();
		nt.start();
		launch(args);
	}
}
