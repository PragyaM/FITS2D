package models;

import java.awt.Point;
import java.util.ArrayList;

import services.ConvertPixels;

/**
 * A PixelRegion is a collection of Points that represent coloured pixels in an annotation
 * 
 * @author Pragya
 */
public class PixelRegion {

	private ArrayList<Point> canvasPixels;
	private ArrayList<Point> imagePixels;

	public PixelRegion(ArrayList<Point> canvasPixels){
		this.canvasPixels = canvasPixels;
		this.imagePixels = new ArrayList<Point>();
	}

	public PixelRegion(){
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
		StringBuilder regionString = new StringBuilder("r ");

		for (Point p : imagePixels){
			regionString.append(p.x);
			regionString.append(',');
			regionString.append(p.y);
			regionString.append(' ');
		}
		return regionString.toString();
	}

	public void cropToBounds(double width, double height) {
		ArrayList<Point> pixelsToDelete = new ArrayList<Point>();

		for (int i = 0; i < canvasPixels.size(); i++){
			Point p = canvasPixels.get(i);
			if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
				pixelsToDelete.add(p);
			}
		}

		canvasPixels.removeAll(pixelsToDelete);
	}

	public boolean isEmpty() {
		if (canvasPixels.size() == 0 && imagePixels.size() == 0){
			return true;
		}
		else return false;
	}

}
