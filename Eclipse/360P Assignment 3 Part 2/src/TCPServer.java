import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
	
	public static void main(String args[]) throws Exception {
		String[] turnOff;
		int clientsRun=-1;
		int sleepTime=0;
		
		int SERVER_ID = args[0].charAt(6)-'0';
		if(SERVER_ID < 0)
			SERVER_ID = 1;
		
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
		
		if(in.hasNext()){
			turnOff = in.nextLine().split(" ");
			clientsRun = Integer.parseInt(turnOff[1]);
			sleepTime = Integer.parseInt(turnOff[2]);
		}
		
		int len = 1024;
		in.close();
		
		ServerSocket sock = new ServerSocket(ports[SERVER_ID], len,	InetAddress.getByName("localhost"));

		while(true){
			if(clientsRun == Server.clientsRun){
				System.out.println("GONNA SLEEP");
				//sock.close();
				Thread.sleep(sleepTime);
				//sock = new ServerSocket(ports[SERVER_ID], len,	InetAddress.getByName("localhost"));
				Server server = new Server();
				clientsRun--;
				//server.update();
			}
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
    static volatile int clientsRun = 0;
    //static volatile int acksReceived;
    Request myReq;
    //private static volatile int runOnce = 1;
    
    Server() throws IOException{
    	System.out.println("GONNA UPDATE NOW");
    	update();
    }
        
    Server(ServerSocket sock, int[] ports, String[] addresses, int[] books, int sid) throws IOException{
        Server.ports = ports;
        Server.addresses = addresses;
        Server.sock = sock;
        Server.books = books;
        Server.SERVER_ID = sid;
        Server.SERVERS_LIVE = ports.length-1;
		server = sock.accept();
    }
    
    void update(){
    	String input;
    	//while(!requestCS())	{}
    	System.out.println("UPDATED");
    	
    	serverNetwork = new Socket[ports.length];
        SERVERS_LIVE = ports.length-1;
        for(int i = 1; i < ports.length; i++){
        	if(i!=SERVER_ID){
	        	try {
					serverNetwork[i] = new Socket(InetAddress.getByName(addresses[i]), ports[i]);
					PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(serverNetwork[i].getOutputStream()));
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverNetwork[i].getInputStream()));
					
					outToServer.println("UPDATE");
					outToServer.flush();
					
					input = inFromServer.readLine();
					System.err.println("Received: " + input);
					
					//String[] up = input.split(" "); 
					
					for(int j = 1; j < books.length; j++){
						books[j] = Integer.parseInt(input.charAt(j-1)+"");
					}
					break;
					
	        		  
				} catch (IOException e) {
					System.out.println("DETECED SERVER " + i + " IS DOWN!!");
					SERVERS_LIVE--;
				}
        	}
		}
        System.out.println("UPDATE WORKED!! " + Arrays.toString(books));
    }
    

    /**
     * Requests Critical Section for Server
     * @return
     */
	private synchronized boolean requestCS() {
		sendToServers("SERVER req " + SERVER_ID + " " + lc.getValue());
		myReq = new Request(lc.getValue(), SERVER_ID);
		pq.add(myReq);
		
		//TODO: add the acksReceived to myReq???
		while(myReq == null || !pq.peek().equals(myReq) || pq.peek().acksReceived < SERVERS_LIVE-1){} //blocks
		
		System.out.println("made it past CS!");
		return true;
	}
	
	private void releaseCS(String cat)  {
		System.out.println("releasing CS!");
		//acksReceived = 0;
		pq.remove(myReq);
		sendToServers("SERVER rel " + SERVER_ID + " " + lc.sendAction() + cat);
	}
    
    /**
     * Sends msg to every server that is still alive
     * @param msg
     */
	public void sendToServers(String msg) {
		System.err.println("Sending " + msg + " to all alive servers");
        serverNetwork = new Socket[ports.length];
        SERVERS_LIVE = ports.length-1;
        for(int i = 1; i < ports.length; i++){
        	if(i!=SERVER_ID){
	        	try {
					serverNetwork[i] = new Socket(InetAddress.getByName(addresses[i]), ports[i]);
					PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(serverNetwork[i].getOutputStream()));
					outToServer.println(msg);
					outToServer.flush();
				} catch (IOException e) {
					System.out.println("DETECED SERVER " + i + " IS DOWN!!");
					SERVERS_LIVE--;
				}
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
		} catch (Exception e) {
			System.out.println("SERVER WAS DOWN");
			//e.printStackTrace();
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
			System.out.println("\n\nRECEIVED" + Arrays.toString(commands) + "\n");
			sendToServer(serv, "SERVER ack " + lc.sendAction() + " " + Integer.parseInt(commands[3])); //TODO see if this is bad or not
		}
		else if(commands[1].equals("ack")){
			//if(myReq.lc < Integer.parseInt(commands[2]))
			//	acksReceived++;
			Iterator<Request> it = pq.iterator();
			while(it.hasNext()){
				Request rq = (Request) it.next();
				if(rq.SERVER_NUM == SERVER_ID && rq.lc < Integer.parseInt(commands[2]) ){//&& rq.lc == Integer.parseInt(commands[3])+runOnce){//== myReq.lc){
					rq.acksReceived++;
					//runOnce = 0;
				}
			}
		}
		else if(commands[1].equals("rel")){
			if(commands[4].equals("1")){
				books[Integer.parseInt(commands[5])] = Integer.parseInt(commands[6]);
			}
			else if(commands[4].equals("2")){
				books[Integer.parseInt(commands[5])] = 0;
			}
			
			Iterator<Request> it = pq.iterator();
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
		lc.tick();
		
		try {
			Scanner inp = new Scanner(server.getInputStream());
			PrintWriter p = new PrintWriter(server.getOutputStream());
			
			input = inp.nextLine();
			System.err.println("Received: " + input);

			if(input.contains("SERVER")){
				serverHandler(input);
			}
			else if(input.contains("CLIENT")) { // TODO MUST BE A CLIENT MESSAGE
				clientsRun++;
				p.println(clientOutput(input));
			}
			else if(input.contains("UPDATE")){
				Thread.sleep(1000);
				String out = "";
				for(int i = 1; i < books.length; i++){
					out+=books[i];
				}
				System.out.println("Sending update " + out);
				p.println(out);
			}

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
	int acksReceived = 0;
	
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
