import java.net.*;
import java.io.*;
import java.util.*;

import javax.rmi.CORBA.Util;

public class TCPServer {
	NameTable table;

	public TCPServer() {
		table = new NameTable();
	}

	public static void main(String[] args) {
		TCPServer ns = new TCPServer();
		System.out.println("NameServer started:");
		try {
			ServerSocket listener = new ServerSocket(Symbols.ServerPort);
			Socket s;
			while ((s = listener.accept()) != null) {
				Thread t = new ServerThread(ns.table, s);
				t.start();
			}
		} catch (IOException e) {
			System.err.println("Server aborted:" + e);
		}
	}
}

class NameTable {
    class NameEntry {
        public String procName;
        public InetSocketAddress addr;
        public NameEntry(String pName, String host, int port){
           procName = pName;
           addr = new InetSocketAddress(host, port);
        }
      }
    ArrayList<NameEntry> table = new ArrayList<NameEntry>();
    public synchronized InetSocketAddress search(String s) {
        for (NameEntry entry: table)
            if (s.equals(entry.procName)) return entry.addr;
        return null;
    }
    // returns 0 if old value replaced, otherwise 1
    public synchronized int insert(String s, String hostName, int portNumber) {
        int retValue = 1;
        for (NameEntry entry: table)
            if (s.equals(entry.procName)) {
                table.remove(entry);
                retValue = 0;
            }
        table.add(new NameEntry(s,hostName, portNumber));
        notifyAll();
        return retValue;
    }
    public synchronized InetSocketAddress blockingFind(String s) {
         InetSocketAddress addr = search(s);
         while (addr == null) {
                Util.myWait(this);
                addr = search(s);
         }
         return addr;
    }
    public synchronized void clear() {
         table.clear();
    }
}