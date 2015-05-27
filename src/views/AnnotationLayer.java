package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public void turnAnnotatingOff(){
		this.removeEventHandler(MouseEvent.ANY, annotations.get(annotations.size()-1));
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
	
//	public void selectionFromAnnotations(ArrayList<Annotation> selectedAnnotations){
//		
//	}
//	
//	public void cutSelection(){
//		
//	}
//	
//	public void undo(){
//		
//	}
//	
//	public void redo(){
//		
//	}
	
//	public void showAnnotations(ArrayList<Annotation> annotations){
//		
//	}
	
	public void hideAnnotations(){
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void writeAnnotationsToFile(String fname){
		File aFile = new File(fname);
		BufferedWriter writer = null;
		String annotationsString = "FitsImageViewerAnnotations";
		
		for (Annotation a : annotations){
			annotationsString = annotationsString + "\n*\n" + a.toString();
		}
		
		try {
			writer = new BufferedWriter(new FileWriter(aFile));
			writer.write(annotationsString);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addAnnotationsFromFile(File aFile){
		//TODO read saved files
		
		//Check for "FitsImageViewAnnotations" at beginning of file to validate format
		
		//Delimit annotations with "*"
		
		//Within annotations, delimit SVGPaths by newline
		
		//Annotations can now be rendered
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(aFile));

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  
}
