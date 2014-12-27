package assignment3;

import java.util.*;

/**
 * Performs operations on cart and calculates shipping, tax, and total price of items in cart.
 * Allowable operations include: 
 * Add item, remove item, search for item, update item and view cart.
 * Items in cart held in ArrayList<Item> contents.
 *  
 * @author Kevin Nguyen, 422c
 * 
 */

public class ShoppingCart 
{

	public ArrayList<Item> contents;
	/**
	 * Constructor for cart, initializes attributes of cart.
	 */
	public ShoppingCart() 
	{
		contents = new ArrayList<Item>();
	}

	/**
	 * Adds specified item to cart. 
	 * 
	 * @param newItem
	 * @return true if add was successful, false if not successful
	 * (Postcondition: newItem added to contents)
	 */
	public boolean addItem(Item newItem) 
	{
		return contents.add(newItem);
	}

	/**
	 * Deletes specified item from cart. 
	 * 
	 * @param delItem
	 * (Postcondition: delItem removed from contents)
	 */
	public void deleteItem(String delItem) 
	{
		int count = 0;
		Iterator<Item> contentsIterator = contents.iterator();
		while(contentsIterator.hasNext()){
			if(contentsIterator.next().name.equals(delItem)){
				count++;
				contentsIterator.remove();
			}
		}
		System.out.println("You have deleted " + count + " occurences of " +delItem+ " from your cart.");
	}

	/**
	 * Counts all occurrences of a given item and prints the count
	 * 
	 * @param searchItemName
	 */
	public void searchCart(String searchItemName) 
	{
		int count = 0;
		for(int i = 0; i < contents.size(); i++){
			if(contents.get(i).compareTo(searchItemName) == 0){
				count++; 
			}
		}
		System.out.println("Your cart contains " + count + " of the item " + searchItemName);
	}

	/**
	 * Prints the contents of the cart in alphabetical order and the total charge
	 * of the shopping cart
	 */
	public void printCart() 
	{	// Cart needs to  be sorted by name
		System.out.println("\nPrinting Cart :");
		Collections.sort(contents);
		double sPrice = 0;
		double tPrice = 0;
		double iPrice = 0;
		double fPrice = 0;
		for(Item i : contents)
		{
			iPrice += i.getPrice();
			tPrice += i.getTaxPrice();
			sPrice += i.getShippingPrice();
			fPrice += i.getFinalPrice();
			System.out.println(i);
			
		}
		
		System.out.printf("\nBase Price:\t\t$%.2f", iPrice);
		System.out.printf("\nTotal Shipping Cost:\t$%.2f", sPrice);
		System.out.printf("\nTotal Tax:\t\t$%.2f", tPrice);
		System.out.printf("\nTotal Price:\t\t$%.2f\n\n", fPrice);
		
	}

	/**
	 * Changes the quantity of a given item in cart contents to the specified quantity.
	 * 
	 * @param upItemName name of item to be updated
	 * @param quantity new quantity
	 * @return true if item successfully updated, false if not successfully updated
	 * (Postcondition: quantity of item updated in contents)
	 */
	public boolean updateItem(String upItemName, int newQuantity)
	{ 
		for(int i = 0; i < contents.size(); i++)
		{				
			if(contents.get(i).name.equals(upItemName)){
				contents.get(i).setQuantity(newQuantity);
				contents.get(i).calcItemPrice(); //updates price + tax + shipping with the update
				System.out.println("The first occurence of " + upItemName + " has a new quantity of " + newQuantity);
				return true;
			}
		}
		System.out.println("Your shopping cart did not contain "+upItemName);
		return false;
	}
	

	/**
	 * Get list of contents of cart. 
	 * 
	 * @return contents ArrayList<Item>
	 */
	public ArrayList<Item> getContents() {
		return contents;
	}

	
}
