package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import services.DrawLine;
import services.FillRegion;
import views.FitsCanvas;
import views.FitsImageViewBox;
import controllers.DrawingsController;

/**
 * Drawings are created by the user drawing on the canvas interface while in annotation or selection mode.
 * These drawings, while drawn in canvas pixels, must store coordinate values representing FITS
 * image pixels when written to a file. This allows drawings to work across various images, 
 * rather than being locked down to a rendered state that is specific to one image.
 * @author Pragya
 *
 */
public abstract class Drawing implements EventHandler<MouseEvent>{

	protected ArrayList<PixelRegion> regions = new ArrayList<PixelRegion>();
	protected GraphicsContext gc;
	//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	protected Color color;
	protected PixelRegion region;
	protected Point currentPoint;
	protected PixelWriter pw;
	protected FitsCanvas canvas;
	protected FitsImage image;
	protected FitsImageViewBox imageViewBox;
	protected DrawingsController controller;

	public Drawing(FitsCanvas canvas, DrawingsController controller, Color color){
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.pw = gc.getPixelWriter();
		this.color = color;
		this.controller = controller;
		this.controller.setCurrentDrawing(this);
		this.imageViewBox = controller.getImageViewBox();
		this.image = imageViewBox.getFitsImage();
	}

	public void draw() {
		for (PixelRegion region : regions){
			for (Point pixel : region.getCanvasPixels()){
				try {
					pw.setColor(pixel.x, pixel.y, color);
				} catch(IndexOutOfBoundsException e){
					System.out.println("ignore pixels that are out of image bounds");
				}
			}
		}
	}

	public void setRegions(ArrayList<PixelRegion> regions){
		this.regions = regions;
	}
	
	public ArrayList<PixelRegion> getRegions(){
		return regions;
	}

	public void addRegion(PixelRegion region){
		this.regions.add(region);
	}
	
	public void undo(){
		if (regions.size() == 0){
			/* there are no regions left in this currentDrawing to be undone */
		}
		else {
			regions.remove(regions.size() - 1);
		}
	}
	
	public void handle(MouseEvent event) {
		/*let subclasses define this*/
	}
	
	public void drawAction(MouseEvent event){
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
			Point p = new Point((int) event.getX(), (int) event.getY());

			ArrayList<Point> canvasPixels = (DrawLine.draw(currentPoint, p));
			region.addAllCanvasPixels(canvasPixels);
			
			for (Point pixel : canvasPixels){
				pw.setColor(pixel.x, pixel.y, color);
			}
			currentPoint = p;

			event.consume();
		}

		else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
			region = new PixelRegion();
			Point p = new Point((int) event.getX(), (int) event.getY());
			currentPoint = p;

			event.consume();
		}

		else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
			region.cropToBounds(canvas.getWidth(), canvas.getHeight());
			region.generateImagePixels(canvas.getHeight(), image.getHeight());
			regions.add(region);

			event.consume();
		}
	}
	
	public void floodFillAction(MouseEvent event){
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
			region = new PixelRegion();
			Point p = new Point((int) event.getX(), (int) event.getY());
			
			ArrayList<Point> canvasPixels = (FillRegion.fill(canvas, controller.getFitsCanvasController(), p, color));
			region.addAllCanvasPixels(canvasPixels);
			
			for (Point pixel : canvasPixels){
				pw.setColor(pixel.x, pixel.y, color);
			}

			region.cropToBounds(canvas.getWidth(), canvas.getHeight());
			region.generateImagePixels(canvas.getHeight(), image.getHeight());
			regions.add(region);
			event.consume();
		}
	}
	
	public String toString(){
		/*let subclasses define this*/
		return "toString must be defined by subclass";
	}

}
