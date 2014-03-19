import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
	public static void main(String args[]) throws Exception {
		int n;
		int connectedserver;
		String[] addrs;
		String addr;
		int port;
		InetAddress inaddr;
		Socket clientSocket;
		
		String send = "";
		String output;
		Scanner in = new Scanner(new File(args[0]));
		
		n = in.nextInt();
		connectedserver = 0;
		in.nextLine();
		
		addrs = new String[n];
		
		for (int i = 0; i < n; i++) {
			String str = in.nextLine();
			addrs[i] = str;
		}

		addr = addrs[connectedserver];
		port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		int len = 1024;
		inaddr = InetAddress.getByName(addr.substring(0, addr.indexOf(':')));

		// Client client = new Client(port,addr,addrs,w);

		while (true) {
			try {

				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				clientSocket = new Socket(inaddr, port);

				PrintWriter p = new PrintWriter(clientSocket.getOutputStream());

				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				if (in.hasNext()) {
					//changeserver();
					String[] commands = in.nextLine().split(" ");
					if (commands[1].charAt(0) >= '0' && commands[1].charAt(0) <= '9') {
						Thread.sleep(Integer.parseInt(commands[1]));
						//System.err.println("Exited Sleep");
						//continue;
					} else {
						send = commands[0].charAt(1) + " " + commands[1].charAt(1) + " " + commands[2];
						//System.err.println("SENDING " + send);
					
	
						p.println("CLIENT " + send);
						p.flush();
	
						output = inFromServer.readLine();
						//System.err.println("WAITING TO CHECK IF RECIEVED FROM SERVER");
						System.out.println(output);
					}
					// System.err.println("T: " + t++);
				}
				else break;
			} 
			catch (Exception e) {
				addr = addrs[(++connectedserver) % n];
				port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
				inaddr = InetAddress.getByName(addr.substring(0, addr.indexOf(':')));
				// clientSocket.close();
				//clientSocket = new Socket(inaddr, port);
			}

		}

	}
}
