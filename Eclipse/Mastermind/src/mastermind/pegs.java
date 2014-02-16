/**
Date: Feb 17, 2013
Student Name: Nikhil Joglekar
EID: nrj328
Student Name: Anirudh Kashyap
EID: ak22564
Lab Session: 5
 */
package mastermind;

import javax.swing.JOptionPane;

//Handles all actions of the Pegs
public class pegs 
{
	
	private String pegSet;
	//Constructor for the Pegs Class
	pegs()
	{
		pegSet = "";
	}
	
	pegs(String inp)
	{
		pegSet = inp;
	}
	//Randomizes a set of pegs
	void randomize(String a, int s)
	{
		for(int i = 0; i < s; i++)
		{
			int index = (int) (Math.random()*a.length());
			String add = "" + a.charAt(index);
			pegSet+=add;
		}
	}
	//Check to see if the input is valid
	boolean checkValid(String a, int w)
	{
		if(pegSet.length()==w)
		{
			for(int n = 0; n < pegSet.length(); n++)
			{
				if(!a.contains("" + pegSet.charAt(n)))
				{
					JOptionPane.showMessageDialog(null, "INVALID GUESS, INVALID COLORS");
					return false;
				}
			}
			return true;
		}
		JOptionPane.showMessageDialog(null, "INVALID GUESS, WRONG WIDTH");
		return false;
	}
	//Checks the pegs to see if they're correct
	int[] checkBandWs(pegs code)
	{
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
		return ret;
	}
	
	public String toString()
	{
		return pegSet;
	}
}
