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
import views.AnnotationLayer;
import views.AnnotationLayer.Mode;


public class Annotation implements EventHandler<MouseEvent>{

	private ArrayList<Region> regions = new ArrayList<Region>();
	private GraphicsContext gc;
	//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	private Color color;
	private Region region;
	private Point currentPoint;
	private PixelWriter pw;
	private AnnotationLayer canvas;

	public Annotation(AnnotationLayer canvas, Color color){
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.color = color;
		this.pw = gc.getPixelWriter();
	}

	public void draw() {
		for (Region region : regions){
			for (Point pixel : region.getPixels()){
				gc.getPixelWriter().setColor(pixel.x, pixel.y, color);
				gc.fillRect(pixel.x, pixel.y, 0, 0);
			}
		}
	}

	public void setRegions(ArrayList<Region> regions){
		this.regions = regions;
	}
	
	public ArrayList<Region> getRegions(){
		return regions;
	}

	public void addRegion(Region region){
		this.regions.add(region);
	}

	@Override
	public void handle(MouseEvent event) {

		if (canvas.mode==Mode.ANNOTATE_DRAW || canvas.mode==Mode.MASK_DRAW){
			if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
				Point p = new Point((int) event.getX(), (int) event.getY());

				ArrayList<Point> freshPixels = (DrawLine.draw(currentPoint, p));
				region.addAll(freshPixels);
				for (Point pixel : freshPixels){
					pw.setColor(pixel.x, pixel.y, color);
				}
				currentPoint = p;
				

				event.consume();
			}

			else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
				region = new Region();
				Point p = new Point((int) event.getX(), (int) event.getY());
				currentPoint = p;

				event.consume();
			}

			else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
				regions.add(region);

				event.consume();
			}
		}
		else if (canvas.mode==Mode.ANNOTATE_FILL || canvas.mode==Mode.MASK_FILL){
			if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
				region = new Region();
				Point p = new Point((int) event.getX(), (int) event.getY());
				
				ArrayList<Point> freshPixels = (FillRegion.fill(canvas, p));
				region.addAll(freshPixels);
				for (Point pixel : freshPixels){
					pw.setColor(pixel.x, pixel.y, color);
				}
				
				regions.add(region);
				event.consume();
			}
		}		
	}

	public String toString(){
		String annotationString = ""; //"Colour: " + color.toString();
		for (Region region : regions){
			annotationString = annotationString + "\n" +  region.toString();
		}
		return annotationString;
	}

}
