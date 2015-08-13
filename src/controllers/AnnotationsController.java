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
	
	private MainWindow ui;
	
	public AnnotationsController(MainWindow mainWindow){
		this.ui = mainWindow;
	}
	
	public void initialise(FitsImageViewBox imageViewBox){
		this.imageViewBox = imageViewBox;
		this.annotationLayer = imageViewBox.getAnnotationLayer();
	}
	
	public EventHandler<ActionEvent> toggleDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable drawing mode
				annotationLayer.setDrawMode(true);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				annotationLayer.setDrawMode(false);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				annotationLayer.setFillMode(true);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				annotationLayer.setFillMode(false);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable drawing mode
				annotationLayer.setMaskDrawMode(true);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				annotationLayer.setMaskDrawMode(false);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				annotationLayer.setMaskFillMode(true);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				annotationLayer.setMaskFillMode(false);
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
			if (hideAnnotationsButton.isSelected()){
				annotationLayer.hideAnnotations();
			}
			else {
				annotationLayer.drawAllAnnotations();
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

}
