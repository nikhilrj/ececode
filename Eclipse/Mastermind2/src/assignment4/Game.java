/**
Date: Mar 31, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Lab Session: 5
 */

package assignment4;

import javax.swing.JOptionPane;

/**
 * Overall runner for the Game
 * @author Nikhil Joglekar
 *
 */
public class Game 
{
	
	private Board gameBoard;
	private boolean cheaterMode = false;
	static StopWatch st = new StopWatch();
	long lastTime = 0;
	
	Game()
	{
		gameBoard = new Board(st);
	}
	
	Game(Boolean b)
	{
		gameBoard = new Board(st);
		cheaterMode = b;
	}	
	
	/**
	 * Starts an instance of the game
	 */
	void run()
	{
		st.start();
		st.stop();
		JOptionPane.showMessageDialog(null, "Welcome to Mastermind.  Here are the rules.\nThis is a text version of the classic board game Mastermind.\nThe computer will think of a secret code. The code consists of 5 colored pegs.\nThe pegs may be : blue, green, orange, purple, red, yellow, and maroon. A color may appear more than once in the code.\nYou try to guess what colored pegs are in the code and what order they are in.\nAfter you make a valid guess the result (feedback) will be displayed.\nThe result consists of a black peg for each peg you have guessed exactly correct (color and \nposition) in your guess.  For each peg in the guess that is the correct color, but is out of position, \nyou get a white peg.  For each peg, which is fully incorrect, you get no feedback. \nOnly the first letter of the color is displayed. B for Blue, R for Red, and so forth.\nWhen entering guesses you only need to enter the first character of each color as a capital letter.\nYou may use keyword \"history\" to output the history of your inputs so far.");
		st.start();
		System.out.println("Welcome to Mastermind.  Here are the rules.\nThis is a text version of the classic board game Mastermind.\nThe computer will think of a secret code. The code consists of 5 colored pegs.\nThe pegs may be : blue, green, orange, purple, red, yellow, and maroon. A color may appear more than once in the code.\nYou try to guess what colored pegs are in the code and what order they are in.\nAfter you make a valid guess the result (feedback) will be displayed.\nThe result consists of a black peg for each peg you have guessed exactly correct (color and \nposition) in your guess.  For each peg in the guess that is the correct color, but is out of position, \nyou get a white peg.  For each peg, which is fully incorrect, you get no feedback. \nOnly the first letter of the color is displayed. B for Blue, R for Red, and so forth.\nWhen entering guesses you only need to enter the first character of each color as a capital letter.\nYou may use keyword \"history\" to output the history of your inputs so far.");

		String b = "";
		st.stop();
		String a = JOptionPane.showInputDialog(null, "You have 12 guesses to figure out the secret code or you lose the game.\nAre you ready to play?\nEnter Y or N");
		st.start();	
		
		GAME:
		while(true)
		{
			if(a!=null){
				if(a.equals("Y"))
				{
					gameBoard.runner(cheaterMode);
					
					
					gameBoard.clear();
					System.out.println("Current game runtime : " + (st.getElapsedTime()-lastTime) + " ns");
					lastTime = st.getElapsedTime();
					st.stop();
					a = JOptionPane.showInputDialog(null,"Would you like to play again?\nEnter Y or N");
					st.start();
				}
				else
				{
					if(a.equals("N")||b.equals("N"))
					{
						st.stop();
						JOptionPane.showMessageDialog(null, "Thank you! Exiting.");
						st.start();
						break GAME;
					}
					else
					{
						st.stop();
						JOptionPane.showMessageDialog(null,"Sorry, please type Y or N or quit");
						a = JOptionPane.showInputDialog(null,"Would you like to play again?\nEnter Y or N");
						st.start();
					}
				}
			}
			else System.exit(0);
		}
		System.out.println("Full Match Execution time : " + st.getElapsedTime() + " ns");
	}
}
