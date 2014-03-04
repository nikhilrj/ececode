import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class UDPServer {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("server.in"));
		int n = in.nextInt();	
		int[] books = new int[in.nextInt()+1]; //thejas cheating like a bro
		in.nextLine();
		
		String s = in.nextLine();
		int port = Integer.parseInt(s.substring(s.indexOf(':')+1));
		int len = 1024;
		String hostname = "localhost";
		in.close();

		DatagramPacket sPacket, rPacket;
		
		try {
			InetAddress ia = InetAddress.getByName(hostname);
			DatagramSocket datasocket = new DatagramSocket();
			Scanner sc = new Scanner(System.in);
			
			while (true) {
				try {
					
					//RECEIVING FROM CLIENTS
					byte[] rbuffer = new byte[len];
					rPacket = new DatagramPacket(rbuffer, rbuffer.length);
					datasocket.receive(rPacket);
					String command = new String(rPacket.getData(), 0, rPacket.getLength());
					System.out.println("SERVER RECEIVED : " + command);
					
					String ret;
					
					String[] commands = command.split(" ");
					int client = Integer.parseInt(commands[0]);
					int booknum = Integer.parseInt(commands[1]);
					if(booknum>0 && booknum<books.length && commands[2].equals("reserve") && books[booknum]<=0){
						books[booknum] = client;
						ret = "c" + client + " " + "b" + booknum;
					}
					else if(commands[2].equals("return") && books[booknum]==client){
						books[Integer.parseInt(commands[1])] = 0;
						ret = "free c" + client + " " + "b" + booknum;	
					}
					else{
						ret = "fail c" + client + " " + "b" + booknum;
					}
					
					if(ret != null){
						byte[] buffer = new byte[ret.length()];
						buffer = ret.getBytes();
						sPacket = new DatagramPacket(buffer, buffer.length, ia, port);
						datasocket.send(sPacket);
					}
				} 
				catch (IOException e) {
					System.err.println(e);
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
}