/**
 * Nikhil Joglekar
 * Thejas Prasad
 */

import java.util.Arrays;


public class PSort implements Runnable {
	
	private static final int ARRAY_LENGTH = 4096;
	int[] array;

	public PSort(int[] a){
		this.array = a;
	}
	
	public void run(){

		/**
		 * Note: Using the first if statement to replace the 2nd will prevent
		 * java.lang.OutOfMemoryError: unable to create new native thread,
		 * which we believe is being caused by our OS capping the number of threads
		 * from being created.
		 * 
		 * Sorting after reaching the last 16 elements will mean that 2^(n-3), 
		 * instead of 2^n threads will be created, where n is # of elements.
		 * This is enough to prevent the outofmemory error, and also gives us 
		 * a noticeable performance improvement.
		 * 
		 */

		/* USE THIS TO REPLACE 2nd TO FIX OUTOFMEM ERRORS
		/*if(array.length <= 8){
			Arrays.sort(array);
		}*/
		if(array.length <=2){
			if(array.length > 1)
			if(array[0] > array[1]){
				int temp = array[0];
				array[0] = array[1];
				array[1] = temp;
			}
		}
		else{
			PSort mt1 = new PSort(Arrays.copyOfRange(array, 0, array.length/2));
			PSort mt2 = new PSort(Arrays.copyOfRange(array, array.length/2, array.length));
			Thread t1 = new Thread(mt1);
			Thread t2 = new Thread(mt2);
			
			t1.start();
			t2.start();
			
			try{
				t1.join();
				t2.join();
			}
			catch(InterruptedException e){};
			int a = 0;
			int b = 0;
			
			for(int i = 0; i < array.length; i++){
				if(a == mt1.array.length){
					array[i] = mt2.array[b];
					b++;
				}
				else if(b == mt2.array.length){
					array[i] = mt1.array[a];
					a++;
				}
				else if(mt1.array[a] < mt2.array[b]){
					array[i] = mt1.array[a];
					a++;
				}
				else {
					array[i] = mt2.array[b];
					b++;
				}
			}
			
		}
	}

	public static void parallelSort(int[] A, int begin, int end){
		PSort mt = new PSort(A);
		Thread p = new Thread(mt);
		long a = System.nanoTime();
		p.start();
		try {
			p.join();
		}
		catch(InterruptedException e){};

		System.out.println(System.nanoTime()-a);
		
		System.out.println(Arrays.toString(mt.array));
		a = System.nanoTime();
		
		Arrays.sort(A);
		System.out.println(System.nanoTime()-a);
		
		System.out.println((Arrays.toString(A)));
	}
	
	public static void main(String[] args) {
		int a[] = new int[ARRAY_LENGTH];
		for(int i = 0; i < a.length; i++){
			a[i] = (int)(Math.random()*Integer.MAX_VALUE);
		}
		
		parallelSort(a, 0, ARRAY_LENGTH);
	}

}