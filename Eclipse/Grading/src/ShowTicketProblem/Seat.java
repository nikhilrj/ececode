package ShowTicketProblem;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Seat {

public String row;
public int column;
public boolean status = true ; 
public Client buyer;
Lock seatLock;
 
//constructor, creates a seat object with a lock
	public Seat (String row, int column, boolean status, Client buyer){
		this.row = row;
		this.column = column;
		this.status = status;
		this.buyer = buyer;
		this.seatLock = new ReentrantLock();
	}
	
}
