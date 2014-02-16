import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class SATWordBank {
	private final HashMap<String, String> wordBank;
	
	
	/**
	 *  Function loads dictionary and adds words to a HashMap for program to interact with
	 * 
	 * @param inputFile
	 * @throws Exception, FileNotFoundException, DuplicateDictionaryException
	 */
	public SATWordBank(String inputFile) throws Exception, FileNotFoundException, DuplicateDictionaryException {
		wordBank = new HashMap<String, String>();
		Scanner s = null;
		
		try {
			s = new Scanner(new File(inputFile));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("No dictionary found!");
		} catch (Exception e) {
			throw e;
		} 
		
		while (s.hasNextLine()) {
			String line = s.nextLine(); 
			String word = line.split(" ")[0];
			if (wordBank.containsKey(word)) {
				throw new DuplicateDictionaryException(word);
			}
			wordBank.put(word, line);
		}		
		
		s.close(); 
	}
	
	/**
	 * Adds words to dictionary
	 * @param word, def
	 */
	public void add(String word, String def) {
		wordBank.put(word.toLowerCase(),  word + " " + def); 
	}
	
	
	/**
	 * Gets definition of word(key)
	 * returns definition(value) 
	 * @param word
	 * @return definition string
	 * @throws WordNotFoundException
	 */
	public String GetDefinition(String word) throws WordNotFoundException {
		if (!wordBank.containsKey(word)) {
			throw new WordNotFoundException(word);
		} 
		else {
			return wordBank.get(word);
		}
	}
}

/**
 * Custom exception class; used when words aren't found in dic
 */
class WordNotFoundException extends Exception {
	static final long serialVersionUID = 1;
	
    public WordNotFoundException(String message) {
        super(message);
    }
}

/**
 * used when dictionary contains duplicates  
 */
class DuplicateDictionaryException extends Error {
	static final long serialVersionUID = 1; 
	
	public DuplicateDictionaryException(String message) {
		super(message); 
	}
}