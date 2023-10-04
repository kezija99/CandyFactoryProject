package mdp.candyfactory.distributer.rmi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//Class that implements methods from DistributerInterface which are going to be used for remote method invocation(Java RMI)
public class DistributerServer implements DistributerInterface{

	public DistributerServer() throws RemoteException{
		
	}
	
	//Reading the company names from the companies txt file inside the project folder into a list
	@Override
	public List<String> getCompanies() throws RemoteException {
		
		List<String> companies = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("companies.txt"))) {
			
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	companies.add(line);
		    }
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return companies;
	}

	//Reading the products of a company passed as a method parameter from a company txt file inside the project folder into a list
	@Override
	public List<String> getCompanyProducts(String companyName) throws RemoteException {
		
		List<String> companyProducts = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(companyName + ".txt"))) {
			
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	companyProducts.add(line);
		    }
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return companyProducts;
	}
	
	//Standard Java RMI server initialization
	public static void main(String args[]) {
		System.setProperty("java.security.policy", "server_policyfile.txt");
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		
		try {
			
			DistributerServer server = new DistributerServer();
			DistributerInterface stub = (DistributerInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("Distributer", stub);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
