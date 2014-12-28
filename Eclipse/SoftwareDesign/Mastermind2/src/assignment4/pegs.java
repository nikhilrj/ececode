/**
Date: Mar 31, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Lab Session: 5
 */
package assignment4;

import javax.swing.JOptionPane;

/**
 * Handles all actions of the Pegs
 * @author Nikhil
 *
 */
public class pegs 
{
	
	private String pegSet;
	
	/**
	 * Constructor for the Pegs Class
	 */
	pegs()
	{
		pegSet = "";
	}
	
	pegs(String inp)
	{
		pegSet = inp;
	}
	
	/**
	 * Randomizes a set of pegs
	 * @param a - valid colors
	 * @param s - width of guesses
	 */
	public void randomize(String a, int s)
	{
		StopWatch stop = new StopWatch();
		stop.start();
		for(int i = 0; i < s; i++)
		{
			int index = (int) (Math.random()*a.length());
			String add = "" + a.charAt(index);
			pegSet+=add;
		}
		
		System.out.println("pegs.randomize ran in " + stop.getElapsedTime() + " ns");
	}
	
	/**
	 * Checks whether input is valid or not
	 * @param a - current guess
	 * @param w - width
	 * @return
	 */
	public boolean checkValid(String a, int w)
	{
		StopWatch stop = new StopWatch();
		stop.start();
		
		if(pegSet.length()==w)
		{
			for(int n = 0; n < pegSet.length(); n++)
			{
				if(!a.contains("" + pegSet.charAt(n)))
				{
					System.out.println("pegs.checkValid ran in " + stop.getElapsedTime() + " ns");
					JOptionPane.showMessageDialog(null, "INVALID GUESS, INVALID COLORS");
					return false;
				}
			}
			
			System.out.println("pegs.checkValid ran in " + stop.getElapsedTime() + " ns");
			return true;
		}
		
		System.out.println("pegs.checkValid ran in " + stop.getElapsedTime() + " ns");
		JOptionPane.showMessageDialog(null, "INVALID GUESS, WRONG WIDTH");
		return false;
	}
	
	/**
	 * Checks the pegs to see if they're correct
	 * @param code
	 * @return - # of black([0]) and white pegs([1]) 
	 */
	public int[] checkBandWs(pegs code)
	{
		StopWatch stop = new StopWatch();
		stop.start();
		
		int[] ret = {0,0};
		StringBuilder codeString = new StringBuilder(code.pegSet);
		StringBuilder thisString = new StringBuilder(pegSet);
		
		for(int i = 0; i < this.pegSet.length(); i++)
		{
			if(thisString.charAt(i)==codeString.charAt(i))
			{
				ret[0]++;
				thisString.setCharAt(i, '-');
				codeString.setCharAt(i, '-');
			}
		}
		
		String inpCode = new String(codeString);
		String inpPegs = new String(thisString);
		
		for(int i = 0; i < this.pegSet.length(); i++)
		{
			if(thisString.charAt(i)!= '-' && inpPegs.contains(""+inpCode.charAt(i)))
			{
				ret[1]++;
			}
		}

		System.out.println("pegs.checkBandWs ran in " + stop.getElapsedTime() + " ns");
		return ret;
	}
	
	public String toString()
	{
		return pegSet;
	}
}
