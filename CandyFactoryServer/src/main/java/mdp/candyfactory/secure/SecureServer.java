package mdp.candyfactory.secure;

import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.Properties;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SecureServer {
	
	public static Properties prop;
	
	//Reading the informations from a properties file inside the project folder needed for secure connection server, such as Secure host address
	//and secure port number and keyStore fields
	public static void main(String[] args) throws Exception{
		
		prop = new Properties();
		prop.load(new FileInputStream(new File("server_config.properties")));
		
		System.setProperty("javax.net.ssl.keyStore", prop.getProperty("KEY_STORE_PATH"));
		System.setProperty("javax.net.ssl.keyStorePassword", prop.getProperty("KEY_STORE_PASSWORD"));

		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		ServerSocket ss = ssf.createServerSocket(Integer.parseInt(prop.getProperty("SECURE_PORT")));
		
		while (true) {
			SSLSocket s = (SSLSocket) ss.accept();
			new ServerThread(s).start();
		}

	}
}
