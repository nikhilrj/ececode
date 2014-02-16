package assignment6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class RadixSort {
	private int[] rands;

	static StopWatch sw = new StopWatch();
	ArrayList<LinkedList<radixInteger>> solution;
	
	RadixSort() {
		rands = new int[50000];
		Random r = new Random(17);
		
		for(int i = 0; i < 50000; i++){
			int rand = r.nextInt(Integer.MAX_VALUE);
			rands[i] = rand;
		}
		
	}
	
	RadixSort(int[] inp){
		rands = inp;
	}
	
	void initialize(){
		solution = new ArrayList<LinkedList<radixInteger>>();
		for(int i = 0; i < 10; i++){
			solution.add(new LinkedList<radixInteger>());
		}
		
		for(int i = 0; i < rands.length; i++){
			solution.get(0).add(new radixInteger(rands[i]));
		}
	}
	
	void RadixSorter(){
		initialize();
		ArrayList<LinkedList<radixInteger>> temp;
		
		sw.reset();
		sw.start();
		
		for(int i = 0; i < 10; i++){
			temp = new ArrayList<LinkedList<radixInteger>>();
			for(int j = 0; j < 10; j++){
				temp.add(new LinkedList<radixInteger>());
			}
			while(!solution.isEmpty()){
				if(solution.get(0).isEmpty()){
					solution.remove(0);
				}
				else{
					radixInteger a = solution.get(0).remove();
					temp.get(a.radix%10).add(a);
					a.radix/=10;
				}
			}
			solution = temp;
		}
		sw.stop();
		System.out.println("Radix Sort Run Time: : \t\t" + sw.getElapsedTime() + " ns");
		
		int index = 0;
		while(!solution.isEmpty()){
			if(solution.get(0).isEmpty()){
				solution.remove(0);
			}
			else{
				radixInteger a = solution.get(0).remove();
				rands[index] = a.real;
				index++;
			}
		}
	}
	
	void JCFSorter(){
		sw.reset();
		sw.start();
		Arrays.sort(rands);
		sw.stop();
		System.out.println("Finished Arrays.sort in : \t" + sw.getElapsedTime() + " ns");
	
	}
	
	void HeapSort(){
		sw.reset();
		sw.start();
		PriorityQueue<Integer> a = new PriorityQueue<Integer>();
		for(int j : rands){
			a.add(j);
		}
		sw.stop();
		for(int i = 0; i < rands.length; i++){
			rands[i] = a.remove();
		}
		
		System.out.println("\n(extra-Finished Heap-Sort in :\t" + sw.getElapsedTime() + " ns)");
	}
	

	public int[] getArray() {
		return rands;
	}
}

class radixInteger {
	int real;
	int radix;
	radixInteger(int a){
		real = a;
		radix = a;
	}
	radixInteger(int a, int b){
		real = a;
		radix = b;
	}
}