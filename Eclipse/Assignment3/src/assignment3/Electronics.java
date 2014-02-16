package assignment3;


public class Electronics extends Item {
	
	private boolean isFragile;
	private String stateOfTransaction;
	boolean taxable;
	private final String[] noTaxStates = {"TX", "NM", "VA", "AZ", "AK"};
	
	public Electronics(String name, double thePrice, int theQuantity, double theWeight, boolean fragility, String state)
	{
		super(name, thePrice, theQuantity, theWeight);
		isFragile = fragility;
		stateOfTransaction = state;
		
		taxable = true;
		for(String s : noTaxStates){
			if(s.equals(state))
				taxable = false;
		}
		
		calcItemPrice();
	}
	
	
	public void calcItemPrice(){
		if(taxable){
			taxPrice = getQuantity()*getPrice()*.1;
		}
		else {
			taxPrice = 0;
		}
		
		if(isFragile){
			shippingPrice = getQuantity()*getWeight()*24;
		}
		else{
			shippingPrice = getQuantity()*getWeight()*20;
		}
		
		finalPrice = shippingPrice + taxPrice + getPrice()*getQuantity();
	}
	
	
	/**
	 * Creates String representation of all parameters of item.
	 */
	public String toString()
	{
		String a = "Item:\t" +  getName();
		
		if(isFragile){
			a+="\t(Fragile)";
		}
		else{
			a+="\t(Not Fragile)";
		}
		a+="\tState:\t"+stateOfTransaction;
		a+= "\n\tWeight:\t"+getWeight()+"\tQuantity:\t"+getQuantity() + "\tPrice:\t$"+ getPrice();
		String b = String.format("\n\t\t\tShipping:\t$%.2f\tTax:\t$%.2f\n\t\t\t\t\tTotal Cost:\t$%.2f", getShippingPrice(), getTaxPrice(), getFinalPrice());
		a+=b;
		
		return a;
	}
	
	boolean checkValidState(){
		if(stateOfTransaction.length()==2){
			if(stateOfTransaction.charAt(0)>=65 && stateOfTransaction.charAt(0)<=90)
				if(stateOfTransaction.charAt(1)>=65 && stateOfTransaction.charAt(1)<=90)
					return true;
		}
		return false;
	}

	/**
	 * Get fragility parameter of item
	 * 
	 * @return isFragile
	 */
	public boolean getIsFragile() {
		return isFragile;
	}

	/**
	 * Set fragile status for item
	 * 
	 * @param isFragile
	 */
	public void setFragile(boolean isFragile) {
		this.isFragile = isFragile;
	}

	/**
	 * Get state item is being shipped to.
	 * 
	 * @return stateOfTransaction
	 */
	public String getStateOfTransaction() {
		return stateOfTransaction;
	}

	/**
	 * Set state item is being shipped to. 
	 * 
	 * @param state
	 */
	public void setStateOfTransaction(String state) {
		stateOfTransaction = state;
	}
}
