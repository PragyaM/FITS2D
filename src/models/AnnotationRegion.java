package models;

import java.awt.Point;
import java.util.ArrayList;

import services.ConvertPixels;

/**
 * A AnnotationRegion is a collection of Points that represent coloured pixels in an annotation
 * 
 * @author Pragya
 */
public class AnnotationRegion {
	
	private ArrayList<Point> canvasPixels;
	private ArrayList<Point> imagePixels;
	
	public AnnotationRegion(ArrayList<Point> canvasPixels){
		this.canvasPixels = canvasPixels;
		this.imagePixels = new ArrayList<Point>();
	}
	
	public AnnotationRegion(){
		this.canvasPixels = new ArrayList<Point>();
		this.imagePixels = new ArrayList<Point>();
	}
	
	public void addAllCanvasPixels(ArrayList<Point> points){
		this.canvasPixels.addAll(points);
	}
	
	public void addAllImagePixels(ArrayList<Point> points){
		this.imagePixels.addAll(points);
	}
	
	public void addCanvasPixel(Point p){
		this.canvasPixels.add(p);
	}
	
	public void addImagePixel(Point p){
		this.imagePixels.add(p);
	}
	
	public ArrayList<Point> getCanvasPixels(){
		return canvasPixels;
	}
	
	public ArrayList<Point> getImagePixels(){
		return imagePixels;
	}
	
	public void clearAllCanvasPixels(){
		this.canvasPixels = new ArrayList<Point>();
	}
	
	public void clearAllImagePixels(){
		this.imagePixels = new ArrayList<Point>();
	}
	
	public ArrayList<Point> generateImagePixels(double canvasHeight, int imageHeight){
		imagePixels = (new ConvertPixels(canvasHeight, 
				imageHeight)).canvasToImage(canvasPixels);
		
		return imagePixels;
	}
	
	public ArrayList<Point> generateCanvasPixels(double canvasHeight, int imageHeight){
		canvasPixels = (new ConvertPixels(canvasHeight, 
				imageHeight)).imageToCanvas(imagePixels);
		
		return canvasPixels;
	}
	
	public String toString(){
		String regionString = "r ";
		
		for (Point p : imagePixels){
			regionString = regionString + String.format("%d,%d ", p.x, p.y);
		}
		return regionString;
	}

}
