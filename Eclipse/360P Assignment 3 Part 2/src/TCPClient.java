import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {

	public static void main(String args[]) throws Exception {
		String send = "";
		String modifiedSentence;

		Scanner in = new Scanner(new File(args[0]));
		int n = in.nextInt();
		in.nextLine();
		String addr = in.nextLine();
		int port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		int len = 1024;
		String hostname = "localhost";
		int t = 1;

		while (true) {
			try {
				if (in.hasNext()) {
					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					Socket clientSocket = new Socket(hostname, port);
					PrintWriter p = new PrintWriter(clientSocket.getOutputStream());
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					String[] commands = in.nextLine().split(" ");
					if (commands[1].charAt(0) >= '0'
							&& commands[1].charAt(0) <= '9') {
						Thread.sleep(Integer.parseInt(commands[1]));
						System.err.println("Entered Sleep");
					} 
					else {
						send = commands[0].charAt(1) + " "
								+ commands[1].charAt(1) + " " + commands[2];
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
}