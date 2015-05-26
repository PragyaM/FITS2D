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
	
	private ArrayList<Point> coordinates;
	
	public Line(ArrayList<Point> coords){
		this.coordinates = coords;
	}
	
	public Line(){
		this.coordinates = new ArrayList<Point>();
	}
	
	public void appendPoint(Point p){
		coordinates.add(p);
	}
	
	public void setupWCS(){
		//TODO: convert coordinates into WCS
	}
	
	public SVGPath toSVGPath(){
		String pathString = "M";
		pathString += "" + coordinates.get(0).x + "," + coordinates.get(0).y + " C";
		
		for (int i = 1; i < coordinates.size(); i++){
			pathString += "" + coordinates.get(i).x + "," + coordinates.get(i).y + " ";
		}
		
		
		SVGPath path = new SVGPath();
		path.setContent(pathString);
		return path;
	}

}
