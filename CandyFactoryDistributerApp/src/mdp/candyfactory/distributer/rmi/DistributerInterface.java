package mdp.candyfactory.distributer.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DistributerInterface extends Remote{

	List<String> getCompanies() throws RemoteException;
	
	List<String> getCompanyProducts(String companyName) throws RemoteException;
}
