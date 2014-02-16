import java.util.Arrays;


public class PSort implements Runnable {
	
	private static final int ARRAY_LENGTH = 5500;
	int[] array;

	public PSort(int[] a){
		this.array = a;
	}
	
	public void run(){
		if(array.length <= 2){
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
			//System.out.println(Arrays.toString(mt1.array) + Arrays.toString(mt2.array));
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
		
		p.start();
		try {
			p.join();
		}
		catch(InterruptedException e){};
		System.out.println(Arrays.toString(mt.array));
		Arrays.sort(A);
		System.out.println((Arrays.toString(A)));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a[] = new int[ARRAY_LENGTH];
		for(int i = 0; i < a.length; i++){
			a[i] = (int)(Math.random()*Integer.MAX_VALUE);
		}
		
		parallelSort(a, 0, ARRAY_LENGTH);
	}

}