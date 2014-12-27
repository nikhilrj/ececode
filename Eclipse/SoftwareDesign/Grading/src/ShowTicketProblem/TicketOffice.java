package ShowTicketProblem;

//This creates a runnable object. For each Ticket Office

public class TicketOffice implements Runnable{
	
	public ClientLine Clients;	//each office has own line of clients
	public String name;
	public Theater th;		
	
	//constructor: will share theater, have different names and client lines
	public TicketOffice (Theater t, String n) {
		th = t;
		name = n;
		Clients = new ClientLine();
	}
	
	public void run () {
		try {
			while(th.availableSeats != 0){
				while(Clients.line.size() > 0){
					Client c = Clients.line.poll();	//gets next client
					Seat s = th.BestAvailableSeat(name, th, c);
					if(s != null){					
						th.printTicketSeat(s, this);
					}
					Thread.sleep(1);				//add delay
				}
			}
		} 
		catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
}
