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
import javafx.scene.paint.Color;
import models.Annotation;
import models.Region;

public class AnnotationLayer extends Canvas{
	private ArrayList<Annotation> annotations;
	private ArrayList<Annotation> selections;
	private GraphicsContext gc;
	private Annotation currentAnnotation;
	private Annotation currentSelection;
	public enum Mode {NONE, ANNOTATE_DRAW, ANNOTATE_FILL, MASK_DRAW, MASK_FILL};
	public Mode mode;

	public AnnotationLayer(double width, double height){
		super(width, height);
		annotations = new ArrayList<Annotation>();
		selections = new ArrayList<Annotation>();
		gc = this.getGraphicsContext2D();
		gc.setLineWidth(2);
		mode = Mode.NONE;
	}
	
	public void setDrawMode(Boolean enabled){
		if (enabled){
			//TODO: turn cursor into pencil
			turnSelectingOff();
			if (currentAnnotation == null){
				makeNewAnnotation();
			} else this.addEventHandler(MouseEvent.ANY, currentAnnotation);
			mode = Mode.ANNOTATE_DRAW;
		} else {
			turnAnnotatingOff();
			mode = Mode.NONE;
		}
	}
	
	public void setFillMode(Boolean enabled){
		if (enabled){
			//TODO: turn cursor into bucket
			turnSelectingOff();
			if (currentAnnotation == null){
				makeNewAnnotation();
			} else this.addEventHandler(MouseEvent.ANY, currentAnnotation);
			mode = Mode.ANNOTATE_FILL;
		} else {
			turnAnnotatingOff();
			mode = Mode.NONE;
		}
	}
	
	public void setMaskDrawMode(Boolean enabled){
		if (enabled){
			//TODO: turn cursor into pencil
			turnAnnotatingOff();
			if (currentSelection == null){
				makeNewSelection();
			} else this.addEventHandler(MouseEvent.ANY, currentSelection);
			mode = Mode.MASK_DRAW;
		} else {
			turnSelectingOff();
			mode = Mode.NONE;
		}
	}
	
	public void setMaskFillMode(Boolean enabled){
		if (enabled){
			//TODO: turn cursor into bucket
			turnAnnotatingOff();
			if (currentSelection == null){
				makeNewSelection();
			} else this.addEventHandler(MouseEvent.ANY, currentSelection);
			mode = Mode.MASK_FILL;
		} else {
			turnSelectingOff();
			mode = Mode.NONE;
		}
	}

	public void turnAnnotatingOff(){
		for (Annotation a : annotations){
			this.removeEventHandler(MouseEvent.ANY, a);
		}
	}
	
	public void turnSelectingOff(){
		for (Annotation a : selections){
			this.removeEventHandler(MouseEvent.ANY, a);
		}
	}

	private void makeNewAnnotation(){
		currentAnnotation = new Annotation(this, Color.WHITE);
		this.addEventHandler(MouseEvent.ANY, currentAnnotation);
		annotations.add(currentAnnotation);
	}
	
	private void makeNewSelection(){
		currentSelection = new Annotation(this, Color.YELLOW);
		this.addEventHandler(MouseEvent.ANY, currentSelection);
		selections.add(currentSelection);
	}

	public void drawAllAnnotations(){
		System.out.println("reaching here");
		for (Annotation annotation : annotations) {
			System.out.println("drawing an annotation");
			annotation.draw();
		}
	}
	
	public void drawAllSelections(){
		for (Annotation a : selections) {
			a.draw();
		}
	}

		
	public ArrayList<Point> getSelectedArea(){
		//TODO create mask using "selection" annotations
		ArrayList<Point> fullSelection = new ArrayList<Point>();
		for (Annotation a : selections){
			for (Region l : a.getRegions()){
				fullSelection.addAll(l.getPixels());
			}
		}
		return fullSelection;
	}
		

	public void hideAnnotations(){
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		drawAllSelections();
	}

	public void writeAnnotationsToFile(File aFile){
		BufferedWriter writer = null;
		String annotationsString = "FitsImageViewerAnnotations";

		for (Annotation a : annotations){
			annotationsString = annotationsString + "\n*" + a.toString();
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
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(aFile));

			//Check for "FitsImageViewerAnnotations" at beginning of file to validate format
			if (reader.readLine().equalsIgnoreCase("FitsImageViewerAnnotations")){ //continue
				String line;
				Annotation annotation = new Annotation(this, Color.WHITE);

				//Delimit annotations with "*"
				while ((line = reader.readLine()) != null){
					//Within annotations, delimit regions by newline

					if (line.startsWith("r")){
						annotation.addRegion(regionFromString(line));
					}
					else if (line.equalsIgnoreCase("*")){
						annotations.add(annotation);
						annotation = new Annotation(this, Color.WHITE);
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
		drawAllAnnotations();
	}

	public Region regionFromString(String rString){
		rString = rString.substring(1, rString.length()-1);
		rString = rString.trim();
		String[] coords = rString.split(" ");
		Region region = new Region();

		for (int i = 0; i<coords.length; i++){
			try{
				String[] coord = coords[i].split(",");
				region.add(new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return region;
	}
	
	//methods to add later:
//	public void undo(){}
//	public void redo(){}
//	public void selectionFromAnnotations(ArrayList<Annotation> selectedAnnotations){}

}
