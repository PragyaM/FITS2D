package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import views.AnnotationLayer;


public class Annotation implements EventHandler<MouseEvent>{
	
	private ArrayList<Point> coordinates = new ArrayList<Point>();
	private AnnotationLayer container;
	
	public Annotation(AnnotationLayer container){
		this.container = container;
	}

	public void draw(GraphicsContext gc) {
		for (Point p : coordinates){
			
		}
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
			Point p = new Point((int) event.getX(), (int) event.getY());
			coordinates.add(p);
			container.drawPoint(p, Color.WHEAT);
			event.consume();
		}
	}

}
