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
import models.AnnotationRegion;
import services.ConvertWcsPixels;
import services.ChanneliseFitsHeader;
import uk.ac.starlink.ast.FitsChan;

public class AnnotationLayer extends Canvas{
	private ArrayList<Annotation> annotations;
	private ArrayList<Annotation> selections;
	private GraphicsContext gc;
	private Annotation currentAnnotation;
	private Annotation currentSelection;
	public enum Mode {NONE, ANNOTATE_DRAW, ANNOTATE_FILL, MASK_DRAW, MASK_FILL};
	public Mode mode;
	private FitsImageViewBox container;

	public AnnotationLayer(double width, double height, FitsImageViewBox viewBox){
		super(width, height);
		annotations = new ArrayList<Annotation>();
		selections = new ArrayList<Annotation>();
		gc = this.getGraphicsContext2D();
		gc.setLineWidth(2);
		mode = Mode.NONE;
		this.container = viewBox;
	}
	
	public void setDrawMode(Boolean enabled){
		if (enabled){
			//TODO: turn cursor into pencil
			turnSelectingOff();
			if (currentAnnotation == null){
				makeNewAnnotation();
			} else this.addEventHandler(MouseEvent.ANY, currentAnnotation);
			mode = Mode.ANNOTATE_DRAW;
			container.setPannable(false);
		} else {
			turnAnnotatingOff();
			mode = Mode.NONE;
			container.setPannable(true);
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
			container.setPannable(false);
		} else {
			turnAnnotatingOff();
			mode = Mode.NONE;
			container.setPannable(true);
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
			container.setPannable(false);
		} else {
			turnSelectingOff();
			mode = Mode.NONE;
			container.setPannable(true);
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
			container.setPannable(false);
		} else {
			turnSelectingOff();
			mode = Mode.NONE;
			container.setPannable(true);
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
		currentAnnotation = new Annotation(this, container, container.getFitsImage(), Color.RED);
		this.addEventHandler(MouseEvent.ANY, currentAnnotation);
		annotations.add(currentAnnotation);
	}
	
	private void makeNewSelection(){
		currentSelection = new Annotation(this, container, container.getFitsImage(), Color.YELLOW);
		this.addEventHandler(MouseEvent.ANY, currentSelection);
		selections.add(currentSelection);
	}

	public void drawAllAnnotations(){
		for (Annotation annotation : annotations) {
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
			for (AnnotationRegion r : a.getRegions()){
				fullSelection.addAll(r.getImagePixels());
			}
		}
		return fullSelection;
	}
		

	public void hideAnnotations(){
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		drawAllSelections();
	}

	/**
	 * writes image pixels of the annotations on the image to a text file which
	 * can be read and understood by the program
	 * @param aFile
	 */
	public void writeAnnotationsToFile(File aFile){
		BufferedWriter writer = null;
		String fileDescriptorString = "FitsImageViewerAnnotations\n";
		String headerString = this.container.getFitsImage().getHeaderString();
		StringBuilder annotationsString = new StringBuilder();

		for (Annotation a : annotations){
			annotationsString.append(a.toString());
			annotationsString.append("\n*\n");
		}

		try {
			writer = new BufferedWriter(new FileWriter(aFile));
			writer.write(fileDescriptorString);
			writer.write(headerString);
			writer.write(annotationsString.toString());
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
		String headerString = "";
		try {
			reader = new BufferedReader(new FileReader(aFile));

			//Check for "FitsImageViewerAnnotations" at beginning of file to validate format
			if (reader.readLine().equalsIgnoreCase("FitsImageViewerAnnotations")){ //continue
				
				String line = reader.readLine();
				
				//read associated FITS header
				while (line != null){
					headerString = headerString + line + "\n";
					System.out.println(line);
					line = reader.readLine();
					if (line.startsWith("END")) break;
				}
				
				/* Convert image pixels for new image using wcs conversion */
				FitsChan oldFits = ChanneliseFitsHeader.chanFromHeaderString(headerString);
				FitsChan newFits = ChanneliseFitsHeader.chanFromHeaderObj(container.getFitsImage().getHDU().getHeader());
				ConvertWcsPixels wcsConverter = new ConvertWcsPixels(oldFits, newFits);
				
				Annotation annotation = new Annotation(this, container, container.getFitsImage(), Color.RED);

				//Delimit annotations with "*"
				while ((line = reader.readLine()) != null){
					//Within annotations, delimit regions by "r"
					if (line.startsWith("r")){
						annotation.addRegion(regionFromString(line, wcsConverter));
					}
					else if (line.equalsIgnoreCase("*")){
						annotations.add(annotation);
						annotation = new Annotation(this, container, container.getFitsImage(), Color.RED);
					}
				}
			}
			else {
				System.out.println("Not a valid annotation file");
				//TODO add user alert
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

	public AnnotationRegion regionFromString(String rString, ConvertWcsPixels wcsConverter){
		rString = rString.substring(1, rString.length()-1);
		rString = rString.trim();
		String[] coords = rString.split(" ");
		AnnotationRegion region = new AnnotationRegion();

		ArrayList<Point> oldImagePixels = new ArrayList<Point>();
		
		for (int i = 0; i<coords.length; i++){
			try{
				String[] coord = coords[i].split(",");
				Point p = new Point();
				p.setLocation(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
				oldImagePixels.add(p);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/* convert all old image pixels for new image  */
		region.addAllImagePixels(wcsConverter.convertPixels(oldImagePixels));
		region.generateCanvasPixels(this.getHeight(), container.getFitsImage().getHeight());
		
		return region;
	}
	
	//methods to add later:
//	public void undo(){}
//	public void redo(){}
//	public void selectionFromAnnotations(ArrayList<Annotation> selectedAnnotations){}

}
