import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
	
	public static void main(String args[]) throws Exception {
		String input;
		String output;
		
		int SERVER_ID = args[0].charAt(6)-'0';
		
		Scanner in = new Scanner(new File(args[0])); //server 0...n passed in here
		
		int[] ports = new int[in.nextInt() + 1];
		String[] addresses = new String[ports.length];
		
		int[] books = new int[in.nextInt() + 1]; //books[0] = never used
		in.nextLine();
		
		for(int i = 1; i < ports.length; i++){
			String addr = in.nextLine();
			addresses[i] = addr.substring(0,addr.indexOf(':'));
			ports[i] = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
			//servers[i] = new Socket("localhost", );
		}

		int len = 1024;
		in.close();

		ServerSocket sock = new ServerSocket(ports[SERVER_ID], len,	InetAddress.getByName("localhost"));
		System.out.println("Server " + SERVER_ID + " started at " + sock.getLocalPort());
		Server server = new Server(sock, ports, addresses, books);
	}
}

class Server extends Thread{
	
	ServerSocket sock;
    Socket connectionSocket;
    Socket[] serverNetwork;
    int[] ports;
    String[] addresses;
    int[] books;
    
    //private static PriorityQueue<>
    
    Server(ServerSocket sock, int[] ports, String[] addresses, int[] books) throws IOException{
        this.ports = ports;
        this.addresses = addresses;
        this.sock = sock;
        this.books = books;
        this.start();
    }
    
	public void sendToServers(String msg) throws Exception {
        serverNetwork = new Socket[ports.length];
        for(int i = 1; i < ports.length; i++){
        	try {
				serverNetwork[i] = new Socket(InetAddress.getByName(addresses[i]), ports[i]);
				PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(serverNetwork[i].getOutputStream()));
				outToServer.println(msg);
				outToServer.flush();
			} catch (Exception e) {	}
		}
	}
	
	private String clientOutput(String input){
		while(!requestCS())	{}
		
		String output;
		String[] commands = input.split(" ");
		
		int client = Integer.parseInt(commands[0]);
		int booknum = Integer.parseInt(commands[1]);
		if (booknum > 0 && booknum < books.length && commands[2].equals("reserve") && books[booknum] <= 0) {
			books[booknum] = client;
			output = "c" + client + " " + "b" + booknum;
		} else if (commands[2].equals("return")
				&& books[booknum] == client) {
			books[Integer.parseInt(commands[1])] = 0;
			output = "free c" + client + " " + "b" + booknum;
		} else {
			output = "fail c" + client + " " + "b" + booknum;
		}
		
		releaseCS();
		return output;
	}
    
	private void releaseCS() {
		
	}

	private boolean requestCS() {
		return true;
	}

	public void run() {
		try {
			connectionSocket = sock.accept();
			
			// create input stream attached to socket
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			// create output stream attached to socket
			PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
			
			// read in line from the socket
			String input;
			//while ((input = inFromClient.readLine()) != null) {
			input = inFromClient.readLine();
				System.out.println("Client sent: " + input);
				// process


				System.err.println("Received: " + input);
				System.err.println("Status " + Arrays.toString(books) + "\n");


				
				if(input.contains("SERVER")){
				 
				}
				else { // MUST BE A CLIENT MESSAGE
					outToClient.print(clientOutput(input));
				}
					
				outToClient.flush();
			//}
		} catch (Exception e) {}
	}
}

class LamportClock {
    int c;
    public LamportClock() {
        c = 1;
    }
    public int getValue() {
        return c;
    }
    public void tick() {
        c = c + 1;
    }
    public void sendAction() {
        c = c + 1;
    }
    public void receiveAction(int src, int sentValue) {
        if(c > sentValue)
        	c = c + 1;
        else
        	c = sentValue +1;
    }
}
