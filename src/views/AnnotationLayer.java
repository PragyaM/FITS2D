package views;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import models.Annotation;

public class AnnotationLayer extends Canvas{
	private ArrayList<Annotation> annotations;
	private GraphicsContext gc;
	
	public AnnotationLayer(double width, double height){
		super(width, height);
		annotations = new ArrayList<Annotation>();
		gc = this.getGraphicsContext2D();
		gc.setLineWidth(2);
		
		makeNewAnnotation();
	}
	
	public void makeNewAnnotation(){
		Annotation a = new Annotation(gc);
		this.addEventHandler(MouseEvent.ANY, a);
		annotations.add(a);
	}
	
	public void drawAll(){
		for (Annotation annotation : annotations) {
			annotation.draw();
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
