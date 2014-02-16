import java.io.FileNotFoundException;
import java.util.Scanner;


public class ExceptionTracker {
	public static void main(String[] args) {
		
		/**
		 * Opens up and loads words from the dictionary
		 */
		SATWordBank swb = null; 
		Scanner s = new Scanner(System.in);
		
		try {
			//tries to open up dictionary file and add words so can interact with it
			swb = new SATWordBank("dictionary.txt");
		} 
		catch (FileNotFoundException e) {
			//catches exception if file not found
			e.printStackTrace();
		} 
		catch (DuplicateDictionaryException e) {
			//catches exception if dictionary file has two of the same word
			e.printStackTrace();
		} 
		catch (Exception e) {
			//catches any other problems
			e.printStackTrace();
		}
		
		
		/**
		 * This code interacts with the user input stream to look up words
		 * Quits on ":q" input
		 */
		System.out.println("Welcome to SAT vocabulary dictionary!");
		System.out.println("Type a word and press enter for details");
		
		while(true) {
			String term = s.nextLine(); 
			
			if (term.equalsIgnoreCase(":q")) {
				s.close();
				System.exit(0);
			}
			
			try {
				System.out.println(swb.GetDefinition(term));
			} catch (WordNotFoundException e) {
				System.out.print("No definition found for: '" + term + "' would you like to add it? (Y/n) ");
				
				String ans = s.nextLine(); 
				
				if (ans.equalsIgnoreCase("Y") || ans.equalsIgnoreCase("yes") || ans.equals("")) {
					System.out.print("Enter Definition: ");
					swb.add(term, s.nextLine());	
					System.out.println("Successfully added!");
				}
			}
		}
	}
}
