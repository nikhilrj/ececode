package assignment3;

/**
 * Abstract Class that provides the requirements and functionality of objects in the Cart.
 * 
 * Attributes include price, weight, quantity, name.
 * Methods include calculating price, comparison, and getters and setters for each attribute.
 * 
 * @author Nikhil Joglekar
 *
 */
public abstract class Item implements Comparable<Item> {
	
	String name;
	private double price;
	private double weight;
	int quantity;
	double shippingPrice;
	double taxPrice;
	double finalPrice;
	

	/**
	 * Constructor for Item. 
	 * 
	 * @param theName
	 * @param thePrice
	 * @param theWeight
	 * @param theTaxRate
	 * @param theQuantity
	 */
	public Item(String theName, double thePrice, int theQuantity, double theWeight){
		name = theName;
		price = thePrice;
		weight = theWeight;
		quantity = theQuantity;
	}

	abstract void calcItemPrice();
	
	public String toString(){
		return name;
	}
	
	/**
	 * Compares Items by name (lexicographically).
	 * Uses String.compareTo(String s)
	 * 
	 * @param t Item to compare against
	 * @return -1 t.name is lexicographically after, 0 if identical, 1 if t.name before
	 */
	public int compareTo(Item t)
	{
		return name.compareTo(t.name);
	}
	
	/**
	 * Compares Item to name of item.
	 * 
	 * @param itemName
	 * @return -1,0,1 based on lexicographical comparison of name and itemName
	 */
	public int compareTo(String itemName)
	{
		return name.compareTo(itemName);
	}
	
	public double getShippingPrice() {
		return shippingPrice;
	}

	public double getTaxPrice() {
		return taxPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}
	
	/**
	 * Get name attribute of Item.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of Item.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set price of Item. 
	 * 
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Set price of item.
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Get weight of item.
	 * 
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set weight of item. 
	 * 
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Get quantity of item. 
	 * 
	 * @return quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Set quantity of item. 
	 * 
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		calcItemPrice();
	}
}
