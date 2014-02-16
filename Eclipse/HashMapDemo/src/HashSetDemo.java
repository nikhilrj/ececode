import java.util.ArrayList;
import java.util.HashSet;


public class HashSetDemo {

	public static void main(String[] args) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		HashSet<Integer> hs = new HashSet<Integer>();
		
		for(int i = 0; i < 1000000; i++){
			int rand = (int) (Math.random()*100000);
			a.add(rand);
			hs.add(rand);
		}
		
		StopWatch sw = new StopWatch();
		sw.start();
		System.out.println(a.contains(555));
		sw.stop();
		System.out.println("Array lookup: " + sw.getElapsedTime() + " ns");
		sw.reset();
		
		sw.start();
		System.out.println(hs.contains(555));
		sw.stop();
		System.out.println("Hash lookup: " + sw.getElapsedTime() + " ns");
	}

}
