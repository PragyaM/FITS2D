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
import models.FitsImage;
import models.PixelRegion;
import models.Selection;
import services.ChanneliseFitsHeader;
import services.ConvertWcsPixels;
import uk.ac.starlink.ast.FitsChan;
import controllers.AnnotationsController;
import controllers.FitsCanvasController;
import controllers.SelectionsController;

public class FitsCanvas extends Canvas{
	private ArrayList<Annotation> annotations;
	private ArrayList<Selection> selections;
	private Annotation currentAnnotation;
	private Selection currentSelection;
	public enum Mode {NONE, ANNOTATION_DRAW, ANNOTATION_FILL, 
		SELECTION_DRAW, SELECTION_FILL};
		public Mode mode;
		private GraphicsContext gc;
		private AnnotationsController annotationsController;
		private SelectionsController selectionsController;
		private FitsCanvasController controller;

		public FitsCanvas(double width, double height, FitsCanvasController controller){
			super(width, height);
			annotations = new ArrayList<Annotation>();
			selections = new ArrayList<Selection>();
			gc = this.getGraphicsContext2D();
			gc.setLineWidth(2);
			setMode(Mode.NONE);
			this.annotationsController = controller.getAnnotationsController();
			this.selectionsController = controller.getSelectionsController();
			this.controller = controller;
		}

		public void setMode(Mode mode){
			this.mode = mode;
		}

		public Annotation getCurrentAnnotation() {
			return currentAnnotation;
		}

		public void setCurrentAnnotation(Annotation currentAnnotation) {
			this.currentAnnotation = currentAnnotation;
		}

		public Selection getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(Selection currentSelection) {
			this.currentSelection = currentSelection;
		}

		public ArrayList<Annotation> getAnnotations() {
			return annotations;
		}

		public void setAnnotations(ArrayList<Annotation> annotations) {
			this.annotations = annotations;
		}

		public ArrayList<Selection> getSelections() {
			return selections;
		}

		public void setSelections(ArrayList<Selection> selections) {
			this.selections = selections;
		}

		public void turnAnnotatingOff(){
			for (Annotation a : annotations){
				this.removeEventHandler(MouseEvent.ANY, a);
			}
		}

		public void turnSelectingOff(){
			for (Selection s : selections){
				this.removeEventHandler(MouseEvent.ANY, s);
			}
		}

		public void makeNewAnnotation(){
			currentAnnotation = new Annotation(this, annotationsController, Color.RED);
			this.addEventHandler(MouseEvent.ANY, currentAnnotation);
			annotations.add(currentAnnotation);
		}

		public void makeNewSelection(){
			currentSelection = new Selection(this, selectionsController, Color.YELLOW);
			this.addEventHandler(MouseEvent.ANY, currentSelection);
			selections.add(currentSelection);
		}

		public ArrayList<Point> getSelectedArea(){
			//TODO create mask using "selection" annotations
			ArrayList<Point> fullSelection = new ArrayList<Point>();
			for (Selection a : selections){
				for (PixelRegion r : a.getRegions()){
					fullSelection.addAll(r.getImagePixels());
				}
			}
			return fullSelection;
		}

