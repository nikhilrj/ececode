package assignment5;
import java.util.*;

/**
 * Class that creates word ladders and contains recursive methods to compare words.
 * @author Kevin, Nikhil
 *
 */
public class FindLadder {
    private final int length;
    Dictionary dict;
    private PriorityQueue<String> nextWord;
    private int treshold;
	private int count;
    private ArrayList<String> solution;
    private HashSet<String> visited;
    static StopWatch sw;
    
    /**
     * Constructor for the FindLadder class.
     * Creates a dictionary to be used by the FindLadder class.
     * @param dictName
     * @param wordLength
     * Precondition: dictName is a valid name of a file with dictionary words, wordLength is a valid integer greater than one.
     * Postcondition: A dictionary with words of wordLength characters is created. 
     */
    /*public FindLadder(String dictName, int wordLength) {
    	try{
    		dict = new Dictionary(5, dictName);
    	}
    	
    	catch (java.io.FileNotFoundException e){
    		System.out.println("DICTIONARY FILE NOT FOUND");
    	} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
    		System.out.println("IO Exception");
			e.printStackTrace();
		}
    	
    	length = wordLength;
    	treshold = 10;
    	sw = new StopWatch();
    	
    } */
    
    /**
     * Constructor for the FindLadder class.
     * Uses the dictionary passed through theDict.
     * @param theDict
     */
    public FindLadder(Dictionary theDict){
    	dict = theDict;
    	length = theDict.getLength();
    	treshold = 100000000;
    	sw = new StopWatch();
    }
    
    /**
     * Attempts to create a word latter between the two given words.
     * @param begin
     * @param end
     * Precondition: begin and end are valid words of the same length as the words in the dictionary
     * Postcondition: Creates a word ladder if one exists, else tells the user that no word ladder exists.
     */
	public void generateLadder(String begin, final String end) {
		
		//nextWord is a PriorityQueue that stores values based on how close they are to the end string.
		//the string with the least # of characters different than the end string will be placed at the head of the queue.
		nextWord = new PriorityQueue<String>(100,
				new Comparator<String>( ) {
    				public int compare(String i, String j) {
    					int diffA = strComparer(i, end);
    					int diffB = strComparer(j, end);
    					
    					if(diffA == diffB)
    						return 0;
    					
    					if(diffA < diffB) //i is closer to the end
    						return -1;
    					else //j is closer to the end
    						return 1;
    				}
    		}
		);
		
		//visited is a HashSet to keep track of which elements we have already been to in O(1) lookup
		visited = new HashSet<String>();
		//we create a solution ArrayList to hold the resulting wordladder from our recursive calls
		solution = new ArrayList<String>();
		nextWord.add(begin);
		visited.add(begin);
		count = 0;
		sw.reset();
		try{
			sw.start();
			boolean a =(depthFirstSearch(begin, end, 0));
			sw.stop();
			if(a){
				int n = solution.size()-1;
				while(n>1){
					if(strComparer(solution.get(n-1),solution.get(n))!=1)
						solution.remove(n-1);
					else
						n--;
				}
				System.out.println("Solution Found in " + sw.getElapsedTime() + " ns");
				System.out.println("The ladder for " + begin + " and " + end + " is: " );
				String b = "[";
				for(int i = 0; i < solution.size(); i++){
					b+= (solution.get(i) + ", ");
				}
				System.out.println(b+ end+ "]"  + "\n" );
			}
			else
				System.out.println("For the input words " + begin+ " and " + end + "\n"+
						"There is no word ladder between " + begin + " and " + end + "\n"+  "Time taken: " + sw.getElapsedTime() + " ns" + "\n");
			
		}
		catch(StackOverflowError e){
			sw.stop();
			System.out.println(e + "Reached treshold of " + treshold + " in " + sw.getElapsedTime() + " ns");
		}
	}
    
	/**
	 * Recursive method that checks for words that are valid solutions.
	 * @param begin
	 * @param end
	 * @param change
	 * @return returns true if a valid next step in the word ladder if found, else false.
	 */
    boolean depthFirstSearch(String begin, String end, int change){
    	//first, we find how close begin is to end
    	if(strComparer(begin, end) == 1){
    		solution.add(begin);
    		return true;
    	}
    	
    	//sees if time has exceeded allotted treshold
    	if(sw.getElapsedTime() > treshold)
    		throw new Error();
      	
    	//reached end of 'begin' permutation
    	if(change >= length){
    		nextWord.remove(begin);
    		return false;
    	}

    	//permute through all possible combinations of the begin string
    	//ex for hello, Aello...Zello - ...hellA...hellZ
    	if(count<=length){
	    	for(char a = 'a'; a!= 'z'; a++){
		    	String newWord = begin.substring(0, change) + a + begin.substring(change+1);
		    	
		    	//we want the next word to be the word closest to the solution
		    	//to achieve this, we put it into the priorityqueue, and are guaranteed
		    	//the word closest to the end string is at the head of the queue.
		    	if(dict.contains(newWord) && !visited.contains(newWord))
		    		if(!nextWord.contains(newWord))
		    			nextWord.add(newWord);
		   	}
    	
    		count++;
    		if(depthFirstSearch(begin, end, change+1))
    			return true;
    	}
    	
	    count = 0;
	   	if(nextWord.isEmpty())
	   		return false;

	   	//count++;
	   	visited.add(nextWord.peek());
		solution.add(begin);
		//now, we recurse with the next closest word to the solution
	   	return depthFirstSearch(nextWord.remove(), end, 0);
    }
    
    /**
     * Takes two strings and counts the number of characters that they differ by.
     * @param a
     * @param b
     * @return the number of characters that the two strings differ by.
     */
    private int strComparer(String a, String b){
        int diffCount = 0;
 	    for (int i = 0; i < a.length(); i++) {
 		    if (a.charAt(i) != b.charAt(i)) {
 			    diffCount++;
 		    }
 	    }
 	    return diffCount;
    }
    
    /**
     * Getter function for solution.
     * @return returns the solution ArrayList
     */
    public ArrayList<String> getSolution(){
    	return solution;
    }
    
    /**
     * Set the treshold in NS
     * @param treshold
     */
    public void setTreshold(int treshold) {
		this.treshold = treshold;
	}

}
