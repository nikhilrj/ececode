package assignment1;
/**
Date: Feb 1, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Lab Session: Monday 5:00-6:30PM
 */

import java.util.*;
import java.io.*;

public class Palindrome {
	
	/**
	 * runner for Palindrome class
	 * Reads inputs from "input.txt" in the Lab1 folder (not the src folder!)
	 * prints occurrences of palindromes.
	 * @throws IOException
	 */
	void palRunner()throws IOException {
		Scanner scan = new Scanner(new File("input.txt"));
		
    	while( scan.hasNext() ) {
    		String s = scan.nextLine();
    		if(s.equals("QUIT")) { break; }
    		System.out.println("Input> " + s);
    		System.out.print("Output> ");
    		palFinder(s);
    		System.out.println();
	    } 
    	
    	scan.close();
	}
	
	/**
	 * Tests input for palindromes, and prints results
	 * @param test
	 */
	void palFinder(String test){
		String[] palindromes = new String[10000]; //max 10,000 words in a line
		int index = 0;
				
		String opString = removeTokens(test);
        String bigString = ""; //bigString is a comparison string used to test if one string is contained in another

        /*
         * Algorithm reverses and compares the substring from the first character 
         * to the lastIndexOf the first character. It continues to compare the first 
         * character to a smaller substring, from [0] to the lastIndex before the 
         * previous last index.
         */
        while(!opString.isEmpty()){
            int lastIndex = opString.lastIndexOf(opString.charAt(0));

            while(lastIndex!=0){
                String comp = opString.substring(0, lastIndex+1);
                String reversed = reverser(comp);

                if(comp.equals(reversed)){
                    if(comp.length()>2 && !bigString.contains(comp)) {
                        palindromes[index]=comp;
                        index++;
                        bigString+=(comp.substring(1,comp.length()-1))+" ";
                    }
                }
                lastIndex = (opString.substring(0,lastIndex)).lastIndexOf(opString.charAt(0));
            }
            opString = opString.substring(1);
        }
        palPrinter(palindromes, index);
	}
	
	/**
	 * @param a - string array to be sorted and printed
	 * @param s - number of strings in array
	 */
	void palPrinter(String[] a, int s){
		if(s<1) //no need to print if there are no palindromes
			return;
		
		String[] small = new String[s]; //new array to get rid of null strings before sorting
		
		for(int n = 0; n < s; n++){
			small[n]=a[n];
		}
		
		Arrays.sort(small);
		
		System.out.print(small[0] + " ");
		
		for(int n = 1; n < s; n++){
			if(!small[n].equals(small[n-1])) //checks if string already has been printed
				System.out.print(small[n] + " ");
		}
	}
	
	
    /**
     * @param inp string
     * @return reversed string
     */
	String reverser(String inp){
        String reversed = "";
        for(int i=inp.length()-1; i>=0 ; i--){
            reversed += inp.charAt(i);
        }
        return reversed;
    }

    /**
     * @param inp input string
     * @return string without any whitespace characters (ie ' ', '.', '_', '"'), in all caps
     */
	String removeTokens(String inp){
        String equalized = "";
        for(int i=0; i<inp.length(); i++){
            char c = inp.charAt(i);
            if(c>='a' && c<='z'){
                c+=('A'-'a');
                equalized+=c;
            }
            else if(c>='A' && c<='Z'){
                equalized+=c;
            }
        }
        return equalized;
    }
}
