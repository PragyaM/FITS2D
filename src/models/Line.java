package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.shape.SVGPath;

/**
 * A Line is a collection of Points that can be represented as an SVGPath or as a series of WCS coordinates
 * 
 * @author Pragya
 */
public class Line {
	
	private ArrayList<Point> pixels;
	
	public Line(ArrayList<Point> pixels){
		this.pixels = pixels;
	}
	
	public Line(){
		this.pixels = new ArrayList<Point>();
	}
	
	
	public void appendPoints(ArrayList<Point> points){
		this.pixels.addAll(points);
	}
	
	public void appendPoint(Point p){
		this.pixels.add(p);
	}
	
	public ArrayList<Point> getPixels(){
		return pixels;
	}
	
	public String toString(){
		String lineString = "l ";
		for (Point p : pixels){
			lineString = lineString + String.format("%d,%d ", p.x, p.y);
		}
		return lineString;
	}

}
