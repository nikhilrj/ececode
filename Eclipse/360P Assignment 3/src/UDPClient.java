import java.net.*;
import java.io.*;
import java.util.Scanner;

public class UDPClient {
	public static void main(String[] args) throws FileNotFoundException, NumberFormatException, InterruptedException {
		Scanner in = new Scanner(new File(args[0]));
		int n = in.nextInt();	
		in.nextLine();
		String addr = in.nextLine();
		int port = Integer.parseInt(addr.substring(addr.indexOf(':')+1));
		int len = 1024;
		String hostname = "localhost";
		
		DatagramPacket sPacket, rPacket;

		try {
			InetAddress ia = InetAddress.getByName(hostname);
			DatagramSocket datasocket = new DatagramSocket();
			
			while(true) {
				try {
					 
					if(in.hasNext()){
					
					
						String[] commands = in.nextLine().split(" ");
						
						if(commands[1].charAt(0) >= '0' && commands[1].charAt(0) <= '9')
						{	Thread.sleep(Integer.parseInt(commands[1]));
						//System.out.println("Entered Sleep");
						continue; 
						}
						String send = commands[0].charAt(1) + " " + commands[1].charAt(1) + " " + commands[2];
						//System.out.println("SENDING:" + send);
						
						byte[] buffer = new byte[send.length()];
						buffer = send.getBytes();
						sPacket = new DatagramPacket(buffer, buffer.length, ia, port);
						datasocket.send(sPacket);
						//System.out.println("Message Sent");
					
						
					byte[] rbuffer = new byte[len];
					rPacket = new DatagramPacket(rbuffer, rbuffer.length);
					datasocket.receive(rPacket);
					String retstring = new String(rPacket.getData(), 0, rPacket.getLength());
					System.out.println(retstring);
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
		in.close();
	}
}

