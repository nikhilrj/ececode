import java.util.Arrays;


public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Program3 hi = new Program3();
		int classes = 10;
		int maxGrade = 100;
		int hours = 25;
		hi.initialize(classes, maxGrade, new GradeFunction1(classes, maxGrade, hours));
		/*int[][] a = hi.computeSolution(7);
		//int[] b = hi.computeHours(7);
		for(int[] b : a)
			System.out.println(Arrays.toString(b));*/
		System.out.println("Hours[] : "  + Arrays.toString(hi.computeHours(hours)) + "\nGrades[] : " + Arrays.toString(hi.computeGrades(hours)));
	}

}
