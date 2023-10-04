package mdp.candyfactory.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import mdp.candyfactory.model.Product;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class ProductRepository {
	
	private static ProductRepository instance = null;
	
	public static String instanceName;
	public static String redisHost;
	public List<Product> productsToAdd = new ArrayList<>();
	public static List<Map<String, String>> products;
	private Properties prop;
	
	//Generating 90 products used for initializing the reddis db for the first use of the application
	public void generateProducts() {
		
		try (Jedis jedis = new Jedis(redisHost);) {
        	jedis.select(0);
        	jedis.del(instanceName);
        }
		
		for(int i = 10; i < 100; i++)
			productsToAdd.add(new Product("product " + i, i + 15));
	}
	
	public static ProductRepository getInstance() {
		
		if(instance == null)
			instance = new ProductRepository();
		return instance;
	}
	
	//Reading the property file inside the project folder and setting the Redis db required fileds
	public ProductRepository() {
		
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("C:\\Users\\Kezija\\Desktop\\DejanKezicMDP\\CandyFactoryServer\\server_config.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		instanceName = prop.getProperty("REDIS_INSTANCE_NAME");
		redisHost = prop.getProperty("REDIS_HOST");
		initializeProductDb();
	}
	
    public static List<Map<String, String>> getAllProducts() {
    	
    	List<Map<String, String>> productsRefreshed = new ArrayList<>();
    	
        try (Jedis jedis = new Jedis(redisHost);) {
            jedis.select(0);
            String cursor = ScanParams.SCAN_POINTER_START;
            ScanParams scanParams = new ScanParams().count(200);

            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                List<String> keys = scanResult.getResult();

                for (String key : keys) {
                    String type = jedis.type(key);

                    if ("hash".equals(type)) {
                    	Map<String, String> fields = jedis.hgetAll(key);
                        productsRefreshed.add(fields);
                     }
                }

             cursor = scanResult.getStringCursor();
             } while (!cursor.equals(ScanParams.SCAN_POINTER_START));
        }
        products = productsRefreshed;
        return products;
    }
	
    //Initializing reddis db instance with a 90 generated products that will be displayed in the first start of the application
	public void initializeProductDb(){
		
		generateProducts();
		
		Jedis jedis = new Jedis(redisHost);
		jedis.select(0);
			
		for(Product p : productsToAdd) {
			jedis.hmset(instanceName + ":products:map:" + p.getName(), p.toMap());
		}

		jedis.close();
	}
	
	public static void updateProductDb(String productKey) {
	
            try (Jedis jedis = new Jedis(redisHost);) {
            	jedis.select(0);
            	jedis.del(instanceName + ":products:map:" + productKey);
            }
	}
	
	public static void updateProduct(String productKey, int addition) {
		
		try (Jedis jedis = new Jedis(redisHost);) {
        	jedis.select(0);
        	Map<String, String> productMap = jedis.hgetAll(instanceName + ":products:map:" + productKey);
        	jedis.del(instanceName + ":products:map:" + productKey);
        	int productQuantity = Integer.parseInt(productMap.get("quantity"));
        	int newProductQuantity = productQuantity + addition;
        	productMap.put("quantity", String.valueOf(newProductQuantity));
        	jedis.hmset(instanceName + ":products:map:" + productKey, productMap);
		}
	}
	
	public static void addProduct(Product product) {
		
		try (Jedis jedis = new Jedis(redisHost);) {
			jedis.select(0);
			
			jedis.hmset(instanceName + ":products:map:" + product.getName(), product.toMap());
		}
	}
}
