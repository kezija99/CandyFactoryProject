package mdp.candyfactory.gui.controllers;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mdp.candyfactory.distributer.rmi.DistributerInterface;

public class  FirstViewController{
	
	private Stage stage;
	private Scene scene;
	
	public void toUsersView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/UsersView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toProductsView(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/ProductsView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toDistributers(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/DistributersView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void showNotification(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/mdp/candyfactory/gui/resources/NotificationView.fxml"));
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("PROMOTION");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.show();
	}
}
