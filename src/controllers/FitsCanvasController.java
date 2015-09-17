package controllers;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.util.BufferedFile;
import services.ExtractFitsRegion;
import views.FitsCanvas;
import views.FitsImageViewBox;
import views.MainWindow;

public class FitsCanvasController {

	private FitsCanvas fitsCanvas;
	private FitsImageViewBox imageViewBox;
	private ImageController imageController;
	private AnnotationsController annotationsController;
	private SelectionsController selectionsController;

	private MainWindow ui;

	public FitsCanvasController(MainWindow mainWindow, ImageController imageController){
		this.ui = mainWindow;
		this.imageController = imageController;
		initialise(imageController.getImageViewBox());
		this.annotationsController = new AnnotationsController(this);
		this.selectionsController = new SelectionsController(this);
	}

	public void initialise(FitsImageViewBox imageViewBox){
		this.imageViewBox = imageViewBox;
		this.fitsCanvas = imageViewBox.getFitsCanvas();
	}

	public EventHandler<ActionEvent> saveAnnotations() {
		return (final ActionEvent e) -> {
			File file = (File) ui.showSaveDialog("TXT");
			fitsCanvas.writeAnnotationsToFile(file);
		};
	}

	public EventHandler<ActionEvent> openAnnotations() {
		return (final ActionEvent e) -> {
			File file = ui.openFile("Select an annotation file", "TXT");
			fitsCanvas.addAnnotationsFromFile(file);
		};
	}

	public EventHandler<ActionEvent> toggleAnnotationsVisible(
			CheckBox hideAnnotationsButton) {
		return (final ActionEvent e) -> {
			try{
				if (hideAnnotationsButton.isSelected()){
					fitsCanvas.hideAnnotations();
				}
				else {
					fitsCanvas.drawAllAnnotations();
				}
			} catch (NullPointerException e1){
				/*annotation layer does not exist yet, so do nothing*/
			}
		};
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
	
	public void resetZoom(){
		imageController.resetZoom();
	}

	public void drawAll() {
		fitsCanvas.getGraphicsContext2D().clearRect(0, 0, fitsCanvas.getWidth(), fitsCanvas.getHeight());
		fitsCanvas.drawAllSelections();
		fitsCanvas.drawAllAnnotations();
		
	}

	public MainWindow getMainUi() {
		return ui;
	}

	public AnnotationsController getAnnotationsController() {
		return annotationsController;
	}
	
	public SelectionsController getSelectionsController() {
		return selectionsController;
	}

	public FitsCanvas getCanvas() {
		return fitsCanvas;
	}

}
