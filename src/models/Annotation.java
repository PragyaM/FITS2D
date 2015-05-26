package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class Annotation implements EventHandler<MouseEvent>{
	
	private ArrayList<Line> lines = new ArrayList<Line>();
	private GraphicsContext gc;
//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	private Color color;
	
	public Annotation(GraphicsContext gc){
		this.gc = gc;
		this.color = Color.WHITE;
	}

	public void draw() {
		for (Line l : lines){
			gc.appendSVGPath(l.toSVGPath().getContent());
			gc.stroke();
		}
	}

	@Override
	public void handle(MouseEvent event) {
		Line line = new Line();
		
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
			Point p = new Point((int) event.getX(), (int) event.getY());
			line.appendPoint(p);
			gc.lineTo(p.x, p.y);
			gc.stroke();
			event.consume();
		}
		
		if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
			gc.setStroke(color);
			gc.beginPath();
			line = new Line();
			event.consume();
		}
		
		if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
			gc.closePath();
			lines.add(line);
			event.consume();
		}
	}

}
