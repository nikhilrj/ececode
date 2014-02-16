/*
 * FairReadWriteLockTest.java by Yen-Jung Chang
 * 
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FairReadWriteLockTest implements Runnable {
	private static AtomicInteger ticket = new AtomicInteger();
	private FairReadWriteLock lock;
	private int id;
	
	public FairReadWriteLockTest(FairReadWriteLock lock) {
		this.lock = lock;
		this.id = ticket.incrementAndGet();
	}
	
	public void run() {
		if (id % 5 == 0) { // a writer
			lock.beginWrite();
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Thread " + id + " is WRITING.");
			
			lock.endWrite();
		} else { // a reader
			lock.beginRead();
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Thread " + id + " is reading.");
			
			lock.endRead();
		}
	}
	
	static public void main(String args[]) {
		ExecutorService pool = Executors.newCachedThreadPool();
		FairReadWriteLock lock = new FairReadWriteLock();
		for (int i = 0; i < 30; ++i) {
			pool.submit(new FairReadWriteLockTest(lock));
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
		try {
			if (!pool.awaitTermination(3, TimeUnit.SECONDS)) pool.shutdownNow();
		} catch (InterruptedException e) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	
	/**************************
	Every 4 reads is separated by one write. You do not need to make "exactly" 4 reads, because 
	every execution may be different due to the thread scheduling from the system.
	
	Example output:
		Thread 1 is reading.
		Thread 2 is reading.
		Thread 3 is reading.
		Thread 4 is reading.
		Thread 5 is WRITING.
		Thread 6 is reading.
		Thread 7 is reading.
		Thread 8 is reading.
		Thread 9 is reading.
		Thread 10 is WRITING.
		Thread 11 is reading.
		Thread 12 is reading.
		Thread 13 is reading.
		Thread 14 is reading.
		Thread 15 is WRITING.
		Thread 16 is reading.
		Thread 17 is reading.
		Thread 18 is reading.
		Thread 19 is reading.
		Thread 20 is WRITING.
		Thread 21 is reading.
		Thread 22 is reading.
		Thread 23 is reading.
		Thread 24 is reading.
		Thread 25 is WRITING.
		Thread 26 is reading.
		Thread 27 is reading.
		Thread 28 is reading.
		Thread 29 is reading.
		Thread 30 is WRITING.
	 */
}
