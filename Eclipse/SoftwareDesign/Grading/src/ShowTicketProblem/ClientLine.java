package ShowTicketProblem;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ClientLine{
	public int count = 0;
	public Queue<Client> line = new LinkedList<Client>();
	
	//constructor: creates a queue of clients to be attended to
	public ClientLine(){		
		Random rand = new Random();
		int num = rand.nextInt(900) + 100;
		while(count != num){
			Client c = new Client(count + 1);
			line.add(c);
			count++;
		}
	}
	
	//method to add more clients to line in case line gets too short
	public void moreClients(){
		Random random = new Random();
		int numb = random.nextInt(100);
		int newCount = count + numb;
		while(count != newCount){
			Client c = new Client(count + 1);
			line.add(c);
			count++;
		}
	}
	
}