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
		}

		int len = 1024;
		in.close();
		
		ServerSocket sock = new ServerSocket(ports[SERVER_ID], len,	InetAddress.getByName("localhost"));

		while(true){
			System.err.println("Thread " + SERVER_ID + " started at " + sock.getLocalPort());
			Server server = new Server(sock, ports, addresses, books, SERVER_ID);
			new Thread(server).start();
		}
	}
}

class Server implements Runnable {
	
	static ServerSocket sock;
    static Socket server;
    static Socket[] serverNetwork;
    static int[] ports;
    static String[] addresses;
    static int SERVER_ID;
    static int SERVERS_LIVE;
    static volatile int[] books;
    static volatile PriorityQueue<Request> pq = new PriorityQueue<Request>();
    static volatile LamportClock lc = new LamportClock();
    static volatile int acksReceived;
    static volatile Request myReq;
        
    Server(ServerSocket sock, int[] ports, String[] addresses, int[] books, int sid) throws IOException{
        this.ports = ports;
        this.addresses = addresses;
        this.sock = sock;
        this.books = books;
        this.SERVER_ID = sid;
        this.SERVERS_LIVE = ports.length-1;
		server = sock.accept();
    }
    

	private synchronized boolean requestCS() throws Exception {
		sendToServers("SERVER req " + SERVER_ID + " " + lc.getValue());
		myReq = new Request(lc.getValue(), SERVER_ID);
		pq.add(myReq);
		while(myReq == null || acksReceived < SERVERS_LIVE-1 || !pq.peek().equals(myReq)){} //blocks
		
		System.out.println("made it past CS!");
		return true;
	}
	
	private void releaseCS(String cat) throws Exception {
		System.out.println("releasing CS!");
		acksReceived = 0;
		pq.remove(myReq);
		sendToServers("SERVER rel " + SERVER_ID + " " + lc.sendAction() + cat);//TODO how to make this work??
	}
    
    /**
     * Sends msg to every server that is still alive
     * @param msg
     */
	public void sendToServers(String msg) throws Exception {
		System.err.println("Sending " + msg + " to all alive servers");
        serverNetwork = new Socket[ports.length];
        for(int i = 1; i < ports.length; i++){
        	if(i!=SERVER_ID){
	        	try {
					serverNetwork[i] = new Socket(InetAddress.getByName(addresses[i]), ports[i]);
					PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(serverNetwork[i].getOutputStream()));
					outToServer.println(msg);
					outToServer.flush();
				} catch (Exception e) {	}
        	}
		}
	}
	
	/**
	 * Sends msg to input server id
	 * @param id
	 * @param msg
	 */
	public void sendToServer(int id, String msg){
		System.err.println("Sending " + msg + " to server " + id);
		try {
			Socket con = new Socket(InetAddress.getByName(addresses[id]), ports[id]);
			PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(con.getOutputStream()));
			outToServer.println(msg);
			outToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles server input
	 * @param input
	 */
	private void serverHandler(String input){
		System.out.println(input);
		String[] commands = input.split(" ");
		
		if(commands.length==4 && commands[1].equals("req")){
			lc.receiveAction(Integer.parseInt(commands[3]));
			int serv = Integer.parseInt(commands[2]);
			Request req = new Request(lc.getValue(), serv);
			pq.add(req);
			sendToServer(serv, "SERVER ack " + lc.sendAction()); //TODO see if this is bad or not
		}
		else if(commands[1].equals("ack")){
			acksReceived++;
		}
		else if(commands[1].equals("rel")){
			if(commands[4].equals("1")){
				books[Integer.parseInt(commands[5])] = Integer.parseInt(commands[6]);
			}
			else if(commands[4].equals("2")){
				books[Integer.parseInt(commands[5])] = 0;
			}
			
			Iterator it = pq.iterator();
			while(it.hasNext()){
				Request rq = (Request) it.next();
				if(rq.SERVER_NUM == Integer.parseInt(commands[2]) && rq.lc == Integer.parseInt(commands[3]))
					System.out.println("removed server " + rq.SERVER_NUM + "'s request from queue");
					pq.remove(rq);
			}
		}
	}
	
	/**
	 * Handles client output
	 * @param input
	 * @return string to output to client
	 */
	private String clientOutput(String input) throws Exception{
		String output;
		String[] commands = input.split(" ");
		
		int client = Integer.parseInt(commands[1]);
		int booknum = Integer.parseInt(commands[2]);
		if (booknum > 0 && booknum < books.length && commands[3].equals("reserve") && books[booknum] <= 0) {
			while(!requestCS())	{}
			books[booknum] = client;
			output = "c" + client + " " + "b" + booknum;
			releaseCS(" 1 " + booknum + " " + client);
		} 
		else if (commands[3].equals("return") && books[booknum] == client) {
			while(!requestCS()) {}
			//books[Integer.parseInt(commands[1])] = 0; //TODO i changed this
			books[booknum] = 0;
			output = "free c" + client + " " + "b" + booknum;
			releaseCS(" 2 " + booknum);
		} else {
			output = "fail c" + client + " " + "b" + booknum;
		}
		
		System.out.println(output);
		return output;
	}
    
	public void run() {
		String input;
		
		try {
			Scanner inp = new Scanner(server.getInputStream());
			int t = 1;
			PrintWriter p = new PrintWriter(server.getOutputStream());
			
			input = inp.nextLine();
			System.err.println("Received: " + input);

			if(input.contains("SERVER")){
				serverHandler(input);
			}
			else if(input.contains("CLIENT")) { // TODO MUST BE A CLIENT MESSAGE
				p.println(clientOutput(input));
			}


			//System.err.println("SENDING MESSAGE TO ADDRESS: " + sock.getInetAddress().toString() + " PORT: " + sock.getLocalPort() + "\n");
			//p.println(output);
			p.flush();

		} catch (Exception e) {}

		System.err.println("Status " + Arrays.toString(books));
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
    public int tick() {
        c = c + 1;
        return c;
    }
    public int sendAction() {
        c = c + 1;
        return c;
    }
    public int receiveAction(int sentValue) {
        if(c > sentValue)
        	c = c + 1;
        else
        	c = sentValue +1;
        return c;
    }
}

class Request implements Comparable<Request> {
	int lc;
	int SERVER_NUM;
	
	Request(int lc, int sn){
		this.lc = lc;
		SERVER_NUM = sn;
	}
	
	//TODO check to see if this is backwards
	public int compareTo(Request r) {
		return Integer.compare(lc, r.lc);
	}
	
	public boolean equals(Request r){
		if(r.SERVER_NUM == SERVER_NUM && r.lc == lc)
			return true;
		return false;
	}
}
