package mdp.candyfactory.model;

public class User {
	
	private String username;
	private String password;
	private String companyName;
	private String adress;
	private String phoneNumber;
	private String status;
	
	public User() {
		super();
	}
	
	public User(String username, String password, String companyName, String adress, String phoneNumber
			, String status) {
		super();
		this.username = username;
		this.password = password;
		this.companyName = companyName;
		this.adress = adress;
		this.phoneNumber = phoneNumber;
		this.status = status;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
		
		return "Korisnicko ime: " + username + " | " + "Ime kompanije: " + companyName
	    		+ " | " + "Broj telefona: " + phoneNumber + " | " + "Adresa: " + adress
	    		+ " | " + "Status: " + status;
	}
}
