package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import services.ConvertPixels;
import services.DrawLine;
import services.FillRegion;
import views.AnnotationLayer;
import views.AnnotationLayer.Mode;

/**
 * Annotations are created by the user drawing on the canvas interface while in annotation mode.
 * These annotations, while drawn in canvas pixels, must store coordinate values representing FITS
 * image pixels when written to a file. This allows annotations to work across various images, 
 * rather than being locked down to a rendered state that is specific to one image.
 * @author Pragya
 *
 */
public class Annotation implements EventHandler<MouseEvent>{

	private ArrayList<AnnotationRegion> regions = new ArrayList<AnnotationRegion>();
	private GraphicsContext gc;
	//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	private Color color;
	private AnnotationRegion region;
	private Point currentPoint;
	private PixelWriter pw;
	private AnnotationLayer canvas;
	private FitsImage image;

	public Annotation(AnnotationLayer canvas, FitsImage image, Color color){
		this.canvas = canvas;
		this.image = image;
		this.gc = canvas.getGraphicsContext2D();
		this.color = color;
		this.pw = gc.getPixelWriter();
	}

	public void draw() {
		for (AnnotationRegion region : regions){
			for (Point pixel : region.getCanvasPixels()){
				try {
					pw.setColor(pixel.x, pixel.y, color);
				} catch(IndexOutOfBoundsException e){
					System.out.println("ignore pixels that are out of image bounds");
				}
			}
		}
	}

	public void setRegions(ArrayList<AnnotationRegion> regions){
		this.regions = regions;
	}
	
	public ArrayList<AnnotationRegion> getRegions(){
		return regions;
	}

	public void addRegion(AnnotationRegion region){
		this.regions.add(region);
	}

	@Override
	public void handle(MouseEvent event) {

		if (canvas.mode==Mode.ANNOTATE_DRAW || canvas.mode==Mode.MASK_DRAW){
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
				region = new AnnotationRegion();
				Point p = new Point((int) event.getX(), (int) event.getY());
				currentPoint = p;

				event.consume();
			}

			else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
				region.generateImagePixels(canvas.getHeight(), image.getHeight());
				regions.add(region);

				event.consume();
			}
		}
		else if (canvas.mode==Mode.ANNOTATE_FILL || canvas.mode==Mode.MASK_FILL){
			if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
				region = new AnnotationRegion();
				Point p = new Point((int) event.getX(), (int) event.getY());
				
				ArrayList<Point> canvasPixels = (FillRegion.fill(canvas, p, color));
				region.addAllCanvasPixels(canvasPixels);
				
				for (Point pixel : canvasPixels){
					pw.setColor(pixel.x, pixel.y, color);
				}

				region.generateImagePixels(canvas.getHeight(), image.getHeight());
				regions.add(region);
				event.consume();
			}
		}		
	}

	public String toString(){
		String annotationString = ""; //"Colour: " + color.toString();
		for (AnnotationRegion region : regions){
			annotationString = annotationString + "\n" +  region.toString();
		}
		return annotationString;
	}

}
