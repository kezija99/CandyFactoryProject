package mdp.candyfactory.gui.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

import javax.net.ssl.SSLServerSocketFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdp.candyfactory.repositories.ProductRepository;
import mdp.candyfactory.repositories.UsersRepository;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/FirstView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws IOException {
		
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("server_config.properties")));
		
		ProductRepository p = ProductRepository.getInstance();
		
		System.setProperty("java.security.policy", "client_policyfile.txt");
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		
		launch(args);
	}

}
