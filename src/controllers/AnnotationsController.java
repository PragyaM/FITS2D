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
import services.CreateMask;
import views.AnnotationLayer;
import views.FitsImageViewBox;
import views.MainWindow;

public class AnnotationsController {

	private AnnotationLayer annotationLayer;
	private FitsImageViewBox imageViewBox;
	private ImageController imageController;

	private MainWindow ui;

	public AnnotationsController(MainWindow mainWindow, ImageController imageController){
		this.ui = mainWindow;
		this.imageController = imageController;
	}

	public void initialise(FitsImageViewBox imageViewBox){
		this.imageViewBox = imageViewBox;
		this.annotationLayer = imageViewBox.getAnnotationLayer();
	}

	public EventHandler<ActionEvent> toggleDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable drawing mode
					annotationLayer.setDrawMode(true);
					imageViewBox.setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable drawing mode
					annotationLayer.setDrawMode(false);
					imageViewBox.setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}

	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable fill mode
					annotationLayer.setFillMode(true);
					imageViewBox.setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable fill mode
					annotationLayer.setFillMode(false);
					imageViewBox.setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}

	public EventHandler<ActionEvent> toggleMaskDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable drawing mode
					annotationLayer.setMaskDrawMode(true);
					imageViewBox.setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable drawing mode
					annotationLayer.setMaskDrawMode(false);
					imageViewBox.setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}

	public EventHandler<ActionEvent> toggleMaskFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			try {
				if (toggle.isSelected()){ //enable fill mode
					annotationLayer.setMaskFillMode(true);
					imageViewBox.setPannable(false);
				}
				else if (!toggle.isSelected()){ //disable fill mode
					annotationLayer.setMaskFillMode(false);
					imageViewBox.setPannable(true);
				}
			} catch (NullPointerException e1){
				/*There is no image yet*/
			}
		};
	}

	public EventHandler<ActionEvent> saveAnnotations() {
		return (final ActionEvent e) -> {
			File file = (File) ui.showSaveDialog("TXT");
			annotationLayer.writeAnnotationsToFile(file);
		};
	}

	public EventHandler<ActionEvent> openAnnotations() {
		return (final ActionEvent e) -> {
			File file = ui.openFile("Select an annotation file", "TXT");
			annotationLayer.addAnnotationsFromFile(file);
		};
	}

	public EventHandler<ActionEvent> toggleAnnotationsVisible(
			CheckBox hideAnnotationsButton) {
		return (final ActionEvent e) -> {
			try{
				if (hideAnnotationsButton.isSelected()){
					annotationLayer.hideAnnotations();
				}
				else {
					annotationLayer.drawAllAnnotations();
				}
			} catch (NullPointerException e1){
				/*annotation layer does not exist yet, so do nothing*/
			}
		};
	}

	public EventHandler<ActionEvent> createMaskFromSelection() {
		return (final ActionEvent e) -> {
			ArrayList<Point> fullSelection = annotationLayer.getSelectedArea();
			try {
				Fits maskFits = CreateMask.mapToFits(fullSelection, imageViewBox.getFitsImage(), 
						(int) annotationLayer.getWidth(), 
						(int) annotationLayer.getHeight());

				BufferedFile bf = new BufferedFile(ui.showSaveDialog("FITS"), "rw");
				maskFits.write(bf);
				bf.close();
			} catch (FitsException e1) {
				System.out.println(e1.getMessage());
			} catch (IOException e1) {
				System.out.println("cancelled");
			}
		};
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
	
	public void resetZoom(){
		imageController.resetZoom();
	}

}
