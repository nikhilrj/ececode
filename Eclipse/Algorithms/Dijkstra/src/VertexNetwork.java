import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class VertexNetwork {
    
    protected double            transmissionRange; // The transmission range of each vertex.
    protected ArrayList<Vertex> location;          // A list of vertices in the network.
    protected ArrayList<Edge>   edges;             // A list of edges in the network
    
    VertexNetwork() {
        /* This constructor creates an empty list of vertex locations,
	   an empty list of edges, and the transmission range is set to 0.0. */
        transmissionRange = 0.0;
        location          = new ArrayList<Vertex>(0);
        edges             = new ArrayList<Edge>(0);
    }
    
    VertexNetwork(String locationFile) {
        /* This constructor creates a list of vertex locations, and edges read 
           from the plain text file locationFile. The transmission 
           range of these vertices is set to 1.0. */
        this(1.0, locationFile);
    }
    
    VertexNetwork(String locationFile, double transmissionRange) {
        /* This constructor creates a list of vertex locations, and edges read 
           from the plain text file locationFile. The transmission 
           range of these vertices is set to transmissionRange. */
        this(transmissionRange, locationFile);
    }
    
    VertexNetwork(double transmissionRange, String locationFile) {
        /* This constructor creates a list of vertex locations, and edges read 
           from the plain text file locationFile. The transmission 
           range of these vertices is set to transmissionRange. */
        this.transmissionRange = transmissionRange;
        Scanner scan   = null;
        try {
            scan = new Scanner(new BufferedReader(new FileReader(locationFile)));
	    String[] networkSize = scan.nextLine().split(" ");

	    int numPoints = Integer.parseInt(networkSize[0]);
	    location = parsePoints(scan, numPoints);
	    edges = parseEdges(scan, numPoints);
        } catch (FileNotFoundException exception) {
            System.err.println("FileNotFoundException: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("IOException: " + exception.getMessage());
        } finally {
            if (scan != null) scan.close();
        }
    }

    private ArrayList<Vertex> parsePoints(Scanner scan, int numPoints) {
        /* This function parses the points in x-y form from the file */

        ArrayList<Vertex> location = new ArrayList<Vertex>(0);
        for (int i=0; i<numPoints; i++) {
	    location.add(new Vertex(scan.nextDouble(), scan.nextDouble()));
        }

        return location;
    }
    
    private ArrayList<Edge> parseEdges(Scanner scan, int numPoints) {
	/* This function parses the edges between vertices, from the file */

        ArrayList<Edge> edges = new ArrayList<Edge>(0);
        for (int i=0; i<numPoints; i++) {
        	for (int j=i+1; j<numPoints; j++) {
        		edges.add(new Edge(i, j, scan.nextDouble()));
        	}
        }

        return edges;
    }
    
    public void setTransmissionRange(double transmissionRange) {
        /* This method sets the transmission range to transmissionRange. */
        /* DO NOT FORGET to recompute your graph when you change the 
           transmissionRange to a new value. */
        this.transmissionRange = transmissionRange;
    }
    
    public abstract ArrayList<Vertex> gpsrPath(int sourceIndex, int sinkIndex) ;
    
    public abstract ArrayList<Vertex> dijkstraPathLatency(int sourceIndex, int sinkIndex) ;
    
    public abstract ArrayList<Vertex> dijkstraPathHops(int sourceIndex, int sinkIndex) ;
    
    public void gpsrAllPairs(boolean print) {
        /* This method calls the GPSR algorithm for all pairs of vertices and 
           displays the number of successful runs as well as the average time 
           taken for these successful runs. Note that the time measured is 
           system time and so you should run your code on a lightly loaded 
           computer to get consistent and meaningful timing results.
        /* DO NOT CHANGE the following code. */
        int  numSuccesses       = 0;
        long totalTimeSuccesses = 0;
        if (print) System.out.println("Paths between all pairs of vertices using the GPSR algorithm:");
        for (int i = 0; i < location.size(); i++) {
            for (int j = i+1; j < location.size(); j++) {
                long startTime           = System.nanoTime();
                ArrayList<Vertex> pathIJ = gpsrPath(i, j);
                long endTime             = System.nanoTime();
                if (!pathIJ.isEmpty()) {
                    numSuccesses++;
                    totalTimeSuccesses += (endTime - startTime);
                }
                if (print) System.out.println("I = " + i + " J = " + j + " : " + pathIJ.toString());
            }
        }
        System.out.println("The GPSR algorithm is successful " + numSuccesses + "/" + location.size()*(location.size()-1)/2 + " times.");
        if (numSuccesses != 0) {
            System.out.println("The average time taken by the GPSR algorithm on successful runs is " + totalTimeSuccesses/numSuccesses + " nanoseconds.");
        } else {
            System.out.println("The average time taken by the GPSR algorithm on successful runs is N/A nanoseconds.");
        }
        System.out.println("");
    }
    
    public void dijkstraLatencyAllPairs(boolean print) {
        /* This method calls Dijkstra's algorithm (for minimum latency) for all pairs
	   of vertices and displays the number of successful runs as well as the
	   average time taken for these successful runs. Note that the time measured
	   is system time and so you should run your code on a lightly loaded 
           computer to get consistent and meaningful timing results.
        /* DO NOT CHANGE the following code. */
        int  numSuccesses       = 0;
        long totalTimeSuccesses = 0;
        if (print) System.out.println("Paths between all pairs of vertices using Dijkstra's algorithm (Min Latency):");
        for (int i = 0; i < location.size(); i++) {
            for (int j = i+1; j < location.size(); j++) {
                long startTime           = System.nanoTime();
                ArrayList<Vertex> pathIJ = dijkstraPathLatency(i, j);
                long endTime             = System.nanoTime();
                if (!pathIJ.isEmpty()) {
                    numSuccesses++;
                    totalTimeSuccesses += (endTime - startTime);
                }
                if (print) System.out.println("I = " + i + " J = " + j + " : " + pathIJ.toString());
            }
        }
        System.out.println("Dijkstra's algorithm (Min Latency) is successful " + numSuccesses + "/" + location.size()*(location.size()-1)/2 + " times.");
        if (numSuccesses != 0) {
            System.out.println("The average time taken by Dijkstra's algorithm (Min Latency) on successful runs is " + totalTimeSuccesses/numSuccesses + " nanoseconds.");
        } else {
            System.out.println("The average time taken by Dijkstra's algorithm (Min Latency) on successful runs is N/A nanoseconds.");
        }
        System.out.println("");
    }
    
    public void dijkstraHopsAllPairs(boolean print) {
        /* This method calls Dijkstra's algorithm (for minimum hops) for all pairs
	   of vertices and displays the number of successful runs as well as the
	   average time taken for these successful runs. Note that the time measured
	   is system time and so you should run your code on a lightly loaded 
           computer to get consistent and meaningful timing results.
        /* DO NOT CHANGE the following code. */
        int  numSuccesses       = 0;
        long totalTimeSuccesses = 0;
        if (print) System.out.println("Paths between all pairs of vertices using Dijkstra's algorithm (Min Hops):");
        for (int i = 0; i < location.size(); i++) {
            for (int j = i+1; j < location.size(); j++) {
                long startTime           = System.nanoTime();
                ArrayList<Vertex> pathIJ = dijkstraPathHops(i, j);
                long endTime             = System.nanoTime();
                if (!pathIJ.isEmpty()) {
                    numSuccesses++;
                    totalTimeSuccesses += (endTime - startTime);
                }
                if (print) System.out.println("I = " + i + " J = " + j + " : " + pathIJ.toString());
            }
        }
        System.out.println("Dijkstra's algorithm (Min Hops) is successful " + numSuccesses + "/" + location.size()*(location.size()-1)/2 + " times.");
        if (numSuccesses != 0) {
            System.out.println("The average time taken by Dijkstra's algorithm (Min Hops) on successful runs is " + totalTimeSuccesses/numSuccesses + " nanoseconds.");
        } else {
            System.out.println("The average time taken by Dijkstra's algorithm (Min Hops) on successful runs is N/A nanoseconds.");
        }
        System.out.println("");
    }
    

}

