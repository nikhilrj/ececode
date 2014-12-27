import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Deque {
	
	private LinkedList<Integer> list;
	private int capacity;
	ReentrantLock rl = new ReentrantLock();
	Condition notEmpty = rl.newCondition();
	Condition notFull = rl.newCondition();

	// Creates a double ended queue with maximum allowed capacity of integers.
	public Deque(int capacity){
		list = new LinkedList<Integer>();
		this.capacity = capacity;
	}
	
	// Returns the first value in the queue. Blocked if the queue is empty.
	@SuppressWarnings("finally")
	public int getHead(){
		int ret = -1;
		rl.lock();
		try{
			while(list.size()<=0){
				notEmpty.await();
			}
			ret = list.getFirst();
			//empty.signalAll();
		}
		finally{
			rl.unlock();
			return ret;//list.getFirst();		
		}
		
	}

	// Inserts the specified value at the beginning of this queue.
	// Blocked if the queue is full.
	public void insertHead(int value){
		rl.lock();
		try{
			while(list.size()>=capacity){
				try {
					notFull.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.addFirst(value);
			notEmpty.signalAll();
		}
		finally{
			rl.unlock();
		}
	}
	
	// Returns and deletes the first value in the queue.
	// Blocked if the queue is empty.
	@SuppressWarnings("finally")
	public int deleteHead(){
		int ret = -1;
		rl.lock();
		try{
			while(list.size()<=0){
				notEmpty.await();
			}
			ret = list.pop();
			notFull.signalAll();
		}
		finally{
			rl.unlock();
			return ret;		
		}
		
	}
	
	// Returns the last value in the queue. Blocked if the queue is empty.
	@SuppressWarnings("finally")
	public int getTail(){
		rl.lock();
		try{
			while(list.size()<=0){
				notEmpty.await();
			}
			//empty.signalAll();
		}
		finally{
			rl.unlock();
			return list.getLast();		
		}
		
	}
	
	// Appends the specified value to the end of this queue.
	// Blocked if the queue is full.
	public void insertTail(int value){
		rl.lock();
		try{
			while(list.size()>=capacity){
				try {
					notFull.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.addLast(value);
			notEmpty.signalAll();
		}
		finally{
			rl.unlock();
		}
		
	}

	// Returns and deletes the last value in the queue.
	// Blocked if the queue is empty.
	@SuppressWarnings("finally")
	public int deleteTail(){
		int ret = -1;
		rl.lock();
		try{
			while(list.size()<=0){
				notEmpty.await();
			}
			ret = list.removeLast();
			notFull.signalAll();
		}
		finally{
			rl.unlock();
			return ret;//list.removeLast();		
		}
		
		
	}

	// Returns a string represents the Deque. For example, if the Deque contains two
	// integers, 1 and 10, then the string should look like: [ 1 10 ].
	public String toString(){
		rl.lock();
		String str = list.toString();
		rl.unlock();
		return str;
		
	}
}
