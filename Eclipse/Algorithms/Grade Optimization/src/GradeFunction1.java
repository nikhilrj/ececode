import java.util.Arrays;
import java.util.Random;


public class GradeFunction1 implements GradeFunction {
    private int numClasses;
    private int maxGrade;
    int[][] gradeMap;

    public GradeFunction1(int n, int g, int h){
    	this.numClasses = n;
    	this.maxGrade = g;
    	int[][] gradeMap2 = {
    		{0, 1,2,3,4,5,6,7},
    		{0, 0,0,60,60,60,60,60},
    		{0, 8,61,61,61,61,61,61},
    		{0, 0,0,0,90,90,90,90}
    	};    	
    	Random r = new Random(13);
    	gradeMap = new int[n][h+1];
    	for(int i = 0; i < n; i++){
    		for(int j = 1; j <= h; j++){
    			gradeMap[i][j] = r.nextInt(15) + gradeMap[i][j-1];
    		}
    		//System.out.println(Arrays.toString(gradeMap[i]));
    	}
    	//gradeMap = gradeMap2;
    }
    
    public GradeFunction1(int n, int g){
    	this.numClasses = n;
    	maxGrade = g;
    }
    
	public int grade(int classID, int hours) {
		return Math.min(gradeMap[classID][hours], maxGrade);
		
		//return Math.min(hours + classID, maxGrade);
	    	
		//return Math.min(hours*classID, maxGrade);
	}

}
