package models;

import java.awt.Point;
import java.util.ArrayList;

/**
 * A Region is a collection of Points that represent coloured pixels in an annotation
 * 
 * @author Pragya
 */
public class Region {
	
	private ArrayList<Point> pixels;
	
	public Region(ArrayList<Point> pixels){
		this.pixels = pixels;
	}
	
	public Region(){
		this.pixels = new ArrayList<Point>();
	}
	
	
	public void addAll(ArrayList<Point> points){
		this.pixels.addAll(points);
	}
	
	public void add(Point p){
		this.pixels.add(p);
	}
	
	public ArrayList<Point> getPixels(){
		return pixels;
	}
	
	public String toString(){
		String regionString = "r ";
		for (Point p : pixels){
			regionString = regionString + String.format("%d,%d ", p.x, p.y);
		}
		return regionString;
	}

}
