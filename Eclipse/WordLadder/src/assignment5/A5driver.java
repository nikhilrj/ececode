package assignment5;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Top level class of Assignment 5 (Word Ladder)
 * This class creates a dictionary object using the given .dat file of dictionary words.
 * It the reads the word pairs from the given input file and creates a word ladder.
 * If the given words are invalid inputs, or no word ladder can be created, the corresponding errors
 * are output to the user on the console.
 * @author Kevin
 *
 */
public class A5driver {

	 public static void main(String[] args) throws IOException {
	        //FindLadder finder = new FindLadder("assn1words.dat", 5);
	        Dictionary dictionary = new Dictionary(5, "assn5words.dat");
	        FindLadder finder = new FindLadder(dictionary);
	        finder.generateLadder("hello", "world");
	        finder.generateLadder("stone", "money");
	        finder.generateLadder("abide", "abuzz");
	        finder.generateLadder("heads", "tails");
	        finder.generateLadder("smart", "money");
	        finder.generateLadder("atlas", "zebra");
	        finder.generateLadder("mumbo", "ghost");
	              
	       /* try {
	        	String start="";
	        	String end="";
	            Scanner infile = new Scanner(new File("assn5data.txt"));
	            while (infile.hasNext()) {	
	              	String line=infile.nextLine();
	            	while (line.length()==0){
	            		line=infile.nextLine();
	            		System.out.println("Invalid input: encountered empty line");
	            	}
	            	String line1 = line.trim(); //take out leading white space        	
	            	if (line1.length()==0){
	            		System.out.println("Invalid input");
	            	}else {
	            		
	            		StringTokenizer st= new StringTokenizer(line1, " \n\t");
	        	       
	        	        	start=st.nextToken().toLowerCase();
	        	        	end=st.nextToken().toLowerCase();
	        	        	if ( !(dictionary.contains(start)&&dictionary.contains(end)) ){
	        	        		System.out.println(start + " and "+ end + " Not in dictionary " +"\n");
	        	        	}
	        	        	else if ( ! (start.matches("[a-z]+")&&end.matches("[a-z]+")) ){
	        	        		System.out.println("invalid");
	        	        	}
	        	        	else if(  start.length()!=5  || end.length()!=5){
		        				System.out.println("For the input words " + start + " and " + end + "\n"+
		        						"At least one of the words " + start + " and " +end+ " are not legitimate 5-letter" + 
		        						"words from the dictionary" + "\n");
		        			}else{
		        			
	        	        	finder.generateLadder(start, end);
		        			}
	            		
	            	}      
	                   
	            }
	            infile.close();
	        }
	        catch (java.io.FileNotFoundException e) {
	            System.out.println("FILE NOT FOUND");
	        } 
	        
	    }*/
	 }
}
