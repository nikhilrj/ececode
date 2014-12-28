import java.util.Arrays;


public class Program3 implements IProgram3 {
	
	int[][] solution;
	int numClasses;
	int maxGrade;
	GradeFunction gradeF;
	
    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3.
     */
    public void initialize(int nc, int mg, GradeFunction gf){
    	gradeF = gf;
    	numClasses = nc;
    	maxGrade = mg;
    }


    /**
     * My computeHours, builds solution based on selection[][]
     */
    public int[] computeHours(int totalHours){
    	int[][] sol = computeSolution(totalHours);
    	int[] hours = new int[numClasses];
    	int row = numClasses-1;
    	int col = totalHours;
    	int tempHours = totalHours;
    	
    	while(row>=0 && tempHours > 0){
    		if(sol[row][col]==0){
    			row--;
    		}
    		else{
    			hours[row]=sol[row][col];
    			tempHours-=sol[row][col];
    			col-=sol[row][col];
    			row--;
    		}
    	}
    	
    	return hours;
    }

    
    /**
     * builds solution by calling computeHours and calculating grades
     */
    public int[] computeGrades(int totalHours){
    	int[] hours = computeHours(totalHours);
    	int[] grades = new int[hours.length];
    	for(int i = 0; i < hours.length; i++){
    		grades[i] = gradeF.grade(i, hours[i]);
    	}
    	return grades;
    }
    
    /**
     * Helper method for computeGrades / computeHours
     * Dynamic programming algorithm to find optimal solution to final grade problem
     *  
     * @param totalHours
     * @return
     */
    private int[][] computeSolution(int totalHours){
    	//solution will build our optimal solution
    	int[][] solution = new int[numClasses][totalHours+1];
    	//selection will store information on how our optimal solution was built
    	int[][] selection = new int[numClasses][totalHours+1];
    	
    	for(int i = 0; i < numClasses; i++){
    		for(int j = 0; j < totalHours+1; j++){
    			if(i>=1){
    				int max = -1;
    				int tempk = 0;
    				for(int k = 0; k <= j; k++){
    					int optSmaller = solution[i-1][j-k] + gradeF.grade(i, k);
    					if(optSmaller >= max){
    						max = optSmaller;
    						tempk = k;
    					}
    				}
    				if(gradeF.grade(i,j) >= max){
    					solution[i][j] = gradeF.grade(i, j);
    					selection[i][j] = j;
    				}
    				else{
    					solution[i][j] = max;
    					selection[i][j] = tempk;
    				}
    			}
    			else{
    				solution[i][j] = gradeF.grade(i, j);
    				selection[i][j] = j;
    			}
    		}
    	}
    	
    	
    	for(int[] lol : solution)
    		System.err.println(Arrays.toString(lol));
    	System.err.println();
    	for(int[] lol : selection)
    		System.err.println(Arrays.toString(lol));
    	
    	
    	return selection;
    }

}
