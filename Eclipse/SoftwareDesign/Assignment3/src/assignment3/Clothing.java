package assignment3;

/**
 * Clothing is a subclass of item.
 * No special shipping available. 
 * 
 * @author catherinebacon
 *
 */
public class Clothing extends Item{
	
	/**
	 * Constructor for Clothing item.
	 * 
	 * @param name
	 * @param thePrice
	 * @param theWeight
	 * @param theTaxRate
	 * @param theQuantity
	 */
	public Clothing(String name, double thePrice, int theQuantity, double theWeight)
	{
		super(name, thePrice, theQuantity, theWeight);
		calcItemPrice();
	}
	
	public void calcItemPrice(){
		taxPrice = (getPrice()*getQuantity()*.1);
		shippingPrice = getQuantity()*getWeight()*20;
		
		finalPrice = (taxPrice + shippingPrice + getPrice()*getQuantity());
	}
	
	
	/**
	 * Creates String representation of all parameters of item.
	 */
	public String toString()
	{
		String a = "Item:\t" +  getName() + "\n\tWeight:\t"+getWeight()+"\tQuantity:\t"+getQuantity() + "\tPrice:\t$"+ getPrice();
		String b = String.format("\n\t\t\tShipping:\t$%.2f\tTax:\t$%.2f\n\t\t\t\t\tTotal Cost:\t$%.2f", getShippingPrice(), getTaxPrice(), getFinalPrice());
		a+=b;
		return a;
	}
}
