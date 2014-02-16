package Assignment0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.System.out;

public class Assignment0 {

	public static void main(String[] args) {
		
		//ArrayList Demo
		
		ArrayList<Integer> intList = new ArrayList<Integer>();
		int[] intArr = new int[10];
		
		//randomizes array
		for(int i = 0; i < 10; i++){
			int randNum = (int)(Math.random()*100);
			
			intArr[i] = randNum;
			intList.add(randNum);
		}
		
		//printing arraylist
		for(int i = 0; i < 10; i++){
			out.print("array: " + intArr[i]);
			out.print(" ; ArrayList: " + intList.get(i) + "\n");
		}
		
		//remove an element
		intList.remove(9);
		
		//Sorting
		Collections.sort(intList);
		
		//checking if an object is contained
		out.println(intList.contains(5));
		out.println(intList.contains(intList.get(0)));
	
		//easy for loop
		for(int i : intList){
			out.println(i);
		}
		
		//Other functions
		Collections.reverse(intList);;
		int min = Collections.min(intList);
		int max = Collections.max(intList);
		
	}

}
