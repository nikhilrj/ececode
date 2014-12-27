/*
 * Yunjian (Erik) Mao and Nawel Boumerdassi
 * Lab: Tues 3-5:30 PM
 * Last Updated: 12/8/2013
 */

package ShowTicketProblem;

public class DriverClass {

	public static void main(String[] args) {
		Theater th = new Theater();

		TicketOffice tARunnable = new TicketOffice(th, "BoxOffice A");
		TicketOffice tBRunnable = new TicketOffice(th, "BoxOffice B");
		Thread tOfficeA = new Thread(tARunnable);
		Thread tOfficeB = new Thread(tBRunnable);
		
		tOfficeA.start();
		tOfficeB.start();
		
		while(th.availableSeats != 0){				
			if(tARunnable.Clients.line.size() < 100){	//if line too short, add more clients
				tARunnable.Clients.moreClients();
			}
			if(tBRunnable.Clients.line.size() < 100){
				tBRunnable.Clients.moreClients();
			}
		}
		System.out.println("All tickets sold out");
	}

}
