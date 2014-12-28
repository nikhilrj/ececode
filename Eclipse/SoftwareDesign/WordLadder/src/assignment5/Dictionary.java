package assignment5;
import java.io.*;
import java.util.HashSet;

/**
 * Class that creates dictionary object with given .dat file of valid words.
 * @author Kevin
 *
 */
public class Dictionary {
	
	public HashSet<String> wordList;
	private int length;
	
	public Dictionary(int wordLength, String dictFile) throws IOException{
		try{
			wordList = new HashSet<String>();
			length = wordLength;
			FileReader infile = new FileReader(dictFile);
			BufferedReader br = new BufferedReader(infile);
			String s = br.readLine();
			while(s != null){
				if(s.startsWith("*")==false)
					wordList.add(s.substring(0,wordLength));
				s = br.readLine();
			}
			br.close();
		}
		
		catch(java.io.FileNotFoundException e){
    		System.out.println("DICTIONARY FILE NOT FOUND");
    	} 
	}
	
	/**
	 * Searches dictionary for the given word.
	 * 
	 * @param searchWord
	 * @return returns true if the word is in the dictionary, else returns false.
	 */
	public boolean contains(String searchWord){
		return wordList.contains(searchWord);
	}
	
	/**
	 * Adds the provided word to the dictionary.
	 * 
	 * @param newWord
	 * Precondition: Length of newWord is equal to the wordLength of the dictionary.
	 * Postcondition: newWord is added to the dictionary.
	 */
	public void addWord(String newWord){
		if(newWord.length() == length && newWord.startsWith("*")==false)
			wordList.add(newWord);
	}
	
	/**
	 * Getter function for the current size of the dictionary (number of words in the dictionary).
	 * 
	 * @return returns the number of words in the dictionary.
	 */
	public int length(){
		return wordList.size();
	}
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int newLength){
		length = newLength;
	}
}
