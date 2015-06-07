package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;

	public GUIController(Application app){
		//this is where previously customized configurations should be applied
	}

	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage);
		ui.addTopMenuBar(this);
		ui.addImageViewBox();
		ui.addToolsAreaBox(this);
		ui.display();
		ui.getImageViewBox().setOnZoom(ui.zoomImage(this));
	}

	public EventHandler<javafx.event.ActionEvent> openFits(){
		return ui.showFitsFileChooser();
	}

	public EventHandler<javafx.event.ActionEvent> toggleImageScrollbars(CheckMenuItem toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()) {
				ui.getImageViewBox().enableScrollBars();
			}
			else  ui.getImageViewBox().disableScrollBars();
		};
	}

	public EventHandler<ActionEvent> toggleDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable drawing mode
				ui.getImageViewBox().getAnnotationLayer().setDrawMode(true);
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				ui.getImageViewBox().getAnnotationLayer().setDrawMode(false);
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				ui.getImageViewBox().getAnnotationLayer().setFillMode(true);
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				ui.getImageViewBox().getAnnotationLayer().setFillMode(false);
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable drawing mode
				ui.getImageViewBox().getAnnotationLayer().setMaskDrawMode(true);
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				ui.getImageViewBox().getAnnotationLayer().setMaskDrawMode(false);
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				ui.getImageViewBox().getAnnotationLayer().setMaskFillMode(true);
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				ui.getImageViewBox().getAnnotationLayer().setMaskFillMode(false);
				ui.getImageViewBox().setPannable(true);
			}
		};
	}

	public EventHandler<ActionEvent> saveAnnotations() {
		return (final ActionEvent e) -> {
			ui.getImageViewBox().getAnnotationLayer().writeAnnotationsToFile("annotations");
		};
	}

	public EventHandler<ActionEvent> openAnnotations() {
		return ui.openAnnotationsFromFile();
	}

	public EventHandler<ActionEvent> toggleAnnotationsVisible(
			CheckBox hideAnnotationsButton) {
		return (final ActionEvent e) -> {
			if (hideAnnotationsButton.isSelected()){
				ui.getImageViewBox().getAnnotationLayer().hideAnnotations();
			}
			else {
				ui.getImageViewBox().getAnnotationLayer().drawAll();
			}
		};
	}
}
