import java.util.concurrent.Semaphore;

public class CyclicBarrier {
    Semaphore count;
    Semaphore wait;
    boolean releaser = false;
    private int parties;
    private int index;
    int generation; 
    
    public CyclicBarrier(int parties) {
    	this.parties = parties;
    	this.index = 0;
    	this.generation = 1; 
    	count = new Semaphore(1);
    	wait = new Semaphore(1);
    	
    	try {
    		wait.acquire();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }
   
    public int await() throws InterruptedException {
    	while(generation == 0); 
    	
    	count.acquire();
    	index++;
    	
    	if(index==parties){
    		wait.release();
    	}
    	count.release();

    	
    	wait.acquire();
    	generation = 0; 
    	count.acquire();
    	index--;
    	
    	if(index==0){
    		releaser = true;
        }
    	count.release();
    	
    	if(releaser){
    		releaser = false;
    		generation = 1;
    	}	
    	else{
    		wait.release();
    	}
    	
    	return parties - index - 1;
           
    }
}