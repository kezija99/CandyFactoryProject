package mdp.candyfactory.services;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import mdp.candyfactory.model.Product;
import mdp.candyfactory.repositories.ProductRepository;

@Path("/products")
public class ProductService {
	
	public ProductRepository productRepo = ProductRepository.getInstance();
	
	@GET
	public Response getProducts() { 
		
		if(productRepo.productsToAdd != null) {
			return Response.status(200).entity(productRepo.getAllProducts()).build();
		}
		else
			return Response.status(500).entity("Error acquiring products.").build();
	}
	
	public static List<Map<String, String>> deleteProduct(String productKey) {
		
		for(Map<String, String> product : ProductRepository.products) {
			if(product.get("name").compareTo(productKey) == 0) {
				ProductRepository.products.remove(product);
				ProductRepository.updateProductDb(productKey);
				break;
			}
		}
		return ProductRepository.products;
	}
	
	public static List<Map<String, String>> updateProduct(String productKey, int addition) {
		
		for(Map<String, String> product : ProductRepository.products) {
			if(product.get("name").compareTo(productKey) == 0) {
				ProductRepository.updateProduct(productKey, addition);
				break;
			}
		}
		ProductRepository.products = ProductRepository.getAllProducts();
		return ProductRepository.products;
	}
	
	public static List<Map<String, String>> addProduct(String name, int quantity){
		
		Product product = new Product(name, quantity);
		ProductRepository.addProduct(product);
		
		return ProductRepository.products;
	}
}
