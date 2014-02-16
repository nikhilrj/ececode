/*
 * DequeTest.java by Yen-Jung Chang
 * 
 */

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class DequeTest {
	final static int CAPACITY = 2;
	Deque que = null;
		
	public static class TestThread implements Runnable {
		final private static AtomicInteger ticket = new AtomicInteger();
		final private int id = ticket.getAndIncrement();
		final Deque que_;
		
		TestThread(Deque que) {
			que_ = que;
		}
		
		@Override
		public void run() {
			Random ran = new Random();
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
            try {
			int head = ran.nextInt(100);
			System.out.println("Thread " + id + " is inserting head: " + head);
			que_.insertHead(head);
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is peeking head: " + que_.getHead());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is removing Head: " + que_.deleteHead());
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int tail = ran.nextInt(100);
			System.out.println("Thread " + id + " is inserting tail: " + tail);
			que_.insertTail(tail);
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is peeking tail: " + que_.getTail());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is removing tail: " + que_.deleteTail());
			System.out.println("Que: " + que_.toString());
            } catch (Exception e){
                e.printStackTrace();
            }
		}
	}
	
	public static void main(String[] argc) {
		Deque deque = new Deque(CAPACITY);
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 20; ++i) pool.submit(new TestThread(deque));
		pool.shutdown();
	}
	
	/********
	There is no certain guide line for checking the output. You only need to make sure the
	output looks appropriate. 
	
	Example output:
		Thread 2 is inserting head: 49
		Thread 0 is inserting head: 27
		Que: [ 27 49 ]
		Que: [ 27 49 ]
		Thread 3 is inserting head: 28
		Thread 0 is peeking head: 27
		Thread 2 is peeking head: 27
		Thread 1 is inserting head: 22
		Thread 2 is removing Head: 27
		Que: [ 28 49 ]
		Que: [ 28 49 ]
		Thread 0 is removing Head: 28
		Que: [ 22 49 ]
		Que: [ 22 49 ]
		Thread 3 is peeking head: 22
		Thread 0 is inserting tail: 46
		Thread 3 is removing Head: 22
		Que: [ 49 46 ]
		Que: [ 49 46 ]
		Thread 2 is inserting tail: 86
		Thread 3 is inserting tail: 62
		Thread 1 is peeking head: 49
		Thread 0 is peeking tail: 46
		Thread 1 is removing Head: 49
		Que: [ 46 86 ]
		Que: [ 46 86 ]
		Thread 0 is removing tail: 86
		Que: [ 46 62 ]
		Que: [ 46 62 ]
		Thread 1 is inserting tail: 68
		Thread 3 is peeking tail: 62
		Thread 2 is peeking tail: 62
		Thread 2 is removing tail: 62
		Que: [ 46 68 ]
		Que: [ 46 68 ]
		Thread 3 is removing tail: 68
		Que: [ 46 ]
		Thread 1 is peeking tail: 46
		Thread 1 is removing tail: 46
		Que: [ ]
	 */

}
