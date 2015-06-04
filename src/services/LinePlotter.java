package services;

import java.awt.Point;
import java.util.ArrayList;
/**
 * Use Bresenham's line drawing algorithm to return a list of all the points that are part of a line given start and end points.
 * This helped: http://stackoverflow.com/a/11683720/4972117
 * 
 * @author Pragya
 *
 */
public class LinePlotter {

	public static ArrayList<Point> makeLine(Point orig, Point dest){
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		int x0 = orig.x;
		int x1 = dest.x;
		int y0 = orig.y;
		int y1 = dest.y;
		
		int w = x1 - x0;
	    int h = y1 - y0;
	    
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
	    
	    //taking into account 6 of 8 octants:
	    if (w<0) dx1 = -1; else if (w>0) dx1 = 1;
	    if (h<0) dy1 = -1; else if (h>0) dy1 = 1;
	    if (w<0) dx2 = -1; else if (w>0) dx2 = 1;
	    
	    int longest = Math.abs(w);
	    int shortest = Math.abs(h);
	    
	    if (!(longest > shortest)) {
	        longest = Math.abs(h);
	        shortest = Math.abs(w);
	        
	        //now the other 2 octants:
	        if (h<0) dy2 = -1; else if (h>0) dy2 = 1;
	        dx2 = 0;            
	    }
	    
	    int numerator = longest >> 1;
	    
	    for (int i=0; i<=longest; i++) {
	    	
	    	points.add(new Point(x0, y0)); //add point that is covered by line
	    	
	        numerator += shortest;
	        
	        if (!(numerator<longest)) {
	            numerator -= longest;
	            x0 += dx1;
	            y0 += dy1;
	        }
	        else {
	            x0 += dx2;
	            y0 += dy2;
	        }
	    }
	    
		return points;
	}
}
