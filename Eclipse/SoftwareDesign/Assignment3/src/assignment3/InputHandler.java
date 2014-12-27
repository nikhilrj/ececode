package assignment3;

import java.util.*;
import java.io.*;

/**
 * Performs all file reading and instruction parsing.
 * 
 * Reads in args from transactions.txt in source folder,
 * parses them, and executes accordingly, updating the shopping cart
 * with the information from the inputs.
 * 
 * Handles invalid input by throwing exceptions, prints exceptions 
 * and the line that triggers them to screen.
 *  
 * @author Nikhil Joglekar
 * 
 */

public class InputHandler {
	
	private ShoppingCart cart;
	
	/**
	 * Creates input/transaction handler and ties to new shopping cart. 
	 */
	InputHandler(){
		cart = new ShoppingCart();
	}
	
	/**
	 * Creates input/transaction handler tied to existing shopping cart a
	 * @param a ShoppingCart
	 */
	InputHandler(ShoppingCart a){
		cart = a;
	}
	
	/**
	 * Handles interactions between user and shopping cart. 
	 * Reads from file "transactions.txt"
	 * Checks that input is a legal transaction
	 * Performs legal transactions by operating on shopping cart
	 */
	void inputRunner() {
		try{
			Scanner in = new Scanner(new File("transactions.txt"));//FIX THIS TO BE CORRECT DIRECTORY
			
			while(in.hasNext()){
				
				String inputLine = in.nextLine().trim();
				while(inputLine.isEmpty()){
					inputLine = in.nextLine().trim();
				}
				
				String[] args = inputLine.split("\\s+");
				
				try{
					switch(compareCommand(args[0])) {
						case 1:
							insertHandler(args);
							break;
						case 2:
							searchHandler(args);
							break;
						case 3:
							deleteHandler(args);
							break;
						case 4:
							updateHandler(args);
							break;
						case 5:
							printHandler(args);
							break;
						case -1: throw new IOException("Invalid command");
					}
				}
				catch(IOException e){
					System.out.println("Encountered IOException : " + e + " \n\tfor line: " + inputLine);
				}
				catch(NumberFormatException n){
					System.out.println("Invalid input for weight, price, or quantity : " + n + "\n\tfor line: " + inputLine);
				}
				
			}
			
			in.close();
		}
		catch(Exception e){
			System.out.println("Error reading in file");
		}
	}
	
	private void insertHandler(String[] args)throws IOException, NumberFormatException {
		if(args.length<6||args.length>8)
			throw new IOException("Invalid line");
		
		//the following 3 lines throw NumberFormatExceptions if respective type cannot be parsed
		int quantity = Integer.parseInt(args[4]);
		double price = Double.parseDouble(args[3]);
		double weight = Double.parseDouble(args[5]);
		
		if(price <= 0) throw new NumberFormatException("Price must be positive.");
		if(quantity <= 0) throw new NumberFormatException("Quantity must be positive.");
		if(weight <= 0) throw new NumberFormatException("Weight must be positive.");
		
		
		int item = compareItem(args[1]);
		
		switch(item){
			case 0: //clothing
				if(args.length!=6)
					throw new IOException("Incorrect parameters for Clothing");
				
				Clothing c = new Clothing(args[2], price, quantity, weight);
				cart.addItem(c);
				break;
				
			case 1: //electronics
				if(args.length!=8)
					throw new IOException("Incorrect parameters for Electronics");
				
				boolean fragile;
				
				if(args[6].equals("F")){
					fragile = true;
				}
				else if(args[6].equals("NF")){
					fragile = false;
				}
				else throw new IOException("Electronics must either be F or NF");
				
				Electronics e = new Electronics(args[2], price, quantity, weight, fragile, args[7]);
				if(!e.checkValidState()){
					throw new IOException("Please enter a valid state");
				}
				cart.addItem(e);
				break;
				
			case 2: //groceries
				if(args.length!=7)
					throw new IOException("Incorrect parameters for Groceries");
				boolean perishable; 
				
				if(args[6].equals("P")){
					perishable = true;
				}
				else if(args[6].equals("NP")){
					perishable = false;
				}
				else throw new IOException("Groceries must either be P or NP");
				
				Groceries g = new Groceries(args[2], price, quantity, weight, perishable);
				cart.addItem(g);
				break;
				
			case -1:
				throw new IOException("Type must be either clothing, electronics, or groceries");
		}
	}
	
	private void searchHandler(String args[])throws IOException {
		if(args.length != 2) throw new IOException("Invalid parameters for Search");
		cart.searchCart(args[1]);
	}
	
	private void deleteHandler(String args[])throws IOException{
		if(args.length != 2) throw new IOException("Invalid parameters for Delete");
		cart.deleteItem(args[1]);
	}
	
	private void updateHandler(String args[])throws IOException, NumberFormatException {
		if(args.length != 3) throw new IOException("Invalid parameters for Update");
		int quantity = Integer.parseInt(args[2]);
		if(quantity <= 0) throw new NumberFormatException("Quantity must be positive.");
		
		cart.updateItem(args[1], quantity);
	}
	
	private void printHandler(String args[]){
		cart.printCart();
	}
	
	private int compareCommand(String in){
		if(in.equals("insert"))
			return 1;
		else if(in.equals("search"))
			return 2;
		else if(in.equals("delete"))
			return 3;
		else if(in.equals("update"))
			return 4;
		else if(in.equals("print"))
			return 5;
		
		return -1;
	}
	
	private int compareItem(String in){
		if(in.equals("clothing"))
			return 0;
		else if(in.equals("electronics"))
			return 1;
		else if(in.equals("groceries"))
			return 2;
		else return -1;
	}
}
