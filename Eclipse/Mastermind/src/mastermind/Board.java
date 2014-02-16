/**
Date: Feb 17, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Student Name: Anirudh Kashyap
EID: ak22564
Lab Session: 5
 */
package mastermind;

import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;

//This class controls the Game, Most of the adminstrative functions are included in this file 
public class Board 
{
	
	private String acceptableColors = "BGOPRYM"; // blue, green, orange, purple, red, yellow, maroon  ;
	private int WIDTH = 5;
	private ArrayList<pegs> pegHistory;
	private pegs currentGuess;
	private int numGuesses = 12;
	private Scanner in;
	private pegs code;
	
	//Constructor
	public Board()
	{
		pegHistory = new ArrayList<pegs>();
		
	}
	public boolean getB() {
		return true;
	}
	void clear()
	{
//		getB();
		numGuesses = 12;
		pegHistory = new ArrayList<pegs>();
	}
	
	//Runs the game
	void runner(boolean cheaterMode)
	{
		//JOptionPane.showMessageDialog(null, "starting...");
		code = new pegs();
		code.randomize(acceptableColors, WIDTH);
		in = new Scanner(System.in);
		
		if(cheaterMode)
		{
			JOptionPane.showMessageDialog(null, "Code : " + code);
		}
		
		gameRun:
		while(numGuesses > 0)
		{
			//System.out.println("You have " + numGuesses + " guesses left.");
			JOptionPane.showMessageDialog(null, "You have " + numGuesses + " guesses left.");
			
			getInput();
			
			boolean rcvInput = currentGuess.checkValid(acceptableColors, WIDTH);
			while(!rcvInput)
			{
				//System.out.println(" -> INVALID GUESS\n\n	");
				getInput();
				rcvInput = currentGuess.checkValid(acceptableColors, WIDTH);
			}
			
			pegHistory.add(currentGuess);
			
			JOptionPane.showMessageDialog(null, printOutput(currentGuess));
			
			if(checkSolved(currentGuess)){
				JOptionPane.showMessageDialog(null, "You Won!!");
				break gameRun;
			}
						
			numGuesses--;
		}
		
		if(numGuesses == 0){
			JOptionPane.showMessageDialog(null, "You failed! The code was : " + code);
		}

		//in.close();
	}
	//Retrieves the players guess
	private void getInput() 
	{
		String inp = JOptionPane.showInputDialog(null, "What is your next guess?\nType in the characters for your guess and press ENTER.");
		if(inp!=null){
			if(inp.equals("history"))
			{
				String hist = "Printing History...";
				for(pegs a : pegHistory)
				{
					hist+=a.toString();
					hist+=(printOutput(a) + "\n");
				}
				//System.out.println("_______________________\n");
				JOptionPane.showMessageDialog(null, hist);
				getInput();
			}
			else
				currentGuess = new pegs(inp);
		}
		else System.exit(0);
	}
	//Prints to the screen the correct output
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
	
	private boolean checkSolved(pegs inp){
		if(inp.checkBandWs(code)[0]==WIDTH)
			return true;
		return false;
	}
	
	//boolean function to add color, returns true if successful add, false if fails
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
	
	public void setWidth(int a)
	{
		WIDTH = a;
		System.out.println("New Width : " + a);
	}
	
	public void setGuesses(int a)
	{
		numGuesses = a;
		System.out.println("New Number of Guesses : " + a);
	}
}
