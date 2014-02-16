public interface IProgram3{

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3.
     */
    public void initialize(int numClasses, int maxGrade, GradeFunction gf);

    /*
     * This method returns an array that is the size of the number of classes.
     * Entry i in the array is the (integer) number of hours I should spend on 
     * class i (0 <= i <= n-1) in maximizing all of my grades.
     */
    public int[] computeHours(int totalHours);

    /*
     * This method does basically the same thing, but instead of telling me the
     * number of hours to spend on class i, it tells me what grade I will get if
     * I spend the optimal number of hours.
     */
    public int[] computeGrades(int totalHours);

}