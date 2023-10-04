package mdp.candyfactory.secure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import mdp.candyfactory.repositories.UsersRepository;

public class ServerThread extends Thread{

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private static Properties prop;
	
	private static Random rand = new Random();
	
	public ServerThread(Socket socket) {
		super();
		
		this.socket = socket;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(new File("server_config.properties")));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Checking if the desired functionality is operator login or writing the order info to a file
	@Override
	public void run() {
		try {
			String input = in.readLine();
			if(!input.startsWith("ORDER INFO")) {
				String[] inputArray = input.split(" ");
				
				if(login(inputArray[0], inputArray[1]))
					out.println("Success");
				else
					out.println("Fail");
			}
			else {
				String[] parsedInput = input.split("#");
				String folderPath = prop.getProperty("ORDERS_PATH");
				File f = new File(folderPath, "order" + rand.nextInt(100) + ".txt");
				while(f.exists()) {
					f = new File(folderPath, "order" + rand.nextInt(100) + ".txt");
				}
				f.createNewFile();
				out.println("Success");
				
				FileWriter writer = new FileWriter(f, true);
				for(String line : parsedInput) {
					writer.write(line + "\n");
				}
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Checking the factory users json file inside the projcet folder if it contains the user passed as parameters
    private static boolean login(String name, String surname) throws FileNotFoundException, IOException {
    	
    	JSONArray jsonArray = UsersRepository.readJsonArrayFromFile(prop.getProperty("FACTORY_USERS_PATH"));
    	
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            
            return false;
        }
        else
        	for(int i = 0; i < jsonArray.size(); i++) {
        		JSONObject jsonUser = (JSONObject) jsonArray.get(i);
        		if(jsonUser.get("name").toString().compareTo(name) == 0 && 
        				jsonUser.get("surname").toString().compareTo(surname) == 0)
        				return true;
        	}
        return false;
    }
}
