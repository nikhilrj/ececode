import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class UDPServer {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner in = new Scanner(new File("server.in"));
		int n = in.nextInt();	
		int[] books = new int[in.nextInt()+1]; //thejas cheating like a bro
		in.nextLine();
		
		String addr = in.nextLine();
		int port = Integer.parseInt(addr.substring(addr.indexOf(':')+1));
		int len = 1024;
		String hostname = "localhost"; // should this be 127..?
		in.close();

		DatagramPacket sPacket = null, rPacket;
		
		try {
			InetAddress ia = InetAddress.getByName(hostname);
			DatagramSocket datasocket = new DatagramSocket(port,ia);
			System.out.println("Server started at " + ia.toString() + ":" + port);
			Scanner sc = new Scanner(System.in);
			int t = 1; 
			while (true) {
				//RECEIVING FROM CLIENTS
				if(t == 1){
				ServerThread server = new ServerThread(datasocket,books); 
				new Thread(server).start();
				t++; 
				}
			}
		}
		catch (UnknownHostException e) {
			System.err.println(e);
		} 
		catch (SocketException se) {
			System.err.println(se);
		}
	}

	
		// TODO Auto-generated method stub
		
	} 

class ServerThread implements Runnable 
{
	int len; 
    DatagramSocket datasocket = null; 
    int[] books; 
    DatagramPacket sPacket = null, rPacket;
	public ServerThread(DatagramSocket datasocket, int[] books) {
		// TODO Auto-generated constructor stub
		this.datasocket = datasocket; 
		len = 1024;
		this.books = books;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte[] rbuffer = new byte[len];
		rPacket = new DatagramPacket(rbuffer, rbuffer.length);
		try {
			datasocket.receive(rPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String command = new String(rPacket.getData(), 0, rPacket.getLength());
		InetAddress IPAddress = rPacket.getAddress();
		System.out.println("SERVER RECEIVED: " + command + " FROM ADDRESS: " + IPAddress.getHostName() + " PORT: " + rPacket.getPort());
		InetAddress ia = null;
		try {
			ia = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			parseandreturnpacket(command,books,sPacket,ia,rPacket,datasocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static synchronized void parseandreturnpacket(String command, int[] books, DatagramPacket sPacket, InetAddress ia, DatagramPacket rPacket, DatagramSocket datasocket) throws IOException {
		String ret;
		String[] commands = command.split(" ");
		int client = Integer.parseInt(commands[0]);
		int booknum = Integer.parseInt(commands[1]);
		if(booknum>0 && booknum<books.length && commands[2].equals("reserve") && books[booknum]<=0){
			books[booknum] = client;
			ret = "c" + client + " " + "b" + booknum;
			System.out.println("IT IS RESERVING: " + ret);
		}
		else if(commands[2].equals("return") && books[booknum]==client){
			books[Integer.parseInt(commands[1])] = 0	;
			ret = "free c" + client + " " + "b" + booknum;
			System.out.println("IT IS RETURNING: " + ret);
		}
		else{
			ret = "fail c" + client + " " + "b" + booknum;
		}
		
		if(ret != null){
		System.out.println("ATTEMPTING TO SEND : " + ret);
			byte[] buffer = new byte[ret.length()];
			buffer = ret.getBytes();
			sPacket = new DatagramPacket(buffer, buffer.length, ia, rPacket.getPort());
			datasocket.send(sPacket);
			System.out.println("MESSAGE SENT");
		}
	
}
}
