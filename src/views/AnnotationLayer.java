package views;

import java.awt.Point;
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
import models.Line;

public class AnnotationLayer extends Canvas{
	private ArrayList<Annotation> annotations;
	private GraphicsContext gc;
	private Annotation currentAnnotation;
	public enum Mode {NONE, DRAW, FILL};
	public Mode mode;

	public AnnotationLayer(double width, double height){
		super(width, height);
		annotations = new ArrayList<Annotation>();
		gc = this.getGraphicsContext2D();
		gc.setLineWidth(2);
		mode = Mode.NONE;
		makeNewAnnotation();
	}
	
	public void turnDrawModeOn(){
		//TODO: turn cursor into pencil
		if (currentAnnotation == null){
			makeNewAnnotation();
		} else this.addEventHandler(MouseEvent.ANY, currentAnnotation);
		mode = Mode.DRAW;
	}
	
	public void turnDrawModeOff(){
		turnAnnotatingOff();
		mode = Mode.NONE;
	}
	
	public void turnFillModeOn(){
		//TODO: turn cursor into bucket
		if (currentAnnotation == null){
			makeNewAnnotation();
		} else this.addEventHandler(MouseEvent.ANY, currentAnnotation);
		mode = Mode.FILL;
	}
	
	public void turnFillModeOff(){
		turnAnnotatingOff();
		mode = Mode.NONE;
	}
	
	

	public void turnAnnotatingOff(){
		for (Annotation a : annotations){
			this.removeEventHandler(MouseEvent.ANY, a);
		}
	}

	private void makeNewAnnotation(){
		currentAnnotation = new Annotation(this);
		this.addEventHandler(MouseEvent.ANY, currentAnnotation);
		annotations.add(currentAnnotation);
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
	//	
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
		System.out.println("Reaching here");

		BufferedReader reader = null;
		try {

			System.out.println("inside try block");
			reader = new BufferedReader(new FileReader(aFile));

			//Check for "FitsImageViewerAnnotations" at beginning of file to validate format
			if (reader.readLine().equalsIgnoreCase("FitsImageViewerAnnotations")){ //continue
				String line;
				Annotation annotation = new Annotation(this);

				System.out.println("fetching annotations from file");

				//Delimit annotations with "*"
				while ((line = reader.readLine()) != null){
					//Within annotations, delimit lines by newline

					System.out.println("fetching lines from file");

					if (line.startsWith("l")){
						annotation.addLine(lineFromString(line));
					}
					else if (line.equalsIgnoreCase("*")){
						annotations.add(annotation);
						annotation = new Annotation(this);
					}
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Annotations can now be rendered
		drawAll();
	}

	public Line lineFromString(String lString){
		lString = lString.substring(1, lString.length()-1);
		String[] coords = lString.split(" ");
		Line line = new Line();

		for (int i = 0; i<coords.length; i++){
			try{
				System.out.println(coords[i]);
				String[] coord = coords[i].split(",");
				System.out.println(coord[0]);
				System.out.println(coord[1]);
				line.appendPoint(new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return line;
	}

}
