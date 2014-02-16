/*
 * Name: Nikhil Joglekar
 * EID: nrj328
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/* Your solution goes in this file.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */

public class Program2 extends VertexNetwork {
    /* DO NOT FORGET to add a graph representation and 
       any other fields and/or methods that you think 
       will be useful. 
       DO NOT FORGET to modify the constructors when you 
       add new fields to the Program2 class. */
    
	ArrayList<ArrayList<Integer>> adjacencyMatrix;
	
    Program2() {
        super();
        initMatrix();
    }
    
    Program2(String locationFile) {
        super(locationFile);
        initMatrix();
    }
    
    Program2(String locationFile, double transmissionRange) {
        super(locationFile, transmissionRange);
        initMatrix();
    }
    
    Program2(double transmissionRange, String locationFile) {
        super(transmissionRange, locationFile);
        initMatrix();
    }
    
    public void setTransmissionRange(double transmissionRange) {
        /* This method sets the transmission range to transmissionRange. */
        /* DO NOT FORGET to recompute your graph when you change the 
           transmissionRange to a new value. */
        this.transmissionRange = transmissionRange;
        initMatrix();
    }
    
    void initMatrix(){
    	adjacencyMatrix = new ArrayList<ArrayList<Integer>>();

       	for(int i = 0; i < location.size(); i++){
    		adjacencyMatrix.add(new ArrayList<Integer>());
    	}
    	
    	for(int i = 0; i < location.size(); i++){
    		for(int j = 0; j < location.size(); j++){
	    		if(i!=j){
    			double dist = location.get(i).distance(location.get(j));
	    		if(dist <= transmissionRange){
		    			if(!adjacencyMatrix.get(i).contains(j))
		    				adjacencyMatrix.get(i).add(j);
		    			if(!adjacencyMatrix.get(j).contains(i))
		    				adjacencyMatrix.get(j).add(i);
	    			}
	    		}
    		}
    	}
    }
    
    /* This method returns a path from a source at location sourceIndex 
    and a sink at location sinkIndex using the GPSR algorithm. An empty 
    path is returned if the GPSR algorithm fails to find a path. */
    /* The following code is meant to be a placeholder that simply 
    returns an empty path. Replace it with your own code that 
    implements the GPSR algorithm. */
    public ArrayList<Vertex> gpsrPath(int sourceIndex, int sinkIndex) {
    	ArrayList<Vertex> path = new ArrayList<Vertex>();
    	ArrayList<Integer> visited = new ArrayList<Integer>();
    	
     	boolean correctPath = false;
     	int currentIndex = sourceIndex;
     	int nextIndex = -2;
     	    	
     	search:
     	while(true){
         	if(currentIndex == sinkIndex){
         		correctPath = true;
         		break;
         	}
         	
         	if(nextIndex == -1) break;
         	ArrayList<Integer> adjacents = adjacencyMatrix.get(currentIndex);
         	
         	if(adjacents.isEmpty())
         		break;

         	nextIndex = -1;
         	double currentDist = location.get(currentIndex).distance(location.get(sinkIndex));
         	
         	boolean allContained = true;
         	
         	for(int i = 0; i < adjacents.size(); i++){
         		if(!visited.contains(adjacents.get(i))){
         			allContained = false;
         		}
         	}
         	if(allContained) break search;
         	
         	for(int i = 0; i < adjacents.size(); i++){
         		double tempDist = location.get(adjacents.get(i)).distance(location.get(sinkIndex));
	       		if(tempDist < currentDist && !visited.contains(adjacents.get(i))){
	       			currentDist = tempDist;
	         		nextIndex = adjacents.get(i);
	        	}
         	}
         	
         	path.add(location.get(currentIndex));
        	visited.add(currentIndex);
        	currentIndex = nextIndex;
              
     	}
     	if(correctPath) {
        	path.add(location.get(sinkIndex));
        	return path;
        }
        else return new ArrayList<Vertex>(0);
     	
    }
    
