package mdp.candyfactory.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.Properties;


public class NotificationService {
	
	//Sending the multicast promotion message to a multicast address and port read from the property file inside the projcet folder
	public static void sendMulticast(String msg) throws FileNotFoundException, IOException {
		
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("server_config.properties")));
		MulticastSocket socket = null;
		byte[] buf = new byte[256];
		try {
			socket = new MulticastSocket(Integer.parseInt(prop.getProperty("NOTIFICATION_PORT")));
			InetAddress address = InetAddress.getByName(prop.getProperty("NOTIFICATION_ADDRESS"));
			SocketAddress socketAddress = new InetSocketAddress(address, Integer.parseInt(prop.getProperty("NOTIFICATION_PORT")));
			NetworkInterface networkInterface = getFirstNonLoopbackInterface();;
			socket.joinGroup(socketAddress, networkInterface);
			
			buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(prop.getProperty("NOTIFICATION_PORT")));
			socket.send(packet);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Helper method needed for sending the multicast
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
