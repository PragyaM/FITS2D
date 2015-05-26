package views;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import models.Annotation;

public class AnnotationLayer extends Canvas{
	private ArrayList<Annotation> annotations;
	private GraphicsContext gc;
	
	public AnnotationLayer(double width, double height){
		super(width, height);
		annotations = new ArrayList<Annotation>();
		gc = this.getGraphicsContext2D();
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2);
		
		makeNewAnnotation();
	}
	
	public void makeNewAnnotation(){
		Annotation a = new Annotation(this);
		this.addEventHandler(MouseEvent.ANY, a);
		annotations.add(a);
	}
	
	public void drawPoint(Point p, Color c){
		gc.getPixelWriter().setColor(p.x, p.y, c);
	}
	
	public void drawAll(){
		for (Annotation annotation : annotations) {
			annotation.draw(gc);
		}
	}
	
	public void selectionFromAnnotations(ArrayList<Annotation> selectedAnnotations){
		
	}
	
	public void cutSelection(){
		
	}
	
	public void undo(){
		
	}
	
	public void redo(){
		
	}
	
	public void showAnnotations(ArrayList<Annotation> annotations){
		
	}
	
	public void hideAnnotations(ArrayList<Annotation> annotations){
		
	}
  
}
