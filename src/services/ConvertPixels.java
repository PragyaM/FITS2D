package services;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class bridges the gap between having a visual representation of a FITS image that
 * can be visually manipulated, and having FITS data used and changed accurately in the background.
 * 
 * This class provides the ability to convert FITS image pixels to canvas pixels and vice-versa,
 * where canvas pixels represent the rendered image, and FITS image pixels represent the data pixels
 * that are contained in a FITS image.
 * 
 * We need to be careful about preserving the image to canvas ratio of pixels when doing these conversions,
 * while also remembering that the FITS data representation observes an image with the (0, 0) pixel coordinate
 * in the bottom left corner, whereas the rendered representation observes images with the (0, 0) pixel
 * coordinate in the top left corner.
 * 
 * @author Pragya
 *
 */

public class ConvertPixels {
	
	private double imageToCanvasRatio;
	private int canvasHeight;
	private int imageHeight;

	public ConvertPixels(double canvasHeight, int imageHeight){
		this.canvasHeight = (int) canvasHeight;
		this.imageHeight = imageHeight;
		this.imageToCanvasRatio = imageHeight / canvasHeight;
	}
	
	public ArrayList<Point> imageToCanvas(ArrayList<Point> imagePixels){
		HashSet<Point> canvasPixels = new HashSet<Point>();
		for (Point pixel : imagePixels){
			int x = (int) (Math.floor(pixel.x / imageToCanvasRatio));
			int y = (int) (Math.ceil(pixel.y / imageToCanvasRatio));
			canvasPixels.add(new Point(x, (canvasHeight - y)));
		}
		
		ArrayList<Point> outputCanvasPixels = new ArrayList<Point>(canvasPixels);
		
		return outputCanvasPixels;
	}
	
	public ArrayList<Point> canvasToImage(ArrayList<Point> canvasPixels){
		HashSet<Point> imagePixels = new HashSet<Point>();
		for (Point pixel : canvasPixels){
			int x0 = (int) (Math.floor(pixel.x * imageToCanvasRatio));
			if (x0 < 0) x0 = 0;
			int y0 = (int) (Math.floor(pixel.y * imageToCanvasRatio));
			int x1 = x0 + (int) imageToCanvasRatio;
			int y1 = y0 + (int) imageToCanvasRatio;
			
			for (int x = x0; x < x1; x++){
				for (int y = y0; y < y1; y++){
					imagePixels.add(new Point(x, imageHeight - y));
				}
			}
		}
		
		ArrayList<Point> outputImagePixels = new ArrayList<Point>(imagePixels);
		
		return outputImagePixels;
	}
}
