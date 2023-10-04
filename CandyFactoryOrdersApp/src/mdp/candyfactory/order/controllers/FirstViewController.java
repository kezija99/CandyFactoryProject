package mdp.candyfactory.order.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import mdp.candyfactory.order.util.ConnectionFactoryUtil;

public class FirstViewController {
	
	public static String QUEUE_NAME;
	
	@FXML
	Label mailLabel;
	
	@FXML
	ListView<String> orderList;
	
	private List<String> stringOrder = new ArrayList<>();
	private String username, password;
	
	//Opening a connection to a Message queue server and accessing the working queue. Getting the order which is sent longest time ago. Parsing the
	//xml document into a list of products containing the order and displaying them on gui.
	public void takeOrder(ActionEvent event) throws IOException, TimeoutException {
		Connection conn = ConnectionFactoryUtil.createConnection();
		Channel channel = conn.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		GetResponse response = channel.basicGet(QUEUE_NAME, false);
		if (response != null) {
		    try {
		        String xmlContent = new String(response.getBody(), StandardCharsets.UTF_8);
		        
		        try {
		            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		            DocumentBuilder builder = factory.newDocumentBuilder();

		            InputSource inputSource = new InputSource(new StringReader(xmlContent));

		            Document document = builder.parse(inputSource);

		            Element root = document.getDocumentElement();
		            String email = root.getElementsByTagName("MAIL").item(0).getTextContent();
		            mailLabel.setText(email);
		            NodeList userList = root.getElementsByTagName("PRODUCT");

		            for (int i = 0; i < userList.getLength(); i++) {
		                Node userNode = userList.item(i);
		                if (userNode.getNodeType() == Node.ELEMENT_NODE) {
		                    Element userElement = (Element) userNode;
		                    String productName = userElement.getElementsByTagName("name").item(0).getTextContent();
		                    String quantity = userElement.getElementsByTagName("quantity").item(0).getTextContent();
		                    stringOrder.add("Naziv: " + productName + " | " + "Kolicina: " + quantity);
		                }
		            }
		            orderList.getItems().addAll(stringOrder);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			channel.close();
			conn.close();
		}
	}
	
	public void approveOrder(ActionEvent event) throws FileNotFoundException, IOException {
		
		if(stringOrder.size() > 0)
			orderInfo(true);
		
		mailLabel.setText("");
		orderList.getItems().clear();
	}
	
	public void denyOrder(ActionEvent event) throws UnknownHostException, IOException {
		
		if(stringOrder.size() > 0)
			orderInfo(false);
		mailLabel.setText("");
		orderList.getItems().clear();
	}
	
	//Creating the order info txt file after processing the order using the secure connection as it was done with the login. Every order info
	//contains the sender e-mail, list of products and the order status (approved / rejected)
	private void orderInfo(boolean approved) throws UnknownHostException, IOException {
		
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("orderApp_config.properties")));
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket s = (SSLSocket) sf.createSocket(prop.getProperty("HOST"), Integer.parseInt(prop.getProperty("SECURE_PORT")));

		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
	
		String info = "ORDER INFO:#---------------------------------#ORDER SENDER:" + mailLabel.getText() + "#"
				+ "---------------------------------#";
		
		for(int i = 0; i < stringOrder.size(); i++) {
			
			info += stringOrder.get(i) + "#";
		}
		info += "---------------------------------#";
		
		if(approved) {
			info += "ORDER STATUS: APPROVED"; 
		}
		else {
			info += "ORDER STATUSE: REJECTED"; 
		}
		
		out.println(info);
		
		String response = in.readLine();
		stringOrder.clear();
		in.close();
		out.close();
		s.close();
	}
}