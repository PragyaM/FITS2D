package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import views.AnnotationLayer;


public class Annotation implements EventHandler<MouseEvent>{
	
	private ArrayList<Point> coordinates = new ArrayList<Point>();
	private AnnotationLayer container;
	
	public Annotation(AnnotationLayer container){
		this.container = container;
	}

	public void draw(GraphicsContext gc) {
		//FIXME: Use Lines instead
		for (Point p : coordinates){
			
		}
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
			Point p = new Point((int) event.getX(), (int) event.getY());
			coordinates.add(p); //FIXME: write to Lines instead
			container.getGraphicsContext2D().lineTo(p.x, p.y);
			container.getGraphicsContext2D().stroke();
			event.consume();
		}
		
		if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
			container.getGraphicsContext2D().beginPath();
			event.consume();
		}
		
		if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
			container.getGraphicsContext2D().closePath();
			event.consume();
		}
	}

}
