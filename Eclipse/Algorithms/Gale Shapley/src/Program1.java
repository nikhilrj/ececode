/*
 * Name: Nikhil Joglekar
 * EID: nrj328
 */

import java.util.ArrayList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
    	ArrayList<ArrayList<Integer>> hospPref = marriage.getHospitalPreference();
    	ArrayList<ArrayList<Integer>> resPref = marriage.getResidentPreference();
    	ArrayList<Integer> match = marriage.getResidentMatching();
    	ArrayList<Integer> slots = marriage.getHospitalSlots();
    	
    	ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();
    	
    	for(int r = 0; r < marriage.getHospitalCount(); r++){
    		current.add(new ArrayList<Integer>());
    	}
    	
    	for(int r = 0; r < match.size(); r++){
    		int h = match.get(r);
    		if(h!=-1) current.get(h).add(r);
    	}
    	
    
    	for(int i = 0; i < marriage.getResidentCount(); i++){ //i is index of student
    		if(match.get(i)==-1) { //Unmatched
    			for(int h = 0; h < current.size(); h++){ // h is current hospital index
    				ArrayList<Integer> currentlyMatched = current.get(h);
    				if(currentlyMatched.size()!=slots.get(h))
    					return false;
    				for(int j : currentlyMatched){ //j will be the currently matched resident's #
    					if(hospPref.get(h).indexOf(i) < hospPref.get(h).indexOf(j))
    						return false;
    				}
    			}
    		}
    		else{ //matched
    			for(int h = 0; h < current.size(); h++){
    				ArrayList<Integer> currentlyMatched = current.get(h);
    				for(int j : currentlyMatched){
    					if(hospPref.get(h).indexOf(i) < hospPref.get(h).indexOf(j)) //h prefers i to j
    						if(resPref.get(i).get(h) < resPref.get(i).get(match.get(i)))
    							return false;
    				}
    			}
    		}
    	}
    	
    	return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching marriage) {

    	ArrayList<ArrayList<Integer>> hospPref = new ArrayList<ArrayList<Integer>>(marriage.getHospitalPreference());
    	ArrayList<ArrayList<Integer>> resPref = new ArrayList<ArrayList<Integer>>(marriage.getResidentPreference());
    	
    	ArrayList<Integer> match = new ArrayList<Integer>();
    	int[] slots = new int[marriage.getHospitalCount()];
    	for(int p = 0; p < marriage.getHospitalCount(); p++){
    		slots[p] = marriage.getHospitalSlots().get(p);
    	}
    	
    	int hospSize = marriage.getHospitalCount();
    	int i = 0;
    	
    	for(int n = 0; n < marriage.getResidentCount(); n++){
    		match.add(-1);
    	}

    	while(hospitalFull(slots) && preferencesExhausted(hospPref)){
    		if(!hospPref.get(i).isEmpty() && slots[i]>0) {
    			    			
	    		int res = hospPref.get(i).get(0); // gets resident index
	    		int currentMatch = match.get(res);
	    		
	    		if(currentMatch==-1){ //is resident currently matched?
	    			match.set(res, i);  //no, pairs hospital + resident
	    			slots[i]--;
	    		}
	    		else{
	    			//compares ranking of i to currently matched hospital
	    			if(resPref.get(res).indexOf(i) < resPref.get(res).indexOf(currentMatch)){
	    				//i is ranked higher
	    				match.set(res, i); //changes matching
	    				slots[i]--; //decrements free slots in i
	    				slots[currentMatch]++; //increments old match slots 
	    			}
	    		}
	    		
	
				hospPref.get(i).remove(0);
    		}
    		
    		if(slots[i]<=0)
    			i=(i+1)%hospSize; //cycles
    	}
    	
    	return new Matching(marriage, match);
    	
    }
    
    boolean hospitalFull(int[] slots){
    	for(int i : slots){
    		if(i>0)
    			return true;
    	}
    	return false;
    }
    
    boolean preferencesExhausted(ArrayList<ArrayList<Integer>> hospPref){
    	for(ArrayList<Integer> r : hospPref){
    		if(!r.isEmpty())
    			return true;
    	}
    	return false;
    }
}