		/**
		 * writes image pixels of the annotations on the image to a text file which
		 * can be read and understood by the program
		 * @param aFile
		 */
		public void writeAnnotationsToFile(File aFile){
			BufferedWriter writer = null;
			String fileDescriptorString = "FITS2D\n";
			FitsImage fitsImage = controller.getImageViewBox().getFitsImage();
			String headerString = fitsImage.getWcsHeaderCardsString();
			StringBuilder annotationsString = new StringBuilder();

			for (Annotation a : annotations){
				if (!a.isEmpty()){
					annotationsString.append(a.toString());
				}
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
				if (reader.readLine().equalsIgnoreCase("FITS2D")){ //continue

					String line = reader.readLine();

					//read associated FITS header
					while (line != null){
						headerString = headerString + line + "\n";
						line = reader.readLine();
						if (line.startsWith("END")) break;
					}

					/* Convert image pixels for new image using wcs conversion */
					FitsChan oldFits = ChanneliseFitsHeader.chanFromHeaderString(headerString);
					FitsChan newFits = ChanneliseFitsHeader.chanFromHeaderObj(
							controller.getImageViewBox().getFitsImage().getHDU().getHeader());
					ConvertWcsPixels wcsConverter = new ConvertWcsPixels(oldFits, newFits);

					Annotation annotation = new Annotation(this, annotationsController, Color.RED);

					/* Delimit annotations with "a" */
					while ((line = reader.readLine()) != null){
						/* Within annotations, delimit regions with "r" */
						if (line.startsWith("r")){
							annotation.addRegion(regionFromString(line, wcsConverter));
						}
						else if (line.equalsIgnoreCase("a")){
							annotations.add(annotation);
							annotation = new Annotation(this, annotationsController, Color.RED);
						}
					}
					annotations.add(annotation);
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
			annotationsController.drawAll();
			makeNewAnnotation();
		}

		public PixelRegion regionFromString(String rString, ConvertWcsPixels wcsConverter){
			rString = rString.substring(1, rString.length()-1);
			rString = rString.trim();
			String[] coords = rString.split(" ");
			PixelRegion region = new PixelRegion();

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
			region.generateCanvasPixels(this.getHeight(), 
					controller.getImageViewBox().getFitsImage().getHeight());

			return region;
		}

		public void clear(){
			gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		}

		public void eraseAll(){
			annotations.clear();
			selections.clear();
			clear();
		}

		public void drawAll(){
			annotationsController.drawAll();
			selectionsController.drawAll();
		}

		public void writeSelectionsToFile(File file) {
			BufferedWriter writer = null;
			String fileDescriptorString = "FITS2D\n";
			FitsImage fitsImage = controller.getImageViewBox().getFitsImage();
			String headerString = fitsImage.getWcsHeaderCardsString();
			StringBuilder selectionString = new StringBuilder();

			for (Selection s : selections){
				if (!s.isEmpty()){
					selectionString.append(s.toString());
				}
			}

			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(fileDescriptorString);
				writer.write(headerString);
				writer.write(selectionString.toString());
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

		public void addSelectionsFromFile(File file) {
			BufferedReader reader = null;
			String headerString = "";
			try {
				reader = new BufferedReader(new FileReader(file));

				//Check for "FITS2D" at beginning of file to validate format
				if (reader.readLine().equalsIgnoreCase("FITS2D")){ //continue

					String line = reader.readLine();

					//read associated FITS header
					while (line != null){
						headerString = headerString + line + "\n";
						line = reader.readLine();
						if (line.startsWith("END")) break;
					}

					/* Convert image pixels for new image using wcs conversion */
					FitsChan oldFits = ChanneliseFitsHeader.chanFromHeaderString(headerString);
					FitsChan newFits = ChanneliseFitsHeader.chanFromHeaderObj(
							controller.getImageViewBox().getFitsImage().getHDU().getHeader());
					ConvertWcsPixels wcsConverter = new ConvertWcsPixels(oldFits, newFits);

					Selection selection = new Selection(this, selectionsController, Color.YELLOW);

					/* Delimit selections with "s" */
					while ((line = reader.readLine()) != null){
						/* Within selections, delimit regions with "r" */
						if (line.startsWith("r")){
							selection.addRegion(regionFromString(line, wcsConverter));
						}
						else if (line.equalsIgnoreCase("s")){
							selections.add(selection);
							selection = new Selection(this, selectionsController, Color.YELLOW);
						}
					}
					selections.add(selection);
				}
				else {
					System.out.println("Not a valid selection file");
					//TODO add user alert
				}

				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Selections can now be rendered
			selectionsController.drawAll();
			makeNewSelection();
		}
}
