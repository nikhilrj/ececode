import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
	public static void main(String args[]) throws Exception {
		String input;
		String output;

		Scanner in = new Scanner(new File(args[0]));
		int n = in.nextInt();

		int[] books = new int[in.nextInt() + 1];

		in.nextLine();
		String addr = in.nextLine();
		int port = Integer.parseInt(addr.substring(addr.indexOf(':') + 1));
		int len = 1024;
		in.close();

		ServerSocket sock = new ServerSocket(port, len,	InetAddress.getByName("localhost"));
		System.out.println("Server started at " + sock.getLocalPort());

		while (true) {
			Socket server = sock.accept();
			Scanner inp = new Scanner(server.getInputStream());
			int t = 1;
			PrintWriter p = new PrintWriter(server.getOutputStream());
			
			try {
				input = inp.nextLine();
				System.err.println("Received: " + input);
				System.err.println("Status " + Arrays.toString(books) + "\n");

				output = "";
				String[] commands = input.split(" ");
				int client = Integer.parseInt(commands[0]);
				int booknum = Integer.parseInt(commands[1]);
				if (booknum > 0 && booknum < books.length
						&& commands[2].equals("reserve") && books[booknum] <= 0) {
					books[booknum] = client;
					output = "c" + client + " " + "b" + booknum;
				} else if (commands[2].equals("return")
						&& books[booknum] == client) {
					books[Integer.parseInt(commands[1])] = 0;
					output = "free c" + client + " " + "b" + booknum;
				} else {
					output = "fail c" + client + " " + "b" + booknum;
				}

				System.err.println("SENDING MESSAGE: " + output	+ " TO ADDRESS: " + sock.getInetAddress().toString() + " PORT: " + sock.getLocalPort());
				
				p.println(output);
				p.flush();
			} catch (Exception e) {
			}

		}

	}
}
