import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Translator 
{
	
	public static void main (String args[]) 
	{ 
		if (args.length != 1) 
		{
			System.err.println ("Error: Incorrect number of command line arguments");
			System.exit(-1);
		}
		processLinesInFile (args[0]);
		
	}

	/**
	 * Opens the file specified in String filename, reads each line in it
	 * Invokes translate () on each line in the file, and prints out the  
	 * translated piglatin string.
	 * @param filename - the name of the file that needs to be read
	 */
	public static void processLinesInFile (String filename) 
	{ 

		Translator translator = new Translator(); 
		try 
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			
			for (String s = reader.readLine(); s != null; s = reader.readLine()) 
			{
				System.out.println("The input string is: " + s);
                                String pigLatin = translator.translate(s);
				System.out.println("The Pig Latin version is: " + pigLatin);
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println ("Error: File not found. Exiting...");
			e.printStackTrace();
			System.exit(-1);
		} 
                catch (IOException e) 
		{
			System.err.println ("Error: IO exception. Exiting...");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Converts the inputString into piglatin based on rules specified 
	 * in your assignment write-up. 
	 * 
	 * @param inputString - the String that needs to be translated to 
	 * 			piglatin
	 * @return the String object containing the piglatin translation of the
	 *         inputString    
	 */
	
	public String translate (String inputString)
	{ 
		/*splits the string into words, each word is the
		 *element of the array 'Words'. Then, each word is translated
		 *with the routine 'Latin', and the original elements of 'Word'
		 *are replaced with the translated ones. Lastly, the 
		 *translated array is turned into a string and the translated
		 *phrase is returned*/
		String[] Words = inputString.split("\\s+"); //splits whenever a delimiter is encountered
		String[] PigLatin = Words; 
		for (int y=0; y<Words.length; y++)
		{
			/*translates the words to PigLatin*/
			PigLatin[y]= Latin(Words[y]);
		}
		inputString = "";
		for (int i=0; i<Words.length; i++)
		{
			/*puts the translated elements into a string so that the phrase can be returned*/
			inputString+=PigLatin[i];
			inputString+=' ';
		}
		return inputString; /*returns the translated phrase*/
	}

	public static String Latin (String inputString)
	{
		/*checks for digits,symbols, and hyphens.
		 *if the string contains a hyphen, the string is split
		 *if the string has numbers or symbols, the word is unchanged
		 *then, the string is translated with the toTranslate method*/
		int l= inputString.length(); 
		for (int x=0; x<l; x++)
		{
			char d= inputString.charAt(x); 
			if (Character.isDigit(d))
			{ 
				return inputString; //the string contains numbers
			}
			else if(d != '-' && d != '\'' && !Character.isLetter(d))
			{
				if(Delimitercheck(d))
				{
					/*if the nonnumeric, nonalphabetic character is a delimiter,
					 * the delimiter is appended to the end of the translated word
					 * using this routine*/
					StringBuilder numbers = new StringBuilder(inputString); 
					int z= inputString.indexOf(d); 
					numbers.delete(z, z+1); 
					inputString= Latin(numbers.toString()) +d; 
					return inputString; 
				}
				else 
					return inputString; 
			}
			else {}
		}
		if(inputString.contains("-"))
		{
			/* splits the hyphenated word into parts and 
			 * translates each part separately, then puts 
			 * connects them back with a hyphen */
			String[] parts = inputString.split("-");
			String parts1= parts[0];
			String parts2= parts[1]; 
			parts1= toTranslate(parts1);
			parts2= toTranslate(parts2);
			inputString= parts1+'-'+parts2; 
		}
		else{
			/*if there was no hyphen, the routine
			 * goes on to translate without separating the word */
			inputString= toTranslate(inputString); 
		}
		return inputString; 
			/*the translated word is returned*/ 
	}

	public static String toTranslate (String string1)
	{
		/*translates words to piglatin*/
		char letter1= string1.charAt(0); 
		if(string1.length() == 1)
		{
			/* adds 'yay' if the word has 1 element*/
			return string1+"yay"; 
		}
		char letter2= string1.charAt(1);
		StringBuilder sB1 = new StringBuilder(string1); 
		if (Vowelcheck(letter1)){
			/*adds 'yay' if the word starts with a vowel*/
			sB1.append("yay");
		}
		else if(Vowelcheck(letter2) || letter2=='y' || letter2=='Y' || letter2 == '\''){
			/*moves the consonant to the end of the word and appends 'ay'*/
			sB1.append(letter1);
			sB1.append("ay");
			sB1.delete(0, 1);
		}
		else{
			/*moves the consonants to the end of the word and appends 'ay'*/
			sB1.append(letter1);
			sB1.append(letter2);
			sB1.append("ay");
			sB1.delete(0, 2);
		}
		return sB1.toString(); /*returns the translated phrase as a string*/
	}

	public static boolean Delimitercheck(char c)
	{
		/*tests whether the character is a delimiter*/
		if(c==','|| c=='"' || c=='(' || c==')' || c==':' || c==';' || c=='.' || c=='!' || c== '?')
		{
			return true;
		}
		else
			return false; 
	}
	
	public static boolean Vowelcheck (char c)
	{ 
		/*tests whether the character is a vowel*/
		if(c=='a' || c=='e' || c=='i' || c=='o' || c=='u' || c=='A'|| c=='E' || c=='I' || c=='O' || c=='U')
		{
		return true;
		}
		else
		{
		return false; 
		}
	}
	
}
	
	

