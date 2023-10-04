package mdp.candyfactory.client.model;

import java.util.HashMap;

public class Product {
	
	private String name;
	private int quantity;
	
	public Product() {
		super();
	}
	
	public Product(String name, int quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String toString() {
		
		return "Product " + name + ", quantity: " + String.valueOf(quantity);
	}
	
	public HashMap<String, String> toMap(){
		HashMap<String, String> obj = new HashMap<>();
		obj.put("name", name);
		obj.put("quantity", String.valueOf(quantity));
		return obj;
	}
}
