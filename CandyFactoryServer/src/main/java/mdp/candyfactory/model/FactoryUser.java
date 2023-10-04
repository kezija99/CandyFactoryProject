package mdp.candyfactory.model;

public class FactoryUser {
	
	private String name;
	private String surName;
	
	public FactoryUser() {
		super();
	}
	
	public FactoryUser(String name, String surName) {
		super();
		this.name = name;
		this.surName = surName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
}
