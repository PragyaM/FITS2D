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

	private ArrayList<Line> lines = new ArrayList<Line>();
	private GraphicsContext gc;
	//	private String associatedFileName; //TODO: keep track of which image this annotation was created on
	private Color color;
	private Line line;
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

		if (canvas.mode==Mode.ANNOTATE_DRAW || canvas.mode==Mode.MASK_DRAW){
			if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
				Point p = new Point((int) event.getX(), (int) event.getY());

				ArrayList<Point> freshPixels = (DrawLine.draw(currentPoint, p));
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
		else if (canvas.mode==Mode.ANNOTATE_FILL || canvas.mode==Mode.MASK_FILL){
			if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
				line = new Line();
				Point p = new Point((int) event.getX(), (int) event.getY());
				
				ArrayList<Point> freshPixels = (FillRegion.fill(canvas, p));
				line.appendPoints(freshPixels);
				for (Point pixel : freshPixels){
					pw.setColor(pixel.x, pixel.y, color);
				}
				
				lines.add(line);
				event.consume();
			}
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
