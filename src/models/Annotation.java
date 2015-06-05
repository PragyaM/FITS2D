package models;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import services.LinePlotter;


public class Annotation implements EventHandler<MouseEvent>{
	
	private ArrayList<Line> lines = new ArrayList<Line>();
	private GraphicsContext gc;
//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	private Color color;
	private Line line;
	private Point currentPoint;
	private PixelWriter pw;
	
	public Annotation(GraphicsContext gc){
		this.gc = gc;
		this.color = Color.WHITE;
		this.pw = gc.getPixelWriter();
	}

	public void draw() {
		for (Line line : lines){
			for (Point pixel : line.getPixels()){
				gc.getPixelWriter().setColor(pixel.x, pixel.y, color);
				gc.fillRect(pixel.x, pixel.y, 0, 0);
			}
		}
	}
	
	public void setLines(ArrayList<Line> lines){
		this.lines = lines;
	}
	
	public void addLine(Line line){
		this.lines.add(line);
	}

	@Override
	public void handle(MouseEvent event) {
		
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
			Point p = new Point((int) event.getX(), (int) event.getY());
			
			ArrayList<Point> freshPixels = (LinePlotter.makeLine(currentPoint, p));
			line.appendPoints(freshPixels);
			for (Point pixel : freshPixels){
				pw.setColor(pixel.x, pixel.y, color);
			}
			currentPoint = p;
			
			event.consume();
		}
		
		else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
			line = new Line();
			Point p = new Point((int) event.getX(), (int) event.getY());
			currentPoint = p;
			
			event.consume();
		}
		
		else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
			lines.add(line);
			
			event.consume();
		}
	}
	
	public String toString(){
		String annotationString = ""; //"Colour: " + color.toString();
		for (Line line : lines){
			annotationString = annotationString + "\n" +  line.toString();
		}
		return annotationString;
	}

}
