package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.stage.Stage;
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
	
	public EventHandler<? super MouseEvent> setImageViewMode(){
		setImageViewMode(true);
		setImageAnnotateMode(false);
		return (final MouseEvent e) -> {
			ui.getImageViewBox().setPannable(true);
		};
	}
	
	public EventHandler<? super MouseEvent> setImageAnnotateMode(){
		setImageViewMode(false);
		setImageAnnotateMode(true);
		return (final MouseEvent e) -> {
			ui.getImageViewBox().setupAnnotationLayer();
			ui.getImageViewBox().setPannable(false);
		};
	}

	public boolean isImageViewMode() {
		return imageViewMode;
	}

	public void setImageViewMode(boolean imageViewMode) {
		this.imageViewMode = imageViewMode;
		System.out.println("viewing: " + imageViewMode);
	}

	public boolean isImageAnnotateMode() {
		return imageAnnotateMode;
	}

	public void setImageAnnotateMode(boolean imageAnnotateMode) {
		this.imageAnnotateMode = imageAnnotateMode;
		System.out.println("annotating: " + imageAnnotateMode);
	}
}
