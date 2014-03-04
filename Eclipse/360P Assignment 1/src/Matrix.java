/**
 * Nikhil Joglekar
 * Thejas Prasad
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;


public class Matrix implements Callable<Double> {
	int row, col;
	private static double[][] a;
	private static double[][] b;
	
	/*
	 * Row of A and Col of B that need to dotted by current thread
	 */
	public Matrix(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/*
	 * Computes dot product of inputs
	 */
	private double dotProduct(double[] a, double[] b){
		double temp = 0;
		for(int i = 0; i < a.length; i++){
			temp += (a[i] * b[i]);
		}
		return temp;
	}
	
	/*
	 * Transposes Column Vector to Row Vector for computing dot product
	 */
	private double[] getCol(int col){
		double[] a = new double[b.length];
		for(int i = 0; i < b.length; i++){
			a[i] = b[i][col];
		}
		return a;
	}
	
	/**
	 * Computes dot product of specific rows based on the row / col specific to thread
	 */
	public Double call() {
		return dotProduct(a[row], getCol(col));
	}

	/*
	 *  C = A*B
	 * 
	 *  Uses CachedThreadPool to execute new threads
	 *  One thread created per location in C
	 *  If the number of threads currently execution exceeds numThreads,
	 *  .get() the oldest thread executing, and place the result in C.
	 *
	 *	Once threads based on locations of C are done executing,
	 *	Fill in locations in C based on thread returns 
	 * 	(uses helper class MatrixHelper to link location in C to executing thread)
	 *  
	 */
	public static boolean MatrixMult(int rowA, int colA, double[][] A,int rowB, int colB, double[][] B, double[][] C, int numThreads) {
		if(colA!=rowB){
			return false;
		}

		a = A;
		b = B;
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		ArrayList<MatrixHelper> fList = new ArrayList<MatrixHelper>();
		int threadCounter = 0;
		
		for(int i = 0; i < rowA; i++){
			for(int j = 0; j < colB; j++){
				if(threadCounter<numThreads){
					Future<Double> fv = es.submit(new Matrix(i, j));
					MatrixHelper mh = new MatrixHelper(fv, i, j);
					fList.add(mh);
					threadCounter++;
				}
				else{
					try{
						C[fList.get(0).getRow()][fList.get(0).getCol()] = fList.get(0).getFv().get();
					}
					catch (InterruptedException e) { e.printStackTrace();} 
					catch (ExecutionException e) {e.printStackTrace();}
					fList.remove(0);
					threadCounter--;
					j--;
				}
			}
		}

		for(MatrixHelper f : fList){
			try{
				C[f.getRow()][f.getCol()] = f.getFv().get();
			}
			catch (InterruptedException e) { e.printStackTrace();} 
			catch (ExecutionException e) {e.printStackTrace();}
		}
		
		es.shutdown();
		return true;
	}
	

	public static void main(String[] args) {
		int rowsA = 5;
		int colsA = 3;
		int rowsB = 3;
		int colsB = 6;
		int threads = 16;
		int weight = 10;
		
		double[][] A = new double[rowsA][colsA];
		
		for(int i = 0; i < rowsA; i++){
			for(int j = 0; j < colsA; j++){
				A[i][j] = (Math.random()*weight - weight/2);
			}
			System.err.println(Arrays.toString(A[i]));
		}
		
		System.out.println();
		double[][] B = new double[rowsB][colsB];
		
		for(int i = 0; i < rowsB; i++){
			for(int j = 0; j < colsB; j++){
				B[i][j] = (Math.random()*weight - weight/2);
			}
			System.err.println(Arrays.toString(B[i]));
		}
		
		long a = System.nanoTime();
		
		System.out.println();
		double[][] C = new double[rowsA][colsB];
		double[][] C2 = new double[rowsA][colsB];
		MatrixMult(rowsA, colsA, A, rowsB, colsB, B, C, threads);
		
		//System.out.println(System.nanoTime() - a );
		
		for(double[] i : C){
			System.err.println(Arrays.toString(i));
		}
		
		/*
		System.out.println();
		
		a = System.nanoTime();
		for (int i=0; i<rowsA; ++i)
		    for (int j=0; j<colsB; ++j)
		      for (int k=0; k<colsA; ++k)
		    	 C2[i][j] += A[i][k] * B[k][j];
		
		//System.out.println(System.nanoTime() - a );
		
		for(double[] i : C2){
			System.out.println(Arrays.toString(i));
		}
		*/
		
	}
}

class MatrixHelper {
	private Future<Double> fv;
	private int row;
	private int col;
	
	public Future<Double> getFv() {
		return fv;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	MatrixHelper(Future<Double> fv, int row, int col){
		this.fv = fv;
		this.row = row;
		this.col = col;
	}
}
