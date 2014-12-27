/*
 * CyclicBarrierTest.java by Yen-Jung Chang
 * 
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierTest {
	final static int SIZE = 50;
	final static int ROUND = 30;
	
	public static class TestThread implements Runnable {
		private final static AtomicInteger ticket = new AtomicInteger();
		private final int id = ticket.getAndIncrement();
		private final CyclicBarrier barrier_;
		
		public TestThread(CyclicBarrier barrier) {
			barrier_ = barrier;
		}

		@Override
		public void run() {
			int index = -1;

			for (int round = 0; round < ROUND; ++round) {
				System.out.println("Thread " + id + " is WAITING on round " + round);
				try {
					index = barrier_.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread " + id + " leaves round " + round + " with INDEX: " + index);
			}
		}
	}
	
	public static void main(String args[]) {
		for(int h = 0; h <1; h++){
		CyclicBarrier barrier = new CyclicBarrier(SIZE);
		ExecutorService pool = Executors.newCachedThreadPool(); 
		for (int i = 0; i < SIZE; ++i) pool.submit(new TestThread(barrier));
		pool.shutdown();
		System.out.println(h);
		}
	}

	/**********
	 1. The threads should be waiting on the same round. For example, the following interleaving
		   is NOT allowed:
		   Thread 1 is WAITING on round:0
		   Thread 2 is WAITING on round:1
		   Thread 3 is WAITING on round:0
		   
	 2. The return index of every thread should not be the same.
	 
	 Example output:
		Thread 0 is WAITING on round 0
		Thread 3 is WAITING on round 0
		Thread 1 is WAITING on round 0
		Thread 2 is WAITING on round 0
		Thread 4 is WAITING on round 0
		Thread 4 leaves round 0 with INDEX: 0
		Thread 3 leaves round 0 with INDEX: 3
		Thread 1 leaves round 0 with INDEX: 2
		Thread 1 is WAITING on round 1
		Thread 2 leaves round 0 with INDEX: 1
		Thread 3 is WAITING on round 1
		Thread 4 is WAITING on round 1
		Thread 0 leaves round 0 with INDEX: 4
		Thread 2 is WAITING on round 1
		Thread 0 is WAITING on round 1
		Thread 0 leaves round 1 with INDEX: 0
		Thread 3 leaves round 1 with INDEX: 3
		Thread 4 leaves round 1 with INDEX: 2
		Thread 2 leaves round 1 with INDEX: 1
		Thread 4 is WAITING on round 2
		Thread 3 is WAITING on round 2
		Thread 0 is WAITING on round 2
		Thread 1 leaves round 1 with INDEX: 4
		Thread 2 is WAITING on round 2
		Thread 1 is WAITING on round 2
		Thread 1 leaves round 2 with INDEX: 0
		Thread 3 leaves round 2 with INDEX: 3
		Thread 2 leaves round 2 with INDEX: 1
		Thread 4 leaves round 2 with INDEX: 4
		Thread 0 leaves round 2 with INDEX: 2
	 */
}
