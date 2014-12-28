import java.util.ArrayList;
import java.util.LinkedList;


public class Inits {

	ArrayList<LinkedList<Integer>> adjacencyMatrix;
	Graph[] unvisited;
	
	Inits(){
		
	}
    
    void init(ArrayList<Vertex> location, ArrayList<Edge> edges, double transmissionRange){
    	System.gc();
    	unvisited = new Graph[location.size()];
    	for(int i = 0; i < location.size(); i++){
   			Graph g = new Graph(location.get(i), i);
    		unvisited[i] = g;
    	}
    	
    	for(Edge e : edges){
    		if(location.get(e.getU()).distance(location.get(e.getV())) <= transmissionRange){
    			unvisited[e.getU()].adjacents.add(new Edge(e.getU(), e.getV(), e.getW()));
    			unvisited[e.getU()].adjacents.trimToSize();
    			unvisited[e.getV()].adjacents.add(new Edge(e.getV(), e.getU(), e.getW()));
    			unvisited[e.getV()].adjacents.trimToSize();
    		}
    	}
    }
    
    void initMatrix(ArrayList<Vertex> location, double transmissionRange){
    	System.gc();
    	adjacencyMatrix = new ArrayList<LinkedList<Integer>>(0);

       	for(int i = 0; i < location.size(); i++){
    		adjacencyMatrix.add(new LinkedList<Integer>());
    		//adjacencyMatrix.get(i).trimToSize();
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
    	adjacencyMatrix.trimToSize();
    }
}
