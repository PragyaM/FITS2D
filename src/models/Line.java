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
	private SVGPath path;
	
	public Line(ArrayList<Point> coords){
		this.coordinates = coords;
	}
	
	public Line(String svgPathString){
		this.path = new SVGPath();
		this.path.setContent(svgPathString);
	}
	
	public Line(){
		this.coordinates = new ArrayList<Point>();
	}
	
	public void appendPoint(Point p){
		this.coordinates.add(p);
	}
	
	public void setupWCS(){
		//TODO: convert coordinates into WCS
	}
	
	public SVGPath getSVGPath(){
//		if (coordinates.size() == 0){
//			return path;
//		}
//		else {
			return toSVGPath();
//		}
	}
	
	public int numPoints(){
		return coordinates.size();
	}
	
	public SVGPath toSVGPath(){
		String pathString = "";
		try {
			pathString = pathString + "M" + coordinates.get(0).x + "," + coordinates.get(0).y + " C";
			
			for (int i = 1; i < coordinates.size(); i++){
				pathString = pathString + "" + coordinates.get(i).x + "," + coordinates.get(i).y + " ";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		path = new SVGPath();
		path.setContent(pathString);
		return path;
	}

}
