import java.util.HashMap;
import java.util.Iterator;

public class HashMapDemo {

	public static void main(String[] args) {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		
		//adding values to a HashMap uses the put<<K>,<V>> method
		hm.put("Bran", 3434.34); 
		hm.put("Jon", 123.22); 
		hm.put("Sansa", 1378.00); 
		hm.put("Arya", 99.22); 
		hm.put("Robb", 2219.08); 
		
		
		//iterating through a hashmap does not guarantee order
		Iterator<String> it = hm.keySet().iterator();
		
		while(it.hasNext()){
			String key = it.next();
			
			System.out.println("Key: " + key + "\tValue: " + hm.get(key));
		}
		
		//putting another element into the hashmap replaces the Value
		hm.put("Robb", 0.);
		System.out.println("\nRobb: " + hm.get("Robb"));
		
		//trying to get a key that doesn't exist returns a null
		System.out.println("\nhello: " + hm.get("hello"));
	}

}
