
public class ConcreteRandomGrade implements GradeFunction{

    private int numClasses;
    private int maxGrade;

    public ConcreteRandomGrade(int n, int g){
    	this.numClasses = n;
    	this.maxGrade = g;
    }

    /*
     * This grade function is dumb: for every hour you spend on any course
     * you get one grade "point"
     */
    public int grade(int classID, int hours){
    	return Math.min(hours, maxGrade);
    }

}