    /* This method returns a path (shortest in terms of latency) from a source at
    location sourceIndex and a sink at location sinkIndex using Dijkstra's algorithm.
    An empty path is returned if Dijkstra's algorithm fails to find a path. */
    /* The following code is meant to be a placeholder that simply 
    returns an empty path. Replace it with your own code that 
    implements Dijkstra's algorithm. */    
    public ArrayList<Vertex> dijkstraPathLatency(int sourceIndex, int sinkIndex) {
   	
    	ArrayList<Vertex> path = new ArrayList<Vertex>();
    	ArrayList<Graph> unvisited = new ArrayList<Graph>(); 
    	
    	for(int i = 0; i < location.size(); i++){
   			Graph g = new Graph(location.get(i), i);
    		unvisited.add(g);
    	}
    	
    	for(Edge e : edges){
    		if(location.get(e.getU()).distance(location.get(e.getV())) <= transmissionRange){
    			unvisited.get(e.getU()).adjacents.add(new Edge(e.getU(), e.getV(), e.getW()));
    			unvisited.get(e.getV()).adjacents.add(new Edge(e.getV(), e.getU(), e.getW()));
    		}
    	}
    	
    	PriorityQueue<Graph> graphQueue = new PriorityQueue<Graph>();
    	unvisited.get(sourceIndex).distance = 0;
    	
    	graphQueue.add(unvisited.get(sourceIndex));
    	
    	while(!graphQueue.isEmpty()){
    		if(graphQueue.peek().distance == Double.POSITIVE_INFINITY){
    			break;
    		}
    		
    		Graph g = graphQueue.poll();
    		
    		if(g.index == sinkIndex){
    			break;
    		}
    		
    		for(Edge e : g.adjacents){
    			int nextIndex = e.getV();
    			double weight = e.getW();
    			Graph next = unvisited.get(nextIndex);
    			double tempDistance = g.distance + weight;
    			if(tempDistance < next.distance){
    				graphQueue.remove(next);
    				next.distance = tempDistance;
    				next.previousVertex = g;
    				graphQueue.add(next);
    			}
    		}
    	}
    	
    	Graph last = unvisited.get(sinkIndex);
    	if(last.distance < Double.POSITIVE_INFINITY){
    		path.add(last);
    		while(last.previousVertex!=null){
    			last = last.previousVertex;
    			path.add(last);
    		}
    		Collections.reverse(path);
    	}
    	return path;
    }
    
    /* This method returns a path (shortest in terms of hops) from a source at
    location sourceIndex and a sink at location sinkIndex using Dijkstra's algorithm.
    An empty path is returned if Dijkstra's algorithm fails to find a path. */
    /* The following code is meant to be a placeholder that simply 
    returns an empty path. Replace it with your own code that 
    implements Dijkstra's algorithm. */
    public ArrayList<Vertex> dijkstraPathHops(int sourceIndex, int sinkIndex) {
    	ArrayList<Vertex> path = new ArrayList<Vertex>();
    	ArrayList<Graph> unvisited = new ArrayList<Graph>(); 
    	
    	for(int i = 0; i < location.size(); i++){
   			Graph g = new Graph(location.get(i), i);
    		unvisited.add(g);
    	}
    	
    	for(Edge e : edges){
    		if(location.get(e.getU()).distance(location.get(e.getV())) <= transmissionRange){
    			unvisited.get(e.getU()).adjacents.add(new Edge(e.getU(), e.getV(), 1));
    			unvisited.get(e.getV()).adjacents.add(new Edge(e.getV(), e.getU(), 1));
    		}
    	}
    	
    	PriorityQueue<Graph> graphQueue = new PriorityQueue<Graph>();
    	unvisited.get(sourceIndex).distance = 0;
    	
    	graphQueue.add(unvisited.get(sourceIndex));
    	
    	while(!graphQueue.isEmpty()){
    		if(graphQueue.peek().distance == Double.POSITIVE_INFINITY){
    			break;
    		}
    		
    		Graph g = graphQueue.poll();
    		
    		if(g.index == sinkIndex){
    			break;
    		}
    		
    		for(Edge e : g.adjacents){
    			int nextIndex = e.getV();
    			double weight = e.getW();
    			Graph next = unvisited.get(nextIndex);
    			double tempDistance = g.distance + weight;
    			if(tempDistance < next.distance){
    				graphQueue.remove(next);
    				next.distance = tempDistance;
    				next.previousVertex = g;
    				graphQueue.add(next);
    			}
    		}
    	}
    	
    	Graph last = unvisited.get(sinkIndex);
    	if(last.distance < Double.POSITIVE_INFINITY){
    		path.add(last);
    		while(last.previousVertex!=null){
    			last = last.previousVertex;
    			path.add(last);
    		}
    		Collections.reverse(path);
    	}
    	return path;
    }
    
}

