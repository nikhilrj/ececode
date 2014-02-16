import java.awt.*;
public class graphicHandler {
	/**
     * Problem 6: Draw a Sierpinski Carpet.
     * @param size the size in pixels of the window
     * @param limit the smallest size of a sqauer in the carpet.
     */
    public void drawCarpet(int size, int limit) {
        DrawingPanel p = new DrawingPanel(size, size);
        Graphics g = p.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,size,size);
        g.setColor(Color.WHITE);
        drawSquares(g, size, limit, 0, 0);
    }


    /* Helper method for drawCarpet
     * Draw the individual squares of the carpet.
     * @param g The Graphics object to use to fill rectangles
     * @param size the size of the current square
     * @param limit the smallest allowable size of squares
     * @param x the x coordinate of the upper left corner of the current square
     * @param y the y coordinate of the upper left corner of the current square
     */
    private static void drawSquares(Graphics g, int size, int limit, double x, double y) {
    	if(size <= limit) {
    		//g.setColor(Color.WHITE);
    		return;
    	}
    	else {
    		g.fillRect((int) (x + (size/3)), (int) (y + (size/3)), size/3, size/3 );
    		drawSquares(g, size/3, limit, x, y);
    		drawSquares(g, size/3, limit, x + size/3, y);
    		drawSquares(g, size/3, limit, x, y+size/3);
    		drawSquares(g, size/3, limit, x + 2*size/3, y);
    		drawSquares(g, size/3, limit, x + 2*size/3, y+size/3);
    		drawSquares(g, size/3, limit, x + 2*size/3, y+2*size/3);
    		drawSquares(g, size/3, limit, x + size/3, y+size/3);
    		drawSquares(g, size/3, limit, x, y+2*size/3);
    		drawSquares(g, size/3, limit, x + size/3, y+2*size/3);
    		//drawSquares(g, size/3, limit*3, x, y);
  	
    	}
    }
}
