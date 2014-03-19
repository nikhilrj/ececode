import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	static int n;
	static int connectedserver;
	static String[] addrs;
	static String addr;
	static int port;
	static InetAddress inaddr;
	static Socket clientSocket;
	
	private static void readinput(Scanner in) throws UnknownHostException {
		n = in.nextInt();
		connectedserver = 0;
		in.nextLine();
		addrs = new String[n];
		for (int i = 0; i < n; i++) {
			addrs[n] = in.nextLine();
		}

		addr = addrs[connectedserver];
		port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		int len = 1024;
		inaddr = InetAddress.getByName(addr.substring(0, addr.indexOf(':')));
	}

	private static void changeserver() throws IOException {
		addr = addrs[(++connectedserver) % n];
		port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		inaddr = InetAddress.getByAddress((addr.substring(0, addr.indexOf(':'))).getBytes());
		// clientSocket.close();
		clientSocket = new Socket(inaddr, port);
	}

	public static void main(String args[]) throws Exception {
		String send = "";
		String modifiedSentence;
		Scanner in = new Scanner(new File(args[0]));
		readinput(in);

		// Client client = new Client(port,addr,addrs,w);

		while (true) {
			try {

				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				clientSocket = new Socket(inaddr, port);

				PrintWriter p = new PrintWriter(clientSocket.getOutputStream());

				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				if (in.hasNext()) {
					changeserver();
					String[] commands = in.nextLine().split(" ");
					if (commands[1].charAt(0) >= '0' && commands[1].charAt(0) <= '9') {
						Thread.sleep(Integer.parseInt(commands[1]));
						System.err.println("Entered Sleep");
					} else {
						send = commands[0].charAt(1) + " " + commands[1].charAt(1) + " " + commands[2];
						System.err.println("SENDING " + send);
					}

					p.println("CLIENT " + send);
					p.flush();

					modifiedSentence = inFromServer.readLine();
					System.err.println("WAITING TO CHECK IF RECIEVED FROM SERVER");
					System.out.println(modifiedSentence);
					// System.err.println("T: " + t++);
				}
			} catch (Exception e) {
				changeserver(); // catch the exception and change it
			}

		}

	}
}
