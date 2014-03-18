import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	public static void main(String args[]) throws Exception {
		String send = "";
		String modifiedSentence;

		Scanner in = new Scanner(new File(args[0]));
		int n = in.nextInt();
		int w = 0;
		in.nextLine();
		String[] addrs = new String[n];
		for (int i = 0; i < n; i++) {
			addrs[n] = in.nextLine();
		}

		String addr = addrs[w];
		int port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		int len = 1024;
		InetAddress inaddr = InetAddress.getByName(addr.substring(0,addr.indexOf(':')));
		int t = 1;

		while (true) {
			try {
				if (in.hasNext()) {
					if (changeserver(w, addrs)) {
						addr = addrs[(++w) % n];
						port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
						inaddr = InetAddress.getByName(addr.substring(0, addr.indexOf(':')));
					}
					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					Socket clientSocket = new Socket(inaddr, port);

					PrintWriter p = new PrintWriter(clientSocket.getOutputStream());

					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					String[] commands = in.nextLine().split(" ");
					if (commands[1].charAt(0) >= '0'&& commands[1].charAt(0) <= '9') {
						Thread.sleep(Integer.parseInt(commands[1]));
						System.err.println("Entered Sleep");
					} 
					else {
						send = commands[0].charAt(1) + " "	+ commands[1].charAt(1) + " " + commands[2];
						System.err.println("SENDING " + send);
					}

					p.println(send);
					p.flush();

					modifiedSentence = inFromServer.readLine();
					System.err.println("WAITING TO CHECK IF RECIEVED FROM SERVER");
					System.out.println(modifiedSentence);
					System.err.println("T: " + t++);
				}
			} catch (Exception e) {
			}

		}

	}

	private static boolean changeserver(int w, String[] addrs) {
		// TODO Auto-generated method stub
		return false;
	}
}