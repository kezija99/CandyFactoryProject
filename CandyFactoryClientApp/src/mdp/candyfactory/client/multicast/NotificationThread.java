package mdp.candyfactory.client.multicast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.Properties;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mdp.candyfactory.client.controllers.NotificationViewController;

public class NotificationThread extends Thread{
	
	//Thread for listening the multicast messages comming from the main Factory application. Both port and address are read from the
	//property file inside the projects folder
	public void run() {
		
		Properties prop;
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("clientApp_config.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		MulticastSocket socket = null;
		byte[] buf = new byte[256];
		try {
			socket = new MulticastSocket(Integer.parseInt(prop.getProperty("NOTIFICATION_PORT")));
			InetAddress address = InetAddress.getByName(prop.getProperty("NOTIFICATION_ADDRESS"));
			SocketAddress socketAddress = new InetSocketAddress(address, Integer.parseInt(prop.getProperty("NOTIFICATION_PORT")));
			NetworkInterface networkInterface = getFirstNonLoopbackInterface();;
			socket.joinGroup(socketAddress, networkInterface);
			while(true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/mdp/candyfactory/client/resources/NotificationView.fxml"));
				Parent root = loader.load();
				NotificationViewController nvc = loader.getController();
				Platform.runLater(() -> {
					nvc.setContent(received);
			        Stage popupStage = new Stage();
			        popupStage.initModality(Modality.APPLICATION_MODAL);
			        popupStage.setTitle("PROMOCIJA");
			        Scene scene = new Scene(root);
			        popupStage.setScene(scene);
			        popupStage.show();
				});
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    private static NetworkInterface getFirstNonLoopbackInterface() throws IOException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                return networkInterface;
            }
        }
        throw new IOException("No non-loopback network interface available.");
    }
}
