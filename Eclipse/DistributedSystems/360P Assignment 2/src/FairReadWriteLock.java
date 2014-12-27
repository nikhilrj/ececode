
public class FairReadWriteLock {
	private static int numReaders = 0;
	private static int numWriters = 0;
	private static boolean writeLock = false;
	
	synchronized void beginRead(){
		while(writeLock || numWriters > 0){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		numReaders++;
	}
	synchronized void endRead(){
		//if(numReaders>0){
			numReaders--;
		//}
		notifyAll();
	}
	
	synchronized void beginWrite(){
		numWriters++;
		while(numReaders>0 || writeLock){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		numWriters--;
		writeLock = true;;
	}
	synchronized void endWrite(){
		writeLock=false;
		notifyAll();
	}
}
