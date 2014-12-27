package assignment3;


public class Groceries extends Item {
	
	private boolean isPerishable;
	
	/**
	 * Constructor for Grocery item.
	 * 
	 * @param name
	 * @param thePrice
	 * @param theWeight
	 * @param theTaxRate
	 * @param theQuantity
	 * @param perishability
	 */
	public Groceries(String name, double thePrice, int theQuantity, double theWeight, boolean perishability)
	{
		super(name, thePrice, theQuantity, theWeight);
		isPerishable = perishability;
		calcItemPrice();
	}
	
	/**
	 * Set perishable attribute of item. 
	 * 
	 * @param p 
	 */
	public void setIsPerishable(boolean p)
	{
		isPerishable = p;
	}
	
	/**
	 * Get perishable parameter of item.
	 * 
	 * @return isPerishable
	 */
	public boolean getIsPerishable()
	{
		return isPerishable;
	}
	
	public void calcItemPrice(){
		taxPrice = 0;
		if(isPerishable){
			shippingPrice = getQuantity()*getWeight()*24;
		}
		else
			shippingPrice = getQuantity()*getWeight()*20;

		finalPrice = taxPrice + shippingPrice + getPrice()*getQuantity();
	}
	
	/**
	 * Creates String representation of all parameters of item.
	 */
	public String toString()
	{
		String a = "Item:\t" +  getName();
		
		if(isPerishable){
			a+="\t(Perishable)";
		}
		else{
			a+="\t(Not Perishable)";
		}
		a+= "\n\tWeight:\t"+getWeight()+"\tQuantity:\t"+getQuantity() + "\tPrice:\t$"+ getPrice();
		String b = String.format("\n\t\t\tShipping:\t$%.2f\tTax:\t$%.2f\n\t\t\t\t\tTotal Cost:\t$%.2f", getShippingPrice(), getTaxPrice(), getFinalPrice());
		a+=b;
		
		return a;
	}
}
