public class Vertex {
    
    private double x; // x-coordinate of the vertex.
    private double y; // y-coordinate of the vertex.
    
    Vertex() {
        /* This constructor sets both coordinates to 0.0. */
        this(0.0, 0.0);
    }
    
    Vertex(double x, double y) {
        /* This constructor sets the x-coordinate to x 
           and the y-coordinate to y. */
        this.x = x;
        this.y = y;
    }
    
    public void setX(double x) {
        /* This method sets the x-coordinate to x. */
        this.x = x;
    }
    
    public void setY(double y) {
        /* This method sets the y-coordinate to y. */
        this.y = y;
    }
    
    public void setXY(double x, double y) {
        /* This method sets the x-coordinate to x 
           and the y-coordinate to y. */
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        /* This method returns the x-coordinate. */
        return x;
    }
    
    public double getY() {
        /* This method returns the y-coordinate. */
        return y;
    }
    
    public double distance(Vertex v) {
        /* This method returns the Euclidean distance 
           between the vertex and another vertex v. */
        return Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2));
    }
    
    public String toString() {
        /* This method returns a string representation 
           of the vertex. */
        return "(" + x + "," + y + ")";
    }
    
}

