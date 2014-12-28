/**
Date: Mar 31, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Lab Session: 5
 */

package assignment4;

import java.util.ArrayList;
import javax.swing.*;

/**
 * This class controls the Game, Most of the administrative functions are included in this file 
 * @author Nikhil Joglekar
 *
 */
public class Board 
{
	
	private String acceptableColors = "BGOPRYM"; // blue, green, orange, purple, red, yellow, maroon  ;
	private int WIDTH = 5;
	private ArrayList<pegs> pegHistory;
	private pegs currentGuess;
	private int numGuesses = 12;
	private pegs code;
	StopWatch st;
	
	public Board()
	{
		pegHistory = new ArrayList<pegs>();
		
	}
	public Board(StopWatch s)
	{
		pegHistory = new ArrayList<pegs>();
		st = s;
	}

	void clear()
	{
		numGuesses = 12;
		pegHistory = new ArrayList<pegs>();
	}
	
	/**
	 * Runs the game
	 * @param cheaterMode - if enabled, displays Code at the beginning
	 */
	public void runner(boolean cheaterMode)
	{
		//JOptionPane.showMessageDialog(null, "starting...");
		code = new pegs();
		code.randomize(acceptableColors, WIDTH);
		
		if(cheaterMode)
		{
			st.stop();
			JOptionPane.showMessageDialog(null, "Code : " + code);
			st.start();
		}
		
		gameRun:
		while(numGuesses > 0)
		{
			//System.out.println("You have " + numGuesses + " guesses left.");
			st.stop();
			JOptionPane.showMessageDialog(null, "You have " + numGuesses + " guesses left.");
			st.start();
			
			getInput();
			
			st.stop();
			boolean rcvInput = currentGuess.checkValid(acceptableColors, WIDTH);
			while(!rcvInput)
			{
				getInput();
				rcvInput = currentGuess.checkValid(acceptableColors, WIDTH);
			}
			st.start();
			
			pegHistory.add(currentGuess);
			
			st.stop();
			JOptionPane.showMessageDialog(null, printOutput(currentGuess));
			
			if(checkSolved(currentGuess)){
				JOptionPane.showMessageDialog(null, "You Won!!");
				break gameRun;
			}
			st.start();
						
			numGuesses--;
		}
		
		if(numGuesses == 0){
			st.stop();
			JOptionPane.showMessageDialog(null, "You failed! The code was : " + code);
			st.start();
		}
	}
	
	/**
	 * Retrieves the players guess
	 */
	private void getInput() 
	{
		st.stop();
		String inp = JOptionPane.showInputDialog(null, "What is your next guess?\nType in the characters for your guess and press ENTER.");
		st.start();
		if(inp!=null){
			if(inp.equals("history"))
			{
				String hist = "Printing History...\n\n";
				for(pegs a : pegHistory)
				{
					hist+=a.toString()+" --> ";
					hist+=(printOutput(a) + "\n");
				}
				//System.out.println("_______________________\n");
				st.stop();
				JOptionPane.showMessageDialog(null, hist);
				st.start();
				getInput();
			}
			else
				currentGuess = new pegs(inp);
		}
		else System.exit(0);
	}
	
	/**
	 * Prints to the screen the correct output
	 * @param inp - peg that we want to calculate the output for 
	 * @return - String of calculated output
	 */
	private String printOutput(pegs inp)
	{
		int[] bw = inp.checkBandWs(code);
		
		String ret ="";
		if(bw[0]>0)
		{
			if(bw[0]==WIDTH)
			{
				ret += (bw[0] + " black pegs - You Win !!");
				return ret;
			}
			ret+=(bw[0] + " black pegs ");
		}
		if(bw[1]>0)
		{
			ret+=(bw[1]+ " white pegs");
		}
		if(bw[0]+bw[1]==0)
		{
			ret+=("No pegs");
		}
		return ret;
	}
	
	/**
	 * Checks if last input solves the game
	 * @param inp - last input
	 * @return boolean, whether game is solved or not
	 */
	private boolean checkSolved(pegs inp){
		if(inp.checkBandWs(code)[0]==WIDTH)
			return true;
		return false;
	}
	
	/**
	 * boolean function to add color, returns true if successful add, false if fails
	 * @param a - our valid colors string
	 * @return - returns whether the color was successfully added or not
	 */
	public boolean addColor(String a)
	{
		if(!acceptableColors.contains(""+a.charAt(0))){
			acceptableColors+=a.charAt(0);
			System.out.println("Added color : " + a.charAt(0));
			return true;
		}
		else{
			System.out.println("Failed to add color");
			return false;
		}
	}
	
	/**
	 * Function to change width of guesses
	 * @param a - new width
	 * @return - whether change was valid 
	 */
	public boolean setWidth(int a)
	{
		if(a>0){
			WIDTH = a;
			System.out.println("New Width : " + a);
			return true;
		}
		return false;
	}
	
	/**
	 * Function to change number of guesses
	 * @param a - new # of guesses
	 * @return - whether change was valid
	 */
	public boolean setGuesses(int a)
	{
		if(a>0){
			numGuesses = a;
			System.out.println("New Number of Guesses : " + a);
			return true;
		}
		return false;
	}
}
