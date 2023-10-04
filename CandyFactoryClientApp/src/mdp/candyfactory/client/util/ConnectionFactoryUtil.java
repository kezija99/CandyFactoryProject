package mdp.candyfactory.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionFactoryUtil {

	//Method for creating a Message queue instance with credentials read from the property file inside the project folder
	public static Connection createConnection() throws IOException, TimeoutException {
		
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("clientApp_config.properties")));
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(prop.getProperty("MQ_HOST"));
		factory.setUsername(prop.getProperty("MQ_USERNAME"));
		factory.setPassword(prop.getProperty("MQ_PASSWORD"));
		
		return factory.newConnection();
	}
}
