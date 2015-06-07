package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import models.Annotation;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;
	private boolean imageViewMode = true;
	private boolean imageAnnotateMode = false;

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
				setImageViewMode(false);
				setImageAnnotateMode(true);
				ui.getImageViewBox().getAnnotationLayer().turnDrawModeOn();
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				setImageViewMode(true);
				setImageAnnotateMode(false);
				ui.getImageViewBox().getAnnotationLayer().turnDrawModeOff();
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				setImageViewMode(false);
				setImageAnnotateMode(true);
				ui.getImageViewBox().getAnnotationLayer().turnFillModeOn();
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				setImageViewMode(true);
				setImageAnnotateMode(false);
				ui.getImageViewBox().getAnnotationLayer().turnFillModeOff();
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskDrawMode(ToggleButton toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable drawing mode
				setImageViewMode(false);
				setImageAnnotateMode(true);
				ui.getImageViewBox().getAnnotationLayer().turnDrawModeOn();
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable drawing mode
				setImageViewMode(true);
				setImageAnnotateMode(false);
				ui.getImageViewBox().getAnnotationLayer().turnDrawModeOff();
				ui.getImageViewBox().setPannable(true);
			}
		};
	}
	
	public EventHandler<ActionEvent> toggleMaskFillMode(ToggleButton toggle) {
		return (final ActionEvent e) -> {
			if (toggle.isSelected()){ //enable fill mode
				setImageViewMode(false);
				setImageAnnotateMode(true);
				ui.getImageViewBox().getAnnotationLayer().turnFillModeOn();
				ui.getImageViewBox().setPannable(false);
			}
			else if (!toggle.isSelected()){ //disable fill mode
				setImageViewMode(true);
				setImageAnnotateMode(false);
				ui.getImageViewBox().getAnnotationLayer().turnFillModeOff();
				ui.getImageViewBox().setPannable(true);
			}
		};
	}

	public boolean isImageViewMode() {
		return imageViewMode;
	}

	public void setImageViewMode(boolean imageViewMode) {
		this.imageViewMode = imageViewMode;
	}

	public boolean isImageAnnotateMode() {
		return imageAnnotateMode;
	}

	public void setImageAnnotateMode(boolean imageAnnotateMode) {
		this.imageAnnotateMode = imageAnnotateMode;
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
