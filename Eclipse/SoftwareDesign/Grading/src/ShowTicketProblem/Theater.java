package ShowTicketProblem;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Theater {

	public final int totalSeats;
	public int availableSeats;
	public char currentSeatRow;
	public Seat[][] theater;		//2D array to fill theater with seats
	public int count = 0;
	
	//constructor, builds the theater
	public Theater (){
		totalSeats = 749;
		availableSeats = 749;
		currentSeatRow = 'A';
		theater = new Seat[27][28];
		char row = 'A';
		for(int i = 0; i < 27; i++){
			for(int j = 0; j < 28; j++){
				if(i == 26)
					theater[i][j] = new Seat("AA", j + 101, true, null);
				else{
					theater[i][j] = new Seat("" + row, j + 101, true, null);
				}
				
			}
			row++;
		}
	}

	//This method will find the next best available seat
	//inputs: name of ticket office, theater object, and client object
	//output: seat object of the best seat 
	public Seat BestAvailableSeat(String name, Theater th, Client cl){
		if (th.availableSeats == 0 || cl == null) {
			return null;
		}
		
		for (int i = 7; i<=20 ; i++){
			Seat s = th.theater[th.currentSeatRow-65][i];
			if (s.status){
			try {
				s.seatLock.lock();
				if(s.status == false){		//if both try to reserve same seat
					System.out.println(name + ": Failed to reserve " + s.row + s.column + ". Already allocated");
				}
				if (s.status){
					th.availableSeats--;
					s.status = false;
					s.buyer = cl;
					return s ;
				}
			
			}			
			finally{
				s.seatLock.unlock();	
			}
			
			}
		}
		if (th.currentSeatRow == 'A'){
			for (int i =3; i<=6 ; i++){
				Seat s = th.theater[th.currentSeatRow-65][i];
				if (s.status){
				try {
					s.seatLock.lock();
					if(s.status == false){
						System.out.println(name + ": Failed to reserve " + s.row + s.column + ". Already allocated");
					}
					if (s.status){
						th.availableSeats--;
						s.status = false;
						s.buyer = cl;
						return s ;
					}
				
				}			
				finally{
					s.seatLock.unlock();
				}
				
				}
			}
			for (int i =21; i<=23 ; i++){
				Seat s = th.theater[th.currentSeatRow-65][i];
				if (s.status){
				try {
					s.seatLock.lock();
					if(s.status == false){
						System.out.println(name + ": Failed to reserve " + s.row + s.column + ". Already allocated");
					}
					if (s.status){
						th.availableSeats--;
						s.status = false;
						s.buyer = cl;
					return s ;
					}
				
				}			
				finally{
					s.seatLock.unlock();
				}
				
				}
			}
		}
		else {
			for (int i =0; i<=6 ; i++){
				Seat s = th.theater[th.currentSeatRow-65][i];
				if (s.status){
				try {
					s.seatLock.lock();
					if(s.status == false){
						System.out.println(name + ": Failed to reserve " + s.row + s.column + ". Already allocated");
					}
					if (s.status){
						th.availableSeats--;
						s.status = false;
						s.buyer = cl;
						return s ;
					}
				}			
				finally{
					s.seatLock.unlock();
				}
				
				}
			}
			
			for (int i = 21; i <= 27 ; i++){
				Seat s = th.theater[th.currentSeatRow-65][i];
				if (s.status){
				try {
					s.seatLock.lock();
					if(s.status == false){
						System.out.println(name + ": Failed to reserve " + s.row + s.column + ". Already allocated");
					}
					if (s.status){
						th.availableSeats--;
						s.status = false;
						s.buyer = cl;
						return s ;
					}
				
				}			
				finally{
					s.seatLock.unlock();
				}
				
				}
			}
		}
		th.currentSeatRow++;	
		return BestAvailableSeat(name, th, cl);	//recurse until available seat is found or show is full
	}
	

	//This method will output a ticket seat
	//inputs: seat object and ticket office object
	//outputs: a ticket with information for seat number and client number
	public void printTicketSeat (Seat s, TicketOffice tO) {
		Calendar date = Calendar.getInstance();
		System.out.format("%s : Booked seat %s%d at %tl:%tM %tp on %tD for client %d%n", 
				tO.name, s.row,s.column,date,date,date,date,s.buyer.iD);
	}
	
}
