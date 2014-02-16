/**
 * Nikhil Joglekar
 * nrj328
 */

import java.util.ArrayList;

public class Graph extends Vertex implements Comparable<Graph> {
	double distance;
	Graph previousVertex;
	int index;
	boolean removed;
	ArrayList<Edge> adjacents = new ArrayList<Edge>();
	
	Graph(){
		super();
		distance = Double.POSITIVE_INFINITY;
		removed = false;
	}
	
	Graph(Vertex v, int i){
		super(v.getX(), v.getY());
		distance = Double.POSITIVE_INFINITY;
		removed = false;
	}

	public int compareTo(Graph g){
		return Double.compare(distance, g.distance);
	}
	
}