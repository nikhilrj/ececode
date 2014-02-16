import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Result result = JUnitCore.runClasses(TranslatorTest.class);
		int i =0;
		for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	         i++;
	      }
	      System.out.println(i);
	   }

}
