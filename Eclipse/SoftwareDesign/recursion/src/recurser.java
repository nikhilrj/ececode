
public class recurser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(factorial(5));
		System.out.println(squareRoot(49,0,10000));
		combinations("", "hey");
		
		
	}

	/**
	 * @param i
	 * @return i!
	 */
	static int factorial(int i) {
		return -1;
	}
	
	/**
	 * 
	 * @param d
	 * @return sqrt(d) 
	 * USE RECURSION
	 * HINT: Set of floating point numbers is finite
	 */
	static double squareRoot(double d, double l, double h){
	
		double mid = (l+h)/2;
		
		if(mid*mid==d)
			return mid;
		
		if(mid*mid>d){
			return squareRoot(d, l, mid);
		}
		else
			return squareRoot(d, mid, h);
	}
	
	/**
	 * 
	 * @param suffix
	 * @param prefix
	 * 
	 * HINTS: Think about why you're getting a suffix and prefix
	 * Print the statement inside the 
	 */
	static void combinations(String suffix,String prefix){
        if(prefix.length()<0)
        	return;
        
        System.out.println(suffix);
        
        for(int i=0;i<prefix.length();i++)
        	combinations(suffix+prefix.charAt(i),prefix.substring(i+1,prefix.length()));

    }
	/**
	 * Example output for combinations("","hey"):
	 * 	
		h
		he
		hey
		hy
		e
		ey
		y

	 */
	
}
