package assignment6;

public class A6Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RadixSort a = new RadixSort();
		a.RadixSorter();
		RadixSort b = new RadixSort();
		b.JCFSorter();
		RadixSort h = new RadixSort();
		h.HeapSort();
		
		for(int i = 0; i < a.getArray().length; i++){
			if(a.getArray()[i]!=b.getArray()[i] && a.getArray()[i]!=h.getArray()[i])
				throw new Error("Radix sort failure!");
		}
		
		System.out.println("\nSuccessful Sorting!");
	}

}
