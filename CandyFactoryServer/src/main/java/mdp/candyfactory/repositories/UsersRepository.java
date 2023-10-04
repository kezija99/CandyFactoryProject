package mdp.candyfactory.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import mdp.candyfactory.model.User;

public class UsersRepository {

    public static File usersPath;
    
    private static JSONArray jsonArray = null;
    
    public void initializePath() throws FileNotFoundException, IOException {
   
		Properties prop = new Properties();
		prop.load(new FileInputStream("C:\\Users\\Kezija\\Desktop\\DejanKezicMDP\\CandyFactoryServer\\server_config.properties"));
		usersPath = new File(prop.getProperty("USERS_PATH"));
    }

    //Parsing an user object into a json and checking the users.json file inside the project folder if the user is already existing, if not
    //the json user is being written.
    public boolean register(User user) throws FileNotFoundException, IOException {
    	initializePath();
        if (user != null) {
        	
        	Gson gson = new Gson();
            String jsonString = gson.toJson(user);
            JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
            
            jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
            if (jsonArray == null) {
                jsonArray = new JSONArray();
                
                jsonArray.add(jsonObject);
            }
            else
            	for(int i = 0; i < jsonArray.size(); i++) {
            		JSONObject jsonUser = (JSONObject) jsonArray.get(i);
            		if(jsonUser.get("password").toString().compareTo(jsonObject.get("password").toString()) == 0 && 
            				jsonUser.get("username").toString().compareTo(jsonObject.get("username").toString()) == 0)
            			return false;
            	}
    		jsonArray.add(jsonObject);

            writeJsonArrayToFile(jsonArray, usersPath.getAbsolutePath());

            return true;
        }
        return false;
    }
    
    //Checking the users.json file inside the project folder if the json parsed user is existing
    public boolean login(String username, String password) throws FileNotFoundException, IOException {
    	initializePath();
    	JSONArray jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
    	
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            
            return false;
        }
        else
        	for(int i = 0; i < jsonArray.size(); i++) {
        		JSONObject jsonUser = (JSONObject) jsonArray.get(i);
        		if(jsonUser.get("password").toString().compareTo(password) == 0 && 
        				jsonUser.get("username").toString().compareTo(username) == 0){
        			if(jsonUser.get("status").toString().compareTo("active") == 0)
        				return true;
        		}
        	}
        return false;
    }
    
    //Reading all users from the users.json file inside the project folder and displaying them in users gui in specific string format
    public static List<String> getAllUsers(){
    	
    	if(usersPath == null) {
    		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("server_config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usersPath = new File(prop.getProperty("USERS_PATH"));
    	}
    	List<String> users = new ArrayList<>();
    	
    	jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
        if (jsonArray == null) 
        	return users;
        
    	for(int i = 0; i < jsonArray.size(); i++) {
    		JSONObject jsonUser = (JSONObject) jsonArray.get(i);
    		
    		String user = "Username: " + jsonUser.get("username").toString() + " | " + "Company name: " + jsonUser.get("companyName").toString()
    		+ " | " + "Phone number: " + jsonUser.get("phoneNumber").toString() + " | " + "Address: " + jsonUser.get("adress").toString()
    		+ " | " + "Status: " + jsonUser.get("status").toString();
    		
    		users.add(user);
    	}
    	
    	return users;
    }
    
    //Changing the user status field in the users.json file inside the project folder
    public static boolean approveRequest(int i) {
    	if(usersPath == null) {
    		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("server_config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usersPath = new File(prop.getProperty("USERS_PATH"));
    	}
    	jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
    	JSONObject pickedUser = (JSONObject) jsonArray.get(i);
    	if("pending".compareTo(pickedUser.get("status").toString()) == 0) {
    		pickedUser.put("status", "active");
    		writeJsonArrayToFile(jsonArray, usersPath.getAbsolutePath());
    		return true;
    	}
    	else
    		return false;
    }
    
    //Changing the user status field in the users.json file inside the project folder
    public static boolean denyRequest(int i) {
    	if(usersPath == null) {
    		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("server_config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usersPath = new File(prop.getProperty("USERS_PATH"));
    	}
    	jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
    	JSONObject pickedUser = (JSONObject) jsonArray.get(i);
    	if("pending".compareTo(pickedUser.get("status").toString()) == 0) {
    		jsonArray.remove(i);
    		writeJsonArrayToFile(jsonArray, usersPath.getAbsolutePath());
    		return true;
    	}
    	else
    		return false;
    }
    
    //Deleting the user from the users.json file inside the project folder
    public static boolean removeUser(int i) {
    	if(usersPath == null) {
    		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("server_config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usersPath = new File(prop.getProperty("USERS_PATH"));
    	}
    	jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
    	JSONObject pickedUser = (JSONObject) jsonArray.get(i);
    	if("active".compareTo(pickedUser.get("status").toString()) == 0 ||
    			"blocked".compareTo(pickedUser.get("status").toString()) == 0) {
    		jsonArray.remove(i);
    		writeJsonArrayToFile(jsonArray, usersPath.getAbsolutePath());
    		return true;
    	}
    	else
    		return false;
    }
    
    //Changing the user status field in the users.json file inside the project folder
    public static boolean blockUser(int i) {
    	if(usersPath == null) {
    		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File("server_config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			usersPath = new File(prop.getProperty("USERS_PATH"));
    	}
    	jsonArray = readJsonArrayFromFile(usersPath.getAbsolutePath());
    	JSONObject pickedUser = (JSONObject) jsonArray.get(i);
    	if("active".compareTo(pickedUser.get("status").toString()) == 0) {
    		pickedUser.put("status", "blocked");
    		writeJsonArrayToFile(jsonArray, usersPath.getAbsolutePath());
    		return true;
    	}
    	else
    		return false;
    }
    
    public static JSONArray readJsonArrayFromFile(String fileName) {
        try (FileReader fileReader = new FileReader(fileName)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void writeJsonArrayToFile(JSONArray jsonArray, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